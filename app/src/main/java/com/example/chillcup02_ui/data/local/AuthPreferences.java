package com.example.chillcup02_ui.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chillcup02_ui.data.dto.UserDto;
import com.google.gson.Gson;

public class AuthPreferences {
    
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_MOCK_USER = "mock_user";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    
    private final SharedPreferences prefs;
    private final Gson gson;
    
    public AuthPreferences(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    
    public void saveMockUser(UserDto user, String accessToken, String refreshToken) {
        SharedPreferences.Editor editor = prefs.edit();
        if (user != null) {
            editor.putString(KEY_MOCK_USER, gson.toJson(user));
        } else {
            editor.remove(KEY_MOCK_USER);
        }
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putString(KEY_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }
    
    public UserDto getMockUser() {
        String userJson = prefs.getString(KEY_MOCK_USER, null);
        if (userJson != null) {
            try {
                return gson.fromJson(userJson, UserDto.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    
    public String getAccessToken() {
        return prefs.getString(KEY_ACCESS_TOKEN, null);
    }
    
    public String getRefreshToken() {
        return prefs.getString(KEY_REFRESH_TOKEN, null);
    }
    
    public void clear() {
        prefs.edit()
                .remove(KEY_MOCK_USER)
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .apply();
    }
    
    public boolean hasMockUser() {
        return prefs.contains(KEY_MOCK_USER);
    }
}


