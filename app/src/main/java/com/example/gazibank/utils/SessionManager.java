package com.example.gazibank.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "GaziBankSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_TC = "userTC";
    private static final String KEY_USER_NAME = "userName";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    // Oturum oluştur
    public void createLoginSession(int userId, String tc, String fullName) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_TC, tc);
        editor.putString(KEY_USER_NAME, fullName);
        editor.commit();
    }

    // Oturumu kapat
    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    // Giriş kontrolü
    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Kullanıcı ID'si al
    public int getUserId() {
        return preferences.getInt(KEY_USER_ID, -1);
    }

    // Kullanıcı TC al
    public String getUserTC() {
        return preferences.getString(KEY_USER_TC, null);
    }

    // Kullanıcı adı al
    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, null);
    }

    // Kullanıcı adını güncelle
    public void updateUserName(String fullName) {
        editor.putString(KEY_USER_NAME, fullName);
        editor.commit();
    }
}