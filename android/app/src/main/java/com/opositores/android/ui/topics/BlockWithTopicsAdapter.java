package com.opositores.android.ui.topics;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.opositores.android.R;
import com.opositores.android.data.remote.RetrofitClient;
import com.opositores.android.data.remote.model.BlockModel;
import com.opositores.android.data.remote.model.TopicModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockWithTopicsAdapter extends RecyclerView.Adapter<BlockWithTopicsAdapter.BlockViewHolder> {

    private static final String[] STATUSES = {"NOT_STARTED", "STUDYING", "REVIEWED", "MASTERED"};

    public interface OnBlockExpand {
        void onExpand(long blockId);
    }

    private final List<BlockModel> blocks;
    private final Map<Long, List<TopicModel>> topicsMap = new HashMap<>();
    private final OnBlockExpand expandListener;
    private final long oppositionId;

    public BlockWithTopicsAdapter(List<BlockModel> blocks, OnBlockExpand expandListener, long oppositionId) {
        this.blocks = blocks;
        this.expandListener = expandListener;
        this.oppositionId = oppositionId;
    }

    @NonNull
    @Override
    public BlockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_block, parent, false);
        return new BlockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockViewHolder holder, int position) {
        BlockModel block = blocks.get(position);
        holder.tvBlockName.setText(block.name);

        List<TopicModel> topics = topicsMap.getOrDefault(block.id, new ArrayList<>());
        holder.tvTopicCount.setText(topics.size() + " temas");

        // Al pulsar el bloque, cargar sus temas
        holder.itemView.setOnClickListener(v -> {
            if (!topicsMap.containsKey(block.id)) {
                expandListener.onExpand(block.id);
            }
        });

        // Mostramos los temas cargados
        if (!topics.isEmpty()) {
            holder.rvTopics.setVisibility(View.VISIBLE);
            TopicAdapter topicAdapter = new TopicAdapter(topics,
                    (topicId, status) -> updateTopicStatus(holder, topicId, status), oppositionId);
            holder.rvTopics.setLayoutManager(
                    new androidx.recyclerview.widget.LinearLayoutManager(holder.itemView.getContext()));
            holder.rvTopics.setAdapter(topicAdapter);
        } else {
            holder.rvTopics.setVisibility(View.GONE);
        }
    }

    private void updateTopicStatus(BlockViewHolder holder, long topicId, String status) {
        TopicModel.UpdateRequest req = new TopicModel.UpdateRequest();
        req.status = status;
        RetrofitClient.getInstance(holder.itemView.getContext())
                .getApiService()
                .updateTopic(topicId, req)
                .enqueue(new Callback<TopicModel>() {
                    @Override
                    public void onResponse(Call<TopicModel> call, Response<TopicModel> resp) { /* ok */ }
                    @Override
                    public void onFailure(Call<TopicModel> call, Throwable t) { /* silencioso */ }
                });
    }

    public void setTopicsForBlock(long blockId, List<TopicModel> topics) {
        topicsMap.put(blockId, topics);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return blocks.size(); }

    static class BlockViewHolder extends RecyclerView.ViewHolder {
        TextView tvBlockName, tvTopicCount;
        RecyclerView rvTopics;

        BlockViewHolder(View view) {
            super(view);
            tvBlockName = view.findViewById(R.id.tvBlockName);
            tvTopicCount = view.findViewById(R.id.tvTopicCount);
            rvTopics = view.findViewById(R.id.rvTopics);
        }
    }
}
