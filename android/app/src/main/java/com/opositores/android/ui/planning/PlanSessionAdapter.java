package com.opositores.android.ui.planning;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.opositores.android.R;
import com.opositores.android.data.remote.model.PlanModels;

import java.util.List;
import java.util.Locale;

public class PlanSessionAdapter extends RecyclerView.Adapter<PlanSessionAdapter.ViewHolder> {

    private static final String[] MONTH_ABBR = {
        "ENE","FEB","MAR","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"
    };

    public interface OnStatusClick {
        void onStatusClick(PlanModels.StudySessionDto session);
    }

    private final List<PlanModels.StudySessionDto> sessions;
    private final OnStatusClick listener;

    public PlanSessionAdapter(List<PlanModels.StudySessionDto> sessions, OnStatusClick listener) {
        this.sessions = sessions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_session, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        PlanModels.StudySessionDto s = sessions.get(position);

        // Parsear fecha "YYYY-MM-DD"
        if (s.date != null && s.date.length() >= 10) {
            try {
                int month = Integer.parseInt(s.date.substring(5, 7)) - 1;
                String day = s.date.substring(8, 10);
                h.tvDay.setText(day);
                h.tvMonth.setText(month >= 0 && month < 12 ? MONTH_ABBR[month] : "");
            } catch (NumberFormatException ignored) {
                h.tvDay.setText("--");
                h.tvMonth.setText("");
            }
        }

        h.tvTopic.setText(s.topicTitle != null ? s.topicTitle : "Sesión libre");
        h.tvMinutes.setText(s.plannedMinutes != null ? s.plannedMinutes + " min de estudio" : "");

        String status = s.status != null ? s.status : "PENDING";
        switch (status) {
            case "COMPLETED":
                h.tvStatus.setText("✓ Completada");
                h.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
                h.tvStatus.setBackgroundColor(Color.parseColor("#E8F5E9"));
                break;
            case "SKIPPED":
                h.tvStatus.setText("✗ Saltada");
                h.tvStatus.setTextColor(Color.parseColor("#C62828"));
                h.tvStatus.setBackgroundColor(Color.parseColor("#FFEBEE"));
                break;
            default:
                h.tvStatus.setText("Pendiente");
                h.tvStatus.setTextColor(Color.parseColor("#1565C0"));
                h.tvStatus.setBackgroundColor(Color.parseColor("#E3F2FD"));
        }

        h.tvStatus.setOnClickListener(v -> listener.onStatusClick(s));
    }

    @Override
    public int getItemCount() { return sessions.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvMonth, tvTopic, tvMinutes, tvStatus;

        ViewHolder(View v) {
            super(v);
            tvDay = v.findViewById(R.id.tvSessionDay);
            tvMonth = v.findViewById(R.id.tvSessionMonth);
            tvTopic = v.findViewById(R.id.tvSessionTopic);
            tvMinutes = v.findViewById(R.id.tvSessionMinutes);
            tvStatus = v.findViewById(R.id.tvSessionStatus);
        }
    }
}
