package com.opositores.android.data.remote.model;

import com.google.gson.annotations.SerializedName;

public class AuthModels {

    public static class LoginRequest {
        @SerializedName("email")
        public String email;
        @SerializedName("password")
        public String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public static class RegisterRequest {
        @SerializedName("email")
        public String email;
        @SerializedName("password")
        public String password;
        @SerializedName("name")
        public String name;

        public RegisterRequest(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }
    }

    public static class AuthResponse {
        @SerializedName("token")
        public String token;
        @SerializedName("email")
        public String email;
        @SerializedName("name")
        public String name;
        @SerializedName("subscriptionPlan")
        public String subscriptionPlan;
    }
}
