package com.example.chillcup02_ui;

import android.app.Application;

import com.example.chillcup02_ui.data.api.ApiClient;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize ApiClient with application context
        ApiClient.init(this);
    }
}
