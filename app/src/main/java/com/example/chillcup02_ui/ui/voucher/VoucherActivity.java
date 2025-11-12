package com.example.chillcup02_ui.ui.voucher;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.data.api.DiscountService;
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
    private DiscountService discountService;
    private VoucherAdapter voucherAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoucherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Apply window insets to root view
        applyWindowInsets(binding.getRoot());
        
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        discountService = DiscountService.getInstance();
        
        setupToolbar();
        setupRecyclerView();
        loadVouchers();
    }
    
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mã giảm giá");
        }

        binding.toolbar.setNavigationOnClickListener(v -> finish());
    }
    
    private void setupRecyclerView() {
        voucherAdapter = new VoucherAdapter();
        binding.rvVouchers.setLayoutManager(new LinearLayoutManager(this));
        binding.rvVouchers.setAdapter(voucherAdapter);
    }
    
    private void loadVouchers() {
        Log.d("VoucherActivity", "🎯 [UI] loadVouchers called");

        // Check if view is still available
        if (binding == null || getWindow() == null) {
            Log.w("VoucherActivity", "⚠️ [UI] View not available, skipping loadVouchers");
            return;
        }

        Log.d("VoucherActivity", "🚀 [UI] Starting API call to get all global discounts");
        binding.progressBar.setVisibility(View.VISIBLE);

        discountService.getAllDiscounts(result -> {
            Log.d("VoucherActivity", "📨 [UI] Received API callback - success: " + result.isSuccess());

            // Check if view is still available in callback
            if (binding == null || getWindow() == null) {
                Log.w("VoucherActivity", "⚠️ [UI] View destroyed, ignoring callback");
                return;
            }

            binding.progressBar.setVisibility(View.GONE);

            if (result.isSuccess()) {
                List<DiscountDto> vouchers = result.getData();
                Log.d("VoucherActivity", "✅ [UI] Successfully loaded " + (vouchers != null ? vouchers.size() : 0) + " global discounts");
                if (vouchers != null && voucherAdapter != null) {
                    voucherAdapter.setVouchers(vouchers);
                } else if (voucherAdapter != null) {
                    voucherAdapter.setVouchers(new ArrayList<>());
                }
            } else {
                String errorMsg = result.getError() != null ? result.getError() : "Không thể tải mã giảm giá";
                Log.e("VoucherActivity", "❌ [UI] Failed to load discounts: " + errorMsg);
                Toast.makeText(VoucherActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
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
