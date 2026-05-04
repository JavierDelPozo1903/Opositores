package com.opositores.android.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/** Gestiona el token JWT y datos básicos de sesión en SharedPreferences */
public class SessionManager {

    private static final String PREFS_NAME = "opositores_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_PLAN = "subscription_plan";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveSession(String token, String email, String name, String plan) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_EMAIL, email)
                .putString(KEY_NAME, name)
                .putString(KEY_PLAN, plan)
                .apply();
    }

    public String getToken() { return prefs.getString(KEY_TOKEN, null); }
    public String getEmail() { return prefs.getString(KEY_EMAIL, null); }
    public String getName()  { return prefs.getString(KEY_NAME, null); }
    public String getPlan()  { return prefs.getString(KEY_PLAN, "FREE"); }

    public boolean isLoggedIn() { return getToken() != null; }

    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
