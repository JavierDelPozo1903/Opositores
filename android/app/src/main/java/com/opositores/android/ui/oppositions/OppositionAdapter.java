package com.opositores.android.ui.oppositions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.opositores.android.R;
import com.opositores.android.data.local.entity.OppositionEntity;

public class OppositionAdapter extends ListAdapter<OppositionEntity, OppositionAdapter.ViewHolder> {

    public interface OnOppositionClick {
        void onClick(OppositionEntity opposition);
    }

    private final OnOppositionClick listener;

    public OppositionAdapter(OnOppositionClick listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opposition, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OppositionEntity item = getItem(position);
        holder.tvName.setText(item.name);
        holder.tvScope.setText(item.scope != null ? item.scope : "General");
        holder.tvHours.setText(item.hoursPerWeek + " h/semana");
        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvScope, tvHours;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvOppositionName);
            tvScope = itemView.findViewById(R.id.tvOppositionScope);
            tvHours = itemView.findViewById(R.id.tvOppositionHours);
        }
    }

    private static final DiffUtil.ItemCallback<OppositionEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OppositionEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull OppositionEntity a, @NonNull OppositionEntity b) {
                    return a.id == b.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull OppositionEntity a, @NonNull OppositionEntity b) {
                    return a.name.equals(b.name) && a.scope != null && a.scope.equals(b.scope);
                }
            };
}
