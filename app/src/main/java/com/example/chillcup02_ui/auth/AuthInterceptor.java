package com.example.chillcup02_ui.auth;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.chillcup02_ui.data.local.AuthPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private static final String TAG = "AuthInterceptor";
    private AuthPreferences authPreferences;
    private FirebaseAuthManager firebaseAuthManager;

    public AuthInterceptor(Context context) {
        this.authPreferences = new AuthPreferences(context);
        this.firebaseAuthManager = FirebaseAuthManager.getInstance();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        // Priority: Firebase ID token > JWT token
        String token = null;

        // First, try to get Firebase ID token (for new Firebase users)
        String firebaseToken = firebaseAuthManager.getCurrentIdToken();
        if (firebaseToken != null && !firebaseToken.isEmpty()) {
            token = firebaseToken;
            Log.d(TAG, "Using Firebase ID token for request");
        } else {
            // Fallback to JWT token (for existing users)
            token = authPreferences.getAccessToken();
            if (token != null && !token.isEmpty()) {
                Log.d(TAG, "Using JWT token for request");
            }
        }

        // If we have a token, add it to the request
        if (token != null && !token.isEmpty()) {
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + token);

            Request newRequest = requestBuilder.build();
            return chain.proceed(newRequest);
        }

        // If no token, proceed with the original request
        Log.d(TAG, "No auth token available for request");
        return chain.proceed(original);
    }
}
