package com.opositores.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.opositores.android.data.local.entity.OppositionEntity;
import com.opositores.android.data.remote.model.BlockModel;
import com.opositores.android.data.remote.model.OppositionModel;
import com.opositores.android.repository.OppositionRepository;

import java.util.List;

public class OppositionViewModel extends AndroidViewModel {

    private final OppositionRepository repository;
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<List<BlockModel>> blocks = new MutableLiveData<>();

    public OppositionViewModel(@NonNull Application application) {
        super(application);
        repository = new OppositionRepository(application);
    }

    public LiveData<List<OppositionEntity>> getOppositions() {
        return repository.getOppositions(new OppositionRepository.Callback<Void>() {
            @Override public void onSuccess(Void data) { /* caché actualizado */ }
            @Override public void onError(String message) { error.postValue(message); }
        });
    }

    public LiveData<String> getError() { return error; }
    public LiveData<List<BlockModel>> getBlocks() { return blocks; }

    public void createOpposition(String name, String scope, String targetDate, Double hours) {
        OppositionModel.CreateRequest req = new OppositionModel.CreateRequest(name, scope, targetDate, hours);
        repository.createOpposition(req, new OppositionRepository.Callback<OppositionModel>() {
            @Override public void onSuccess(OppositionModel data) { /* lista se actualiza por LiveData */ }
            @Override public void onError(String message) { error.postValue(message); }
        });
    }

    public void loadBlocks(long oppositionId) {
        repository.getBlocks(oppositionId, new OppositionRepository.Callback<List<BlockModel>>() {
            @Override public void onSuccess(List<BlockModel> data) { blocks.postValue(data); }
            @Override public void onError(String message) { error.postValue(message); }
        });
    }
}
