package com.opositores.android.ui.topics;

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
import com.opositores.android.data.remote.model.BlockModel;
import com.opositores.android.data.remote.model.TopicModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra la lista de bloques con sus temas para una oposición.
 * Carga los bloques primero y luego carga temas por bloque al expandir.
 */
public class TopicListFragment extends Fragment {

    private long oppositionId;
    private RecyclerView rvBlocks;

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
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);
        rvBlocks = view.findViewById(R.id.rvBlocks);
        rvBlocks.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadBlocks();
        return view;
    }

    private void loadBlocks() {
        RetrofitClient.getInstance(requireContext())
                .getApiService()
                .getBlocks(oppositionId)
                .enqueue(new Callback<List<BlockModel>>() {
                    @Override
                    public void onResponse(Call<List<BlockModel>> call, Response<List<BlockModel>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            setupBlockAdapter(resp.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<List<BlockModel>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Error al cargar bloques", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupBlockAdapter(List<BlockModel> blocks) {
        BlockWithTopicsAdapter adapter = new BlockWithTopicsAdapter(blocks,
                blockId -> loadTopicsForBlock(blockId), oppositionId);
        rvBlocks.setAdapter(adapter);
    }

    private void loadTopicsForBlock(long blockId) {
        RetrofitClient.getInstance(requireContext())
                .getApiService()
                .getTopics(blockId)
                .enqueue(new Callback<List<TopicModel>>() {
                    @Override
                    public void onResponse(Call<List<TopicModel>> call, Response<List<TopicModel>> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            // El adapter maneja la actualización de la lista de temas
                            BlockWithTopicsAdapter adapter = (BlockWithTopicsAdapter) rvBlocks.getAdapter();
                            if (adapter != null) {
                                adapter.setTopicsForBlock(blockId, resp.body());
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<List<TopicModel>> call, Throwable t) { /* silencioso */ }
                });
    }
}
