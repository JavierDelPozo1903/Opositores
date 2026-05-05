package com.opositores.android.ui.planning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.opositores.android.R;
import com.opositores.android.data.remote.RetrofitClient;
import com.opositores.android.data.remote.model.PlanModels;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanFragment extends Fragment {

    private long oppositionId;
    private RecyclerView rvPlan;
    private TextView tvEmpty;
    private PlanSessionAdapter adapter;
    private final List<PlanModels.StudySessionDto> sessions = new ArrayList<>();

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
        tvEmpty = view.findViewById(R.id.tvPlanEmpty);
        Button btnGenerate = view.findViewById(R.id.btnGeneratePlan);

        rvPlan.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new PlanSessionAdapter(sessions, this::showStatusDialog);
        rvPlan.setAdapter(adapter);

        btnGenerate.setOnClickListener(v -> generatePlan());
        loadPlan();
        return view;
    }

    private void loadPlan() {
        String from = LocalDate.now().toString();
        String to = LocalDate.now().plusDays(30).toString();

        RetrofitClient.getInstance(requireContext())
                .getApiService()
                .getPlan(oppositionId, from, to)
                .enqueue(new Callback<List<PlanModels.StudySessionDto>>() {
                    @Override
                    public void onResponse(Call<List<PlanModels.StudySessionDto>> call,
                                           Response<List<PlanModels.StudySessionDto>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            sessions.clear();
                            sessions.addAll(resp.body());
                            adapter.notifyDataSetChanged();
                            tvEmpty.setVisibility(sessions.isEmpty() ? View.VISIBLE : View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlanModels.StudySessionDto>> call, Throwable t) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void generatePlan() {
        String start = LocalDate.now().toString();
        String end = LocalDate.now().plusDays(28).toString();
        PlanModels.GeneratePlanRequest req = new PlanModels.GeneratePlanRequest(start, end, 10.0);

        RetrofitClient.getInstance(requireContext())
                .getApiService()
                .generatePlan(oppositionId, req)
                .enqueue(new Callback<List<PlanModels.StudySessionDto>>() {
                    @Override
                    public void onResponse(Call<List<PlanModels.StudySessionDto>> call,
                                           Response<List<PlanModels.StudySessionDto>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            sessions.clear();
                            sessions.addAll(resp.body());
                            adapter.notifyDataSetChanged();
                            tvEmpty.setVisibility(sessions.isEmpty() ? View.VISIBLE : View.GONE);
                            Toast.makeText(requireContext(), "Plan generado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Error al generar plan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PlanModels.StudySessionDto>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showStatusDialog(PlanModels.StudySessionDto session) {
        String[] options = {"Completada", "Saltada", "Pendiente"};
        String[] statuses = {"COMPLETED", "SKIPPED", "PENDING"};

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cambiar estado")
                .setItems(options, (dialog, which) -> updateStatus(session, statuses[which]))
                .show();
    }

    private void updateStatus(PlanModels.StudySessionDto session, String newStatus) {
        Map<String, String> body = new HashMap<>();
        body.put("status", newStatus);

        RetrofitClient.getInstance(requireContext())
                .getApiService()
                .updateSessionStatus(session.id, body)
                .enqueue(new Callback<PlanModels.StudySessionDto>() {
                    @Override
                    public void onResponse(Call<PlanModels.StudySessionDto> call,
                                           Response<PlanModels.StudySessionDto> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            session.status = newStatus;
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<PlanModels.StudySessionDto> call, Throwable t) {
                        Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
