package com.opositores.android.repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.opositores.android.data.local.AppDatabase;
import com.opositores.android.data.local.dao.BlockDao;
import com.opositores.android.data.local.dao.OppositionDao;
import com.opositores.android.data.local.entity.BlockEntity;
import com.opositores.android.data.local.entity.OppositionEntity;
import com.opositores.android.data.remote.ApiService;
import com.opositores.android.data.remote.RetrofitClient;
import com.opositores.android.data.remote.model.BlockModel;
import com.opositores.android.data.remote.model.OppositionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OppositionRepository {

    private final ApiService api;
    private final OppositionDao oppositionDao;
    private final BlockDao blockDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    public OppositionRepository(Context context) {
        api = RetrofitClient.getInstance(context).getApiService();
        AppDatabase db = AppDatabase.getInstance(context);
        oppositionDao = db.oppositionDao();
        blockDao = db.blockDao();
    }

    /** Devuelve LiveData del caché local. Lanza fetch en paralelo para actualizar. */
    public LiveData<List<OppositionEntity>> getOppositions(Callback<Void> callback) {
        refreshOppositions(callback);
        return oppositionDao.getAll();
    }

    public void refreshOppositions(Callback<Void> callback) {
        api.getOppositions().enqueue(new retrofit2.Callback<List<OppositionModel>>() {
            @Override
            public void onResponse(Call<List<OppositionModel>> call, Response<List<OppositionModel>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    List<OppositionEntity> entities = new ArrayList<>();
                    for (OppositionModel m : resp.body()) {
                        OppositionEntity e = new OppositionEntity();
                        e.id = m.id;
                        e.name = m.name;
                        e.scope = m.scope;
                        e.targetExamDate = m.targetExamDate;
                        e.hoursPerWeek = m.hoursPerWeek != null ? m.hoursPerWeek : 0;
                        e.createdAt = m.createdAt;
                        entities.add(e);
                    }
                    executor.execute(() -> {
                        oppositionDao.deleteAll();
                        oppositionDao.insertAll(entities);
                        mainHandler.post(() -> { if (callback != null) callback.onSuccess(null); });
                    });
                } else {
                    mainHandler.post(() -> { if (callback != null) callback.onError("Error " + resp.code()); });
                }
            }

            @Override
            public void onFailure(Call<List<OppositionModel>> call, Throwable t) {
                mainHandler.post(() -> { if (callback != null) callback.onError(t.getMessage()); });
            }
        });
    }

    public void createOpposition(OppositionModel.CreateRequest req, Callback<OppositionModel> callback) {
        api.createOpposition(req).enqueue(new retrofit2.Callback<OppositionModel>() {
            @Override
            public void onResponse(Call<OppositionModel> call, Response<OppositionModel> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    OppositionModel model = resp.body();
                    executor.execute(() -> {
                        OppositionEntity e = new OppositionEntity();
                        e.id = model.id; e.name = model.name; e.scope = model.scope;
                        e.targetExamDate = model.targetExamDate;
                        e.hoursPerWeek = model.hoursPerWeek != null ? model.hoursPerWeek : 0;
                        oppositionDao.insert(e);
                    });
                    mainHandler.post(() -> callback.onSuccess(model));
                } else {
                    mainHandler.post(() -> callback.onError("Error " + resp.code()));
                }
            }

            @Override
            public void onFailure(Call<OppositionModel> call, Throwable t) {
                mainHandler.post(() -> callback.onError(t.getMessage()));
            }
        });
    }

    public void getBlocks(long oppositionId, Callback<List<BlockModel>> callback) {
        api.getBlocks(oppositionId).enqueue(new retrofit2.Callback<List<BlockModel>>() {
            @Override
            public void onResponse(Call<List<BlockModel>> call, Response<List<BlockModel>> resp) {
                if (resp.isSuccessful() && resp.body() != null) {
                    List<BlockModel> blocks = resp.body();
                    executor.execute(() -> {
                        List<BlockEntity> entities = new ArrayList<>();
                        for (BlockModel m : blocks) {
                            BlockEntity e = new BlockEntity();
                            e.id = m.id; e.oppositionId = m.oppositionId;
                            e.name = m.name; e.weight = m.weight != null ? m.weight : 1.0;
                            entities.add(e);
                        }
                        blockDao.deleteByOpposition(oppositionId);
                        blockDao.insertAll(entities);
                    });
                    mainHandler.post(() -> callback.onSuccess(blocks));
                } else {
                    mainHandler.post(() -> callback.onError("Error " + resp.code()));
                }
            }

            @Override
            public void onFailure(Call<List<BlockModel>> call, Throwable t) {
                mainHandler.post(() -> callback.onError(t.getMessage()));
            }
        });
    }
}
