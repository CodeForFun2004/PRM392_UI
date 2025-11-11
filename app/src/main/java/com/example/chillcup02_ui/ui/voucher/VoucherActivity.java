package com.example.chillcup02_ui.ui.voucher;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.data.api.MockDiscountService;
import com.example.chillcup02_ui.data.dto.DiscountDto;
import com.example.chillcup02_ui.databinding.ActivityVoucherBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.example.chillcup02_ui.ui.common.BaseActivity;
import com.example.chillcup02_ui.util.Result;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends BaseActivity {
    
    private ActivityVoucherBinding binding;
    private AuthViewModel authViewModel;
    private MockDiscountService mockDiscountService;
    private VoucherAdapter voucherAdapter;
    private String currentFilter = "false"; // "false" = Có thể sử dụng, "true" = Đã sử dụng
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoucherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Apply window insets to root view
        applyWindowInsets(binding.getRoot());
        
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mockDiscountService = MockDiscountService.getInstance();
        
        setupToolbar();
        setupTabs();
        setupRecyclerView();
        loadVouchers();
    }
    
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Vouchers của bạn");
        }
        
        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupTabs() {
        // Tab "Có thể sử dụng"
        binding.tabUsable.setOnClickListener(v -> {
            if (!"false".equals(currentFilter)) {
                currentFilter = "false";
                updateTabSelection();
                loadVouchers();
            }
        });
        
        // Tab "Đã sử dụng"
        binding.tabUsed.setOnClickListener(v -> {
            if (!"true".equals(currentFilter)) {
                currentFilter = "true";
                updateTabSelection();
                loadVouchers();
            }
        });
        
        updateTabSelection();
    }
    
    private void updateTabSelection() {
        if ("false".equals(currentFilter)) {
            // Có thể sử dụng selected
            binding.tabUsable.setBackgroundResource(R.drawable.badge_new_background);
            binding.tabUsable.setBackgroundTintList(getColorStateList(R.color.primary_green));
            binding.tabUsable.setTextColor(getColor(R.color.white));
            
            binding.tabUsed.setBackground(null);
            binding.tabUsed.setTextColor(getColor(R.color.text_secondary));
        } else {
            // Đã sử dụng selected
            binding.tabUsed.setBackgroundResource(R.drawable.badge_new_background);
            binding.tabUsed.setBackgroundTintList(getColorStateList(R.color.primary_green));
            binding.tabUsed.setTextColor(getColor(R.color.white));
            
            binding.tabUsable.setBackground(null);
            binding.tabUsable.setTextColor(getColor(R.color.text_secondary));
        }
    }
    
    private void setupRecyclerView() {
        voucherAdapter = new VoucherAdapter();
        binding.rvVouchers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvVouchers.setAdapter(voucherAdapter);
    }
    
    private void loadVouchers() {
        // Check if view is still available
        if (binding == null || getWindow() == null) {
            return;
        }
        
        String userId = authViewModel.getUserId();
        if (userId == null) {
            // Try to get from mock user
            com.example.chillcup02_ui.data.dto.UserDto mockUser = authViewModel.getMockUser();
            if (mockUser != null) {
                userId = mockUser.getId();
            }
        }
        
        if (userId == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem vouchers", Toast.LENGTH_SHORT).show();
            if (voucherAdapter != null) {
                voucherAdapter.setVouchers(new ArrayList<>());
            }
            return;
        }
        
        binding.progressBar.setVisibility(View.VISIBLE);
        
        final String finalUserId = userId;
        mockDiscountService.getUserDiscounts(finalUserId, currentFilter, result -> {
            // Check if view is still available in callback
            if (binding == null || getWindow() == null) {
                return;
            }
            
            binding.progressBar.setVisibility(View.GONE);
            
            if (result.isSuccess()) {
                List<DiscountDto> vouchers = result.getData();
                if (vouchers != null && voucherAdapter != null) {
                    voucherAdapter.setVouchers(vouchers);
                } else if (voucherAdapter != null) {
                    voucherAdapter.setVouchers(new ArrayList<>());
                }
            } else {
                Toast.makeText(VoucherActivity.this, 
                        result.getError() != null ? result.getError() : "Không thể tải vouchers", 
                        Toast.LENGTH_SHORT).show();
                if (voucherAdapter != null) {
                    voucherAdapter.setVouchers(new ArrayList<>());
                }
            }
        });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

