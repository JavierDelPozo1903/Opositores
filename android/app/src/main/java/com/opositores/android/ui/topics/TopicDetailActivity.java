package com.opositores.android.ui.topics;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.opositores.android.R;
import com.opositores.android.data.remote.RetrofitClient;
import com.opositores.android.data.remote.model.TestModels;
import com.opositores.android.data.remote.model.TopicModel;
import com.opositores.android.ui.flashcards.FlashcardActivity;
import com.opositores.android.ui.tests.TestRunActivity;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopicDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TOPIC_ID = "topic_id";
    public static final String EXTRA_TOPIC_TITLE = "topic_title";
    public static final String EXTRA_TOPIC_STATUS = "topic_status";
    public static final String EXTRA_TOPIC_DIFFICULTY = "topic_difficulty";
    public static final String EXTRA_TOPIC_REVIEWS = "topic_reviews";
    public static final String EXTRA_OPPOSITION_ID = "opposition_id";

    private static final String[] STATUSES = {"NOT_STARTED", "STUDYING", "REVIEWED", "MASTERED"};
    private static final String[] STATUS_LABELS = {"Sin empezar", "Estudiando", "Repasado", "Dominado"};

    private long topicId;
    private long oppositionId;
    private Spinner spinnerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);

        topicId = getIntent().getLongExtra(EXTRA_TOPIC_ID, -1);
        oppositionId = getIntent().getLongExtra(EXTRA_OPPOSITION_ID, -1);
        String title = getIntent().getStringExtra(EXTRA_TOPIC_TITLE);
        String currentStatus = getIntent().getStringExtra(EXTRA_TOPIC_STATUS);
        int difficulty = getIntent().getIntExtra(EXTRA_TOPIC_DIFFICULTY, 1);
        int reviews = getIntent().getIntExtra(EXTRA_TOPIC_REVIEWS, 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        spinnerStatus = findViewById(R.id.spinnerStatus);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, STATUS_LABELS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        int idx = 0;
        for (int i = 0; i < STATUSES.length; i++) {
            if (STATUSES[i].equals(currentStatus)) { idx = i; break; }
        }
        spinnerStatus.setSelection(idx);

        TextView tvDifficulty = findViewById(R.id.tvDifficulty);
        TextView tvReviews = findViewById(R.id.tvReviews);
        tvDifficulty.setText("Dificultad: " + difficulty + "/5");
        tvReviews.setText("Repasos realizados: " + reviews);

        Button btnSaveStatus = findViewById(R.id.btnMakeTest);
        Button btnFlashcards = findViewById(R.id.btnFlashcards);

        btnSaveStatus.setOnClickListener(v -> {
            String selectedStatus = STATUSES[spinnerStatus.getSelectedItemPosition()];
            saveStatusAndLaunchTest(selectedStatus);
        });

        btnFlashcards.setOnClickListener(v -> {
            Intent intent = new Intent(this, FlashcardActivity.class);
            intent.putExtra(FlashcardActivity.EXTRA_TOPIC_ID, topicId);
            intent.putExtra(FlashcardActivity.EXTRA_TOPIC_TITLE, title);
            startActivity(intent);
        });
    }

    private void saveStatusAndLaunchTest(String status) {
        TopicModel.UpdateRequest req = new TopicModel.UpdateRequest();
        req.status = status;

        RetrofitClient.getInstance(this)
                .getApiService()
                .updateTopic(topicId, req)
                .enqueue(new Callback<TopicModel>() {
                    @Override
                    public void onResponse(Call<TopicModel> call, Response<TopicModel> resp) {
                        launchTestForTopic();
                    }
                    @Override
                    public void onFailure(Call<TopicModel> call, Throwable t) {
                        launchTestForTopic();
                    }
                });
    }

    private void launchTestForTopic() {
        TestModels.CreateRequest req = new TestModels.CreateRequest();
        req.oppositionId = oppositionId;
        req.topicIds = Collections.singletonList(topicId);
        req.numQuestions = 10;
        req.mode = "PRACTICE";

        RetrofitClient.getInstance(this)
                .getApiService()
                .createTest(req)
                .enqueue(new Callback<TestModels.TestDto>() {
                    @Override
                    public void onResponse(Call<TestModels.TestDto> call, Response<TestModels.TestDto> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            Intent intent = new Intent(TopicDetailActivity.this, TestRunActivity.class);
                            intent.putExtra("testId", resp.body().id);
                            startActivity(intent);
                        } else {
                            Toast.makeText(TopicDetailActivity.this,
                                    "No hay preguntas para este tema aún", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<TestModels.TestDto> call, Throwable t) {
                        Toast.makeText(TopicDetailActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
