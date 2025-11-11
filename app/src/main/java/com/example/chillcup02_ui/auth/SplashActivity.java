package com.example.chillcup02_ui.auth;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.chillcup02_ui.MainActivity;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.ui.staff.StaffActivity; // Import StaffActivity
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupStatusBar();
        setContentView(R.layout.activity_splash);

        // Navigate after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // --- DEVELOPMENT SHORTCUT: Go directly to StaffActivity ---
            navigateToStaff();

            /* --- ORIGINAL LOGIC ---
            // When ready for production, remove the line above and uncomment this block.
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                // TODO: Here you would use UserRepository to fetch the user's role from your backend.
                // Based on the role, you would navigateToStaff() or navigateToMain().
                navigateToMain(); // Defaulting to main for now.
            } else {
                navigateToMain(); // Or navigateToLogin() if you have a separate login screen.
            }
            */
        }, SPLASH_DELAY);
    }

    private void setupStatusBar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.primary_green, null));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = window.getDecorView();
            int flags = decorView.getSystemUiVisibility();
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat windowInsetsController = WindowCompat.getInsetsController(window, window.getDecorView());
            if (windowInsetsController != null) {
                windowInsetsController.setAppearanceLightStatusBars(false);
            }
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Development shortcut to navigate directly to the staff activity
    private void navigateToStaff() {
        Intent intent = new Intent(this, StaffActivity.class);
        // Pass a dummy store ID since our repository is using mock data anyway
        intent.putExtra("STORE_ID", "mock_store_123");
        startActivity(intent);
        finish(); // Call finish so the user can't go back to the splash screen
    }
}
