package com.opositores.android.data.remote;

import android.content.Context;

import com.opositores.android.BuildConfig;
import com.opositores.android.data.local.prefs.SessionManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static RetrofitClient instance;
    private final ApiService apiService;

    private RetrofitClient(Context context) {
        SessionManager sessionManager = new SessionManager(context);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                // Interceptor que adjunta el token JWT en cada petición autenticada
                .addInterceptor(chain -> {
                    String token = sessionManager.getToken();
                    okhttp3.Request original = chain.request();
                    if (token != null && !original.url().encodedPath().contains("/auth/")) {
                        okhttp3.Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .build();
                        return chain.proceed(request);
                    }
                    return chain.proceed(original);
                })
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context.getApplicationContext());
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
