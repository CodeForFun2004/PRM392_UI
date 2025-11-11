package com.example.chillcup02_ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chillcup02_ui.databinding.ActivityMainBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.example.chillcup02_ui.ui.catalog.CatalogFragment;
import com.example.chillcup02_ui.ui.common.BaseActivity;
import com.example.chillcup02_ui.ui.orders.OrdersFragment;
import com.example.chillcup02_ui.ui.profile.ProfileFragment;

public class MainActivity extends BaseActivity {
    
    private ActivityMainBinding binding;
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Apply top window insets only to fragment container (for status bar)
        // Bottom navigation will handle its own insets automatically
        applyTopWindowInsets(binding.fragmentContainer);
        
        // Initialize AuthViewModel (shared across fragments)
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.init(this);
        
        setupBottomNavigation();
        
        // Load home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new CatalogFragment());
            binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }
    
    private void setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new CatalogFragment();
            } else if (itemId == R.id.nav_menu) {
                selectedFragment = new CatalogFragment(); // Same as home for now
            } else if (itemId == R.id.nav_orders) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }
            
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
    }
    
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}

