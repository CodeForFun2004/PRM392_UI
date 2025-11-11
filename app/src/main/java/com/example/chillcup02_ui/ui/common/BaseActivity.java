package com.example.chillcup02_ui.ui.common;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public abstract class BaseActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Enable edge-to-edge display
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        
        // Setup window insets controller
        setupWindowInsets();
    }
    
    private void setupWindowInsets() {
        Window window = getWindow();
        WindowInsetsControllerCompat windowInsetsController = 
            WindowCompat.getInsetsController(window, window.getDecorView());
        
        if (windowInsetsController != null) {
            // Configure status bar appearance
            windowInsetsController.setAppearanceLightStatusBars(false);
        }
    }
    
    /**
     * Apply window insets padding to a view
     * Call this method in onCreate after setContentView
     */
    protected void applyWindowInsets(View rootView) {
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            int systemBars = WindowInsetsCompat.Type.systemBars();
            Insets systemBarInsets = insets.getInsets(systemBars);
            
            // Apply padding to avoid system bars
            v.setPadding(
                systemBarInsets.left,
                systemBarInsets.top,
                systemBarInsets.right,
                systemBarInsets.bottom
            );
            
            return insets;
        });
    }
    
    /**
     * Apply window insets padding only to top (for status bar)
     * Useful for fragment containers that have bottom navigation
     */
    protected void applyTopWindowInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int systemBars = WindowInsetsCompat.Type.systemBars();
            Insets systemBarInsets = insets.getInsets(systemBars);
            
            // Apply padding only to top and sides (not bottom)
            v.setPadding(
                systemBarInsets.left,
                systemBarInsets.top,
                systemBarInsets.right,
                0
            );
            
            return insets;
        });
    }
}
