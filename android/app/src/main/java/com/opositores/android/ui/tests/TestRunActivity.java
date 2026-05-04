package com.opositores.android.ui.tests;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.opositores.android.databinding.ActivityTestRunBinding;
import com.opositores.android.data.remote.model.TestModels;
import com.opositores.android.viewmodel.TestViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunActivity extends AppCompatActivity {

    public static final String EXTRA_TEST_ID = "test_id";
    public static final String EXTRA_TIME_LIMIT = "time_limit_minutes";

    private ActivityTestRunBinding binding;
    private TestViewModel viewModel;

    private long testId;
    private List<TestModels.TestQuestionDto> questions;
    private int currentIndex = 0;
    private final Map<Long, String> answers = new HashMap<>();  // questionId -> selectedOption
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestRunBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        testId = getIntent().getLongExtra(EXTRA_TEST_ID, -1);
        int timeLimitMinutes = getIntent().getIntExtra(EXTRA_TIME_LIMIT, 0);

        viewModel = new ViewModelProvider(this).get(TestViewModel.class);

        setupObservers();
        loadTest();

        if (timeLimitMinutes > 0) startTimer(timeLimitMinutes);
    }

    private void loadTest() {
        com.opositores.android.data.remote.RetrofitClient.getInstance(this)
                .getApiService()
                .getTest(testId)
                .enqueue(new retrofit2.Callback<TestModels.TestDto>() {
                    @Override
                    public void onResponse(retrofit2.Call<TestModels.TestDto> call,
                                           retrofit2.Response<TestModels.TestDto> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            questions = resp.body().testQuestions;
                            showQuestion(0);
                        }
                    }
                    @Override
                    public void onFailure(retrofit2.Call<TestModels.TestDto> call, Throwable t) {
                        Toast.makeText(TestRunActivity.this, "Error al cargar el test", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void showQuestion(int index) {
        if (questions == null || index >= questions.size()) return;

        currentIndex = index;
        TestModels.TestQuestionDto tq = questions.get(index);
        TestModels.QuestionDto q = tq.question;

        binding.tvProgress.setText((index + 1) + "/" + questions.size());
        binding.tvQuestion.setText(q.questionText);
        binding.radioGroup.clearCheck();

        // Parseamos las opciones del JSON string
        String[] opts = new Gson().fromJson(q.options, String[].class);
        RadioButton[] radios = {binding.rbOption0, binding.rbOption1, binding.rbOption2, binding.rbOption3};

        for (int i = 0; i < radios.length; i++) {
            if (opts != null && i < opts.length) {
                radios[i].setVisibility(View.VISIBLE);
                radios[i].setText(opts[i]);
            } else {
                radios[i].setVisibility(View.GONE);
            }
        }

        // Restaurar respuesta previa si existe
        String prev = answers.get(q.id);
        if (prev != null && opts != null) {
            for (int i = 0; i < opts.length; i++) {
                if (opts[i].equals(prev)) radios[i].setChecked(true);
            }
        }

        // Botones de navegación
        binding.btnPrev.setEnabled(index > 0);
        binding.btnNext.setVisibility(index < questions.size() - 1 ? View.VISIBLE : View.GONE);
        binding.btnSubmit.setVisibility(index == questions.size() - 1 ? View.VISIBLE : View.GONE);

        binding.btnNext.setOnClickListener(v -> {
            saveCurrentAnswer();
            showQuestion(currentIndex + 1);
        });
        binding.btnPrev.setOnClickListener(v -> {
            saveCurrentAnswer();
            showQuestion(currentIndex - 1);
        });
        binding.btnSubmit.setOnClickListener(v -> confirmSubmit());
    }

    private void saveCurrentAnswer() {
        if (questions == null) return;
        TestModels.QuestionDto q = questions.get(currentIndex).question;
        String[] opts = new Gson().fromJson(q.options, String[].class);
        RadioButton[] radios = {binding.rbOption0, binding.rbOption1, binding.rbOption2, binding.rbOption3};

        for (int i = 0; i < radios.length; i++) {
            if (radios[i].isChecked() && opts != null && i < opts.length) {
                answers.put(q.id, opts[i]);
                return;
            }
        }
        // Sin selección: respuesta en blanco (no guardamos nada)
        answers.remove(q.id);
    }

    private void confirmSubmit() {
        saveCurrentAnswer();
        new AlertDialog.Builder(this)
                .setTitle("Enviar test")
                .setMessage("¿Seguro que quieres enviar el test? No podrás cambiarlo.")
                .setPositiveButton("Enviar", (d, w) -> submitTest())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void submitTest() {
        if (timer != null) timer.cancel();

        List<TestModels.AnswerDto> answerDtos = new ArrayList<>();
        if (questions != null) {
            for (TestModels.TestQuestionDto tq : questions) {
                String selected = answers.get(tq.question.id);
                answerDtos.add(new TestModels.AnswerDto(tq.question.id, selected));
            }
        }

        viewModel.submitTest(testId, new TestModels.SubmitRequest(answerDtos));
    }

    private void setupObservers() {
        viewModel.getTestResult().observe(this, result -> {
            // Mostramos el resultado en un diálogo
            String msg = String.format("Nota: %.1f\nAciertos: %d  |  Fallos: %d  |  Blancos: %d",
                    result.score, result.correctCount, result.incorrectCount, result.blankCount);

            new AlertDialog.Builder(this)
                    .setTitle("Resultado del test")
                    .setMessage(msg)
                    .setPositiveButton("Aceptar", (d, w) -> finish())
                    .setCancelable(false)
                    .show();
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });
    }

    private void startTimer(int minutes) {
        long millis = (long) minutes * 60 * 1000;
        timer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long remaining) {
                long mins = remaining / 60000;
                long secs = (remaining % 60000) / 1000;
                binding.tvTimer.setText(String.format("%02d:%02d", mins, secs));
            }

            @Override
            public void onFinish() {
                binding.tvTimer.setText("00:00");
                Toast.makeText(TestRunActivity.this, "Tiempo agotado", Toast.LENGTH_SHORT).show();
                submitTest();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
