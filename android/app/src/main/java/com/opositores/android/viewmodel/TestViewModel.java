package com.opositores.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.opositores.android.data.remote.RetrofitClient;
import com.opositores.android.data.remote.model.TestModels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestViewModel extends AndroidViewModel {

    private final MutableLiveData<TestModels.TestDto> testData = new MutableLiveData<>();
    private final MutableLiveData<TestModels.ResultDto> testResult = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public TestViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<TestModels.TestDto> getTestData() { return testData; }
    public LiveData<TestModels.ResultDto> getTestResult() { return testResult; }
    public LiveData<String> getError() { return error; }

    public void createTest(long oppositionId, List<Long> topicIds, int numQuestions, String mode) {
        TestModels.CreateRequest req = new TestModels.CreateRequest();
        req.oppositionId = oppositionId;
        req.topicIds = topicIds;
        req.numQuestions = numQuestions;
        req.mode = mode;

        RetrofitClient.getInstance(getApplication())
                .getApiService()
                .createTest(req)
                .enqueue(new Callback<TestModels.TestDto>() {
                    @Override
                    public void onResponse(Call<TestModels.TestDto> call, Response<TestModels.TestDto> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            testData.postValue(resp.body());
                        } else {
                            error.postValue("No se pudo crear el test: " + resp.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<TestModels.TestDto> call, Throwable t) {
                        error.postValue("Error de conexión: " + t.getMessage());
                    }
                });
    }

    public void submitTest(long testId, TestModels.SubmitRequest submitRequest) {
        RetrofitClient.getInstance(getApplication())
                .getApiService()
                .submitTest(testId, submitRequest)
                .enqueue(new Callback<TestModels.ResultDto>() {
                    @Override
                    public void onResponse(Call<TestModels.ResultDto> call, Response<TestModels.ResultDto> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            testResult.postValue(resp.body());
                        } else {
                            error.postValue("Error al enviar el test: " + resp.code());
                        }
                    }
                    @Override
                    public void onFailure(Call<TestModels.ResultDto> call, Throwable t) {
                        error.postValue("Error de conexión: " + t.getMessage());
                    }
                });
    }
}
