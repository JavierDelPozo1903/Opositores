package com.opositores.android.ui.tests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.opositores.android.databinding.ActivityTestCreationBinding;
import com.opositores.android.viewmodel.TestViewModel;

import java.util.ArrayList;

public class TestCreationActivity extends AppCompatActivity {

    public static final String EXTRA_OPPOSITION_ID = "opposition_id";

    private ActivityTestCreationBinding binding;
    private TestViewModel testViewModel;
    private long oppositionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        oppositionId = getIntent().getLongExtra(EXTRA_OPPOSITION_ID, -1);
        testViewModel = new ViewModelProvider(this).get(TestViewModel.class);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Nuevo test");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        testViewModel.getTestData().observe(this, test -> {
            binding.progressBar.setVisibility(View.GONE);
            // Cuando el test se crea, lanzamos la pantalla de realización
            Intent intent = new Intent(this, TestRunActivity.class);
            intent.putExtra(TestRunActivity.EXTRA_TEST_ID, test.id);
            intent.putExtra(TestRunActivity.EXTRA_TIME_LIMIT, test.timeLimitMinutes != null ? test.timeLimitMinutes : 0);
            startActivity(intent);
            finish();
        });

        testViewModel.getError().observe(this, error -> {
            binding.progressBar.setVisibility(View.GONE);
            binding.btnStartTest.setEnabled(true);
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        });
    }

    private void setupListeners() {
        binding.btnStartTest.setOnClickListener(v -> {
            String numStr = binding.etNumQuestions.getText().toString().trim();
            if (numStr.isEmpty()) {
                binding.etNumQuestions.setError("Indica el número de preguntas");
                return;
            }

            int numQuestions = Integer.parseInt(numStr);
            String mode = binding.radioExamMode.isChecked() ? "EXAM_SIMULATION" : "PRACTICE";

            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnStartTest.setEnabled(false);

            // Creamos el test con todos los temas de la oposición (sin filtro)
            testViewModel.createTest(oppositionId, new ArrayList<>(), numQuestions, mode);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
