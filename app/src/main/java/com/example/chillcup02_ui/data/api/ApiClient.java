package com.example.chillcup02_ui.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Try multiple possible URLs for different testing scenarios
    private static final String[] POSSIBLE_BASE_URLS = {
        "http://10.0.2.2:8080",    // Android emulator
        "http://192.168.12.73:8080", // Common local network IP (adjust as needed)
        "http://localhost:8080"     // Direct localhost (for testing)
    };

    private static final String BASE_URL = "http://10.0.2.2:8080"; // Default to emulator
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create logging interceptor
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Create OkHttp client
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            // Create Gson instance
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Create Retrofit instance
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
