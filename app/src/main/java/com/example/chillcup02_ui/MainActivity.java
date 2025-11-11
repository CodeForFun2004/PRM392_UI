package com.example.chillcup02_ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chillcup02_ui.databinding.ActivityMainBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.example.chillcup02_ui.ui.catalog.CatalogFragment;
import com.example.chillcup02_ui.ui.orders.OrdersFragment;
import com.example.chillcup02_ui.ui.profile.ProfileFragment;
import com.example.chillcup02_ui.ui.cart.CartFragment;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    private AuthViewModel authViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Initialize AuthViewModel (shared across fragments)
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        
        setupBottomNavigation();

        // If launched with intent asking to open orders or show success, do that
        boolean openOrderSuccess = getIntent().getBooleanExtra("open_order_success", false);
        boolean openOrders = getIntent().getBooleanExtra("open_orders", false);

        if (savedInstanceState == null) {
            if (openOrderSuccess) {
                String orderId = getIntent().getStringExtra("order_id");
                com.example.chillcup02_ui.ui.orders.OrderSuccessFragment f = com.example.chillcup02_ui.ui.orders.OrderSuccessFragment.newInstance(orderId);
                loadFragment(f);
                binding.bottomNavigation.setSelectedItemId(R.id.nav_orders);
            } else if (openOrders) {
                loadFragment(new OrdersFragment());
                binding.bottomNavigation.setSelectedItemId(R.id.nav_orders);
            } else {
                loadFragment(new CatalogFragment());
                binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
            }
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
            } else if (itemId == R.id.nav_cart) {
                selectedFragment = new CartFragment();
                
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

