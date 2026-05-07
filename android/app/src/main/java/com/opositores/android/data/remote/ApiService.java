package com.opositores.android.data.remote;

import com.opositores.android.data.remote.model.AuthModels;
import com.opositores.android.data.remote.model.BlockModel;
import com.opositores.android.data.remote.model.FlashcardModel;
import com.opositores.android.data.remote.model.OppositionModel;
import com.opositores.android.data.remote.model.PlanModels;
import com.opositores.android.data.remote.model.TestModels;
import com.opositores.android.data.remote.model.TopicModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // ─── Auth ───────────────────────────────────────────────────────────────

    @POST("auth/login")
    Call<AuthModels.AuthResponse> login(@Body AuthModels.LoginRequest req);

    @POST("auth/register")
    Call<AuthModels.AuthResponse> register(@Body AuthModels.RegisterRequest req);

    // ─── Oposiciones ────────────────────────────────────────────────────────

    @GET("oppositions")
    Call<List<OppositionModel>> getOppositions();

    @GET("oppositions/{id}")
    Call<OppositionModel> getOpposition(@Path("id") long id);

    @POST("oppositions")
    Call<OppositionModel> createOpposition(@Body OppositionModel.CreateRequest req);

    @PUT("oppositions/{id}")
    Call<OppositionModel> updateOpposition(@Path("id") long id, @Body OppositionModel.CreateRequest req);

    @DELETE("oppositions/{id}")
    Call<Void> deleteOpposition(@Path("id") long id);

    // ─── Bloques ────────────────────────────────────────────────────────────

    @GET("oppositions/{id}/blocks")
    Call<List<BlockModel>> getBlocks(@Path("id") long oppositionId);

    @POST("oppositions/{id}/blocks")
    Call<BlockModel> createBlock(@Path("id") long oppositionId, @Body BlockModel req);

    // ─── Temas ──────────────────────────────────────────────────────────────

    @GET("blocks/{id}/topics")
    Call<List<TopicModel>> getTopics(@Path("id") long blockId);

    @POST("blocks/{id}/topics")
    Call<TopicModel> createTopic(@Path("id") long blockId, @Body TopicModel req);

    @PUT("topics/{id}")
    Call<TopicModel> updateTopic(@Path("id") long topicId, @Body TopicModel.UpdateRequest req);

    // ─── Plan de estudio ────────────────────────────────────────────────────

    @POST("oppositions/{id}/plan")
    Call<List<PlanModels.StudySessionDto>> generatePlan(
            @Path("id") long oppositionId,
            @Body PlanModels.GeneratePlanRequest req);

    @GET("oppositions/{id}/plan")
    Call<List<PlanModels.StudySessionDto>> getPlan(
            @Path("id") long oppositionId,
            @Query("from") String from,
            @Query("to") String to);

    @PATCH("study-sessions/{id}")
    Call<PlanModels.StudySessionDto> updateSessionStatus(
            @Path("id") long sessionId,
            @Body Map<String, String> body);

    // ─── Tests ──────────────────────────────────────────────────────────────

    @POST("tests")
    Call<TestModels.TestDto> createTest(@Body TestModels.CreateRequest req);

    @GET("tests/{id}")
    Call<TestModels.TestDto> getTest(@Path("id") long testId);

    @POST("tests/{id}/submit")
    Call<TestModels.ResultDto> submitTest(@Path("id") long testId,
                                          @Body TestModels.SubmitRequest req);

    // ─── Flashcards ─────────────────────────────────────────────────────────

    @GET("topics/{id}/flashcards")
    Call<List<FlashcardModel>> getFlashcards(@Path("id") long topicId);

    // ─── Estadísticas ───────────────────────────────────────────────────────

    @GET("stats/overview")
    Call<Map<String, Object>> getStatsOverview();

    @GET("stats/opposition/{id}")
    Call<Map<String, Object>> getOppositionStats(@Path("id") long oppositionId);
}
