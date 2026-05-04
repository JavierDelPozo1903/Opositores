package com.opositores.android.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView tvProgress, tvAvgScore, tvTotalTopics;

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
                        if (resp.isSuccessful() && resp.body() != null) {
                            Map<String, Object> data = resp.body();
                            Object progress = data.get("progressPercent");
                            Object total = data.get("totalTopics");
                            Object reviewed = data.get("reviewedTopics");

                            if (tvProgress != null) {
                                tvProgress.setText(String.format("Avance: %.1f%%",
                                        progress instanceof Number ? ((Number) progress).doubleValue() : 0.0));
                            }
                            if (tvTotalTopics != null) {
                                tvTotalTopics.setText(String.format("Temas: %s / %s repasados",
                                        total, reviewed));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) { /* silencioso */ }
                });
    }
}
