package com.example.chillcup02_ui.ui.staff;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.ui.staff.dashboard.StaffDashboardFragment;
import com.example.chillcup02_ui.ui.staff.order.StaffOrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StaffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Load the default fragment when the activity is first created
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StaffDashboardFragment()).commit();
        }
    }

    // Listener for the bottom navigation items
    private final BottomNavigationView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        int itemId = item.getItemId();
        if (itemId == R.id.nav_dashboard) {
            selectedFragment = new StaffDashboardFragment();
        } else if (itemId == R.id.nav_orders) {
            selectedFragment = new StaffOrderFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
        return false;
    };
}
