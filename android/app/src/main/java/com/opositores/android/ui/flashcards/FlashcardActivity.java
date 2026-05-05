package com.opositores.android.ui.flashcards;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.opositores.android.R;
import com.opositores.android.data.remote.RetrofitClient;
import com.opositores.android.data.remote.model.FlashcardModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardActivity extends AppCompatActivity {

    public static final String EXTRA_TOPIC_ID = "topic_id";
    public static final String EXTRA_TOPIC_TITLE = "topic_title";

    private final List<FlashcardModel> flashcards = new ArrayList<>();
    private int currentIndex = 0;
    private boolean answerVisible = false;

    private TextView tvProgress, tvQuestion, tvAnswer, tvEmpty;
    private View divider;
    private Button btnReveal, btnPrev, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        long topicId = getIntent().getLongExtra(EXTRA_TOPIC_ID, -1);
        String title = getIntent().getStringExtra(EXTRA_TOPIC_TITLE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title != null ? title : "Flashcards");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvProgress = findViewById(R.id.tvProgress);
        tvQuestion = findViewById(R.id.tvQuestion);
        tvAnswer = findViewById(R.id.tvAnswer);
        tvEmpty = findViewById(R.id.tvEmpty);
        divider = findViewById(R.id.divider);
        btnReveal = findViewById(R.id.btnReveal);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        btnReveal.setOnClickListener(v -> revealAnswer());
        btnPrev.setOnClickListener(v -> navigate(-1));
        btnNext.setOnClickListener(v -> navigate(1));

        loadFlashcards(topicId);
    }

    private void loadFlashcards(long topicId) {
        RetrofitClient.getInstance(this)
                .getApiService()
                .getFlashcards(topicId)
                .enqueue(new Callback<List<FlashcardModel>>() {
                    @Override
                    public void onResponse(Call<List<FlashcardModel>> call,
                                           Response<List<FlashcardModel>> resp) {
                        if (resp.isSuccessful() && resp.body() != null && !resp.body().isEmpty()) {
                            flashcards.addAll(resp.body());
                            showCard(0);
                        } else {
                            showEmpty();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FlashcardModel>> call, Throwable t) {
                        showEmpty();
                    }
                });
    }

    private void showCard(int index) {
        if (flashcards.isEmpty()) { showEmpty(); return; }
        currentIndex = index;
        answerVisible = false;
        FlashcardModel card = flashcards.get(index);

        tvProgress.setText((index + 1) + " / " + flashcards.size());
        tvQuestion.setText(card.question);
        tvAnswer.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
        btnReveal.setVisibility(View.VISIBLE);

        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < flashcards.size() - 1);
    }

    private void revealAnswer() {
        if (currentIndex >= flashcards.size()) return;
        tvAnswer.setText(flashcards.get(currentIndex).answer);
        tvAnswer.setVisibility(View.VISIBLE);
        divider.setVisibility(View.VISIBLE);
        btnReveal.setVisibility(View.GONE);
        answerVisible = true;
    }

    private void navigate(int delta) {
        int next = currentIndex + delta;
        if (next >= 0 && next < flashcards.size()) {
            showCard(next);
        }
    }

    private void showEmpty() {
        tvEmpty.setVisibility(View.VISIBLE);
        tvProgress.setVisibility(View.GONE);
        tvQuestion.setVisibility(View.GONE);
        btnReveal.setVisibility(View.GONE);
        btnPrev.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        divider.setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
