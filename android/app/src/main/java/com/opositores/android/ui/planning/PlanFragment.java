package com.opositores.android.ui.planning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.opositores.android.R;
import com.opositores.android.data.remote.RetrofitClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanFragment extends Fragment {

    private long oppositionId;
    private RecyclerView rvPlan;

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
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        rvPlan = view.findViewById(R.id.rvPlanSessions);
        rvPlan.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadPlan();
        return view;
    }

    private void loadPlan() {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(30); // próximas 4 semanas

        RetrofitClient.getInstance(requireContext())
                .getApiService()
                .getOppositionStats(oppositionId) // reutilizamos endpoint stats como placeholder
                .enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> resp) {
                        // En una implementación completa aquí mostraríamos las sesiones del plan
                        // usando el endpoint GET /oppositions/{id}/plan?from=...&to=...
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Error al cargar el plan", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
