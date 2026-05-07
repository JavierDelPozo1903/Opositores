package com.opositores.android.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.opositores.android.R;
import com.opositores.android.data.remote.RetrofitClient;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatsFragment extends Fragment {

    private long oppositionId;
    private TextView tvProgress, tvAvgScore, tvTotalTopics, tvReviewedTopics;
    private TextView tvCountNotStarted, tvCountStudying, tvCountReviewed, tvCountMastered;
    private ProgressBar progressBarStudy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            oppositionId = getArguments().getLong("oppositionId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        tvProgress = view.findViewById(R.id.tvProgress);
        tvAvgScore = view.findViewById(R.id.tvAvgScore);
        tvTotalTopics = view.findViewById(R.id.tvTotalTopics);
        tvReviewedTopics = view.findViewById(R.id.tvReviewedTopics);
        tvCountNotStarted = view.findViewById(R.id.tvCountNotStarted);
        tvCountStudying = view.findViewById(R.id.tvCountStudying);
        tvCountReviewed = view.findViewById(R.id.tvCountReviewed);
        tvCountMastered = view.findViewById(R.id.tvCountMastered);
        progressBarStudy = view.findViewById(R.id.progressBarStudy);

        loadStats();
        return view;
    }

    private void loadStats() {
        RetrofitClient.getInstance(requireContext())
                .getApiService()
                .getOppositionStats(oppositionId)
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> resp) {
                        if (!isAdded() || resp.body() == null) return;
                        Map<String, Object> data = resp.body();

                        double progress = getDouble(data, "progressPercent");
                        int total = getInt(data, "totalTopics");
                        int reviewed = getInt(data, "reviewedTopics");
                        double avgScore = getDouble(data, "avgScore");

                        int countNotStarted = getInt(data, "notStartedCount");
                        int countStudying = getInt(data, "studyingCount");
                        int countReviewed = getInt(data, "reviewedCount");
                        int countMastered = getInt(data, "masteredCount");

                        tvProgress.setText(String.format("%.1f%%", progress));
                        progressBarStudy.setProgress((int) progress);
                        tvTotalTopics.setText(String.valueOf(total));
                        tvReviewedTopics.setText(String.valueOf(reviewed));

                        if (avgScore > 0) {
                            tvAvgScore.setText(String.format("%.1f / 10", avgScore));
                        } else {
                            tvAvgScore.setText("Sin tests aún");
                        }

                        tvCountNotStarted.setText(String.valueOf(countNotStarted));
                        tvCountStudying.setText(String.valueOf(countStudying));
                        tvCountReviewed.setText(String.valueOf(countReviewed));
                        tvCountMastered.setText(String.valueOf(countMastered));
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {}
                });
    }

    private double getDouble(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val instanceof Number ? ((Number) val).doubleValue() : 0.0;
    }

    private int getInt(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val instanceof Number ? ((Number) val).intValue() : 0;
    }
}
