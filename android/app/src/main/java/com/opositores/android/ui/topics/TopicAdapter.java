package com.opositores.android.ui.topics;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.opositores.android.R;
import com.opositores.android.data.remote.model.TopicModel;

import java.util.Arrays;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private static final String[] STATUSES = {"NOT_STARTED", "STUDYING", "REVIEWED", "MASTERED"};
    private static final String[] STATUS_LABELS = {"Sin empezar", "Estudiando", "Repasado", "Dominado"};

    public interface OnStatusChange {
        void onChange(long topicId, String newStatus);
    }

    private final List<TopicModel> topics;
    private final OnStatusChange statusChangeListener;
    private final long oppositionId;

    public TopicAdapter(List<TopicModel> topics, OnStatusChange listener, long oppositionId) {
        this.topics = topics;
        this.statusChangeListener = listener;
        this.oppositionId = oppositionId;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        TopicModel topic = topics.get(position);
        String prefix = topic.officialNumber != null ? topic.officialNumber + ". " : "";
        holder.tvTitle.setText(prefix + topic.title);
        holder.tvInfo.setText("Dif: " + topic.difficulty + " | Prio: " + topic.priority);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                holder.itemView.getContext(), android.R.layout.simple_spinner_item, STATUS_LABELS);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinnerStatus.setAdapter(spinnerAdapter);

        int currentIndex = Arrays.asList(STATUSES).indexOf(topic.status);
        if (currentIndex >= 0) holder.spinnerStatus.setSelection(currentIndex, false);

        holder.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean first = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (first) { first = false; return; }
                topic.status = STATUSES[pos];
                statusChangeListener.onChange(topic.id, STATUSES[pos]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TopicDetailActivity.class);
            intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_ID, topic.id);
            intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_TITLE, topic.title);
            intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_STATUS, topic.status);
            intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_DIFFICULTY, topic.difficulty);
            intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_REVIEWS, topic.reviewCount);
            intent.putExtra(TopicDetailActivity.EXTRA_OPPOSITION_ID, oppositionId);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return topics.size(); }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvInfo;
        Spinner spinnerStatus;

        TopicViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTopicTitle);
            tvInfo = view.findViewById(R.id.tvTopicInfo);
            spinnerStatus = view.findViewById(R.id.spinnerTopicStatus);
        }
    }
}
