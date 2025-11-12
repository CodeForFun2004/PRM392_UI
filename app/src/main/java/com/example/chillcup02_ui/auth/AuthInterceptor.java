package com.example.chillcup02_ui.auth;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.chillcup02_ui.data.local.AuthPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private AuthPreferences authPreferences;

    public AuthInterceptor(Context context) {
        this.authPreferences = new AuthPreferences(context);
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        // Get the access token
        String token = authPreferences.getAccessToken();

        // If we have a token, add it to the request
        if (token != null && !token.isEmpty()) {
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + token);

            Request newRequest = requestBuilder.build();
            return chain.proceed(newRequest);
        }

        // If no token, proceed with the original request
        return chain.proceed(original);
    }
}
