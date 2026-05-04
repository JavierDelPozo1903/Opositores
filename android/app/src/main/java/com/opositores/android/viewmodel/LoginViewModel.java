package com.opositores.android.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.opositores.android.data.local.prefs.SessionManager;
import com.opositores.android.data.remote.RetrofitClient;
import com.opositores.android.data.remote.model.AuthModels;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private final MutableLiveData<AuthResult> authResult = new MutableLiveData<>();
    private final SessionManager sessionManager;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        sessionManager = new SessionManager(application);
    }

    public LiveData<AuthResult> getAuthResult() { return authResult; }

    public void login(String email, String password) {
        RetrofitClient.getInstance(getApplication())
                .getApiService()
                .login(new AuthModels.LoginRequest(email, password))
                .enqueue(new Callback<AuthModels.AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthModels.AuthResponse> call,
                                           Response<AuthModels.AuthResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            AuthModels.AuthResponse body = resp.body();
                            sessionManager.saveSession(body.token, body.email, body.name, body.subscriptionPlan);
                            authResult.postValue(new AuthResult(true, null));
                        } else {
                            authResult.postValue(new AuthResult(false, "Credenciales incorrectas"));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthModels.AuthResponse> call, Throwable t) {
                        authResult.postValue(new AuthResult(false, "Error de conexión: " + t.getMessage()));
                    }
                });
    }

    public void register(String email, String password, String name) {
        RetrofitClient.getInstance(getApplication())
                .getApiService()
                .register(new AuthModels.RegisterRequest(email, password, name))
                .enqueue(new Callback<AuthModels.AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthModels.AuthResponse> call,
                                           Response<AuthModels.AuthResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            AuthModels.AuthResponse body = resp.body();
                            sessionManager.saveSession(body.token, body.email, body.name, body.subscriptionPlan);
                            authResult.postValue(new AuthResult(true, null));
                        } else {
                            authResult.postValue(new AuthResult(false, "Error en el registro"));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthModels.AuthResponse> call, Throwable t) {
                        authResult.postValue(new AuthResult(false, "Error de conexión"));
                    }
                });
    }

    public static class AuthResult {
        public final boolean success;
        public final String errorMessage;

        public AuthResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }
    }
}
