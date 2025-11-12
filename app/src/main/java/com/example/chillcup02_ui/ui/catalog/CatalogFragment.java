package com.example.chillcup02_ui.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chillcup02_ui.auth.LoginActivity;
import com.example.chillcup02_ui.data.api.LoyaltyService;
import com.example.chillcup02_ui.data.api.MockUserService;
import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.databinding.FragmentCatalogBinding;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.example.chillcup02_ui.util.Result;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private FragmentCatalogBinding binding;
    private AuthViewModel authViewModel;
    private CatalogViewModel catalogViewModel;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private LoyaltyService loyaltyService;
    private MockUserService mockUserService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCatalogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        catalogViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        loyaltyService = LoyaltyService.getInstance();
        mockUserService = MockUserService.getInstance();

        setupUI();
        setupRecyclerViews();
        setupSearchAndFilter();
        setupObservers();

        // Load categories and products
        catalogViewModel.loadCategories();
        catalogViewModel.loadProducts();

        // Observe auth state to show/hide login button and membership card
        authViewModel.isLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                binding.cardWelcome.setVisibility(View.GONE);
                binding.cardMembership.setVisibility(View.VISIBLE);
                String displayName = authViewModel.getUserDisplayName();
                if (displayName != null && !displayName.isEmpty()) {
                    binding.tvWelcome.setText("Chào " + displayName + " 👋");
                } else {
                    binding.tvWelcome.setText("Xin chào bạn 👋");
                }
                loadMembershipInfo();
            } else {
                binding.cardWelcome.setVisibility(View.VISIBLE);
                binding.cardMembership.setVisibility(View.GONE);
                binding.tvWelcome.setText("Chào bạn mới 👋");
            }
        });

        // Login button click
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
        
        // Voucher button click
        binding.btnVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), com.example.chillcup02_ui.ui.voucher.VoucherActivity.class);
            startActivity(intent);
        });
    }

    private void setupUI() {
        if (authViewModel.isUserLoggedIn()) {
            binding.cardWelcome.setVisibility(View.GONE);
            binding.cardMembership.setVisibility(View.VISIBLE);
            String displayName = authViewModel.getUserDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                binding.tvWelcome.setText("Chào " + displayName + " 👋");
            } else {
                binding.tvWelcome.setText("Xin chào bạn 👋");
            }
            loadMembershipInfo();
        } else {
            binding.cardWelcome.setVisibility(View.VISIBLE);
            binding.cardMembership.setVisibility(View.GONE);
            binding.tvWelcome.setText("Chào bạn mới 👋");
        }
    }
    
    private void loadMembershipInfo() {
        String userId = authViewModel.getUserId();
        if (userId == null) {
            // Try to get from email
            String email = authViewModel.getUserEmail();
            if (email != null) {
                UserDto user = mockUserService.getUserByEmail(email);
                if (user != null) {
                    userId = user.getId();
                }
            }
        }

        if (userId == null) {
            // Fallback: use mock user from AuthViewModel
            UserDto mockUser = authViewModel.getMockUser();
            if (mockUser != null) {
                userId = mockUser.getId();
                displayMembershipInfo(mockUser, 0);
            }
            return;
        }

        // Store userId as final for use in lambda
        final String finalUserId = userId;

        // Check if view is still available before making async calls
        if (binding == null || getView() == null) {
            return;
        }

        // Load user info
        mockUserService.getCurrentUser(finalUserId, userResult -> {
            // Check if view is still available in callback
            if (binding == null || getView() == null) {
                return;
            }

            final UserDto user = userResult.isSuccess() ? userResult.getData() : null;

            // Load loyalty points from API
            loyaltyService.getMyPoints(pointsResult -> {
                // Check if view is still available in nested callback
                if (binding == null || getView() == null) {
                    return;
                }

                Integer totalPoints = 0;
                if (pointsResult.isSuccess() && pointsResult.getData() != null) {
                    totalPoints = pointsResult.getData().get("totalPoints");
                    if (totalPoints == null) {
                        totalPoints = 0;
                    }
                }
                displayMembershipInfo(user, totalPoints);
            });
        });
    }
    
    private void displayMembershipInfo(UserDto user, Integer totalPoints) {
        // Check if view is still available
        if (binding == null || getView() == null) {
            return;
        }

        if (user != null) {
            // Display member ID (staffId)
            if (user.getStaffId() != null && !user.getStaffId().isEmpty()) {
                binding.tvMemberId.setText("Mã thành viên: " + user.getStaffId());
            } else {
                binding.tvMemberId.setText("Mã thành viên: -");
            }

            // Determine member type based on points
            if (totalPoints != null && totalPoints > 0) {
                binding.tvMemberType.setText("Thành viên");
            } else {
                binding.tvMemberType.setText("Thành viên mới");
            }
        } else {
            binding.tvMemberId.setText("Mã thành viên: -");
            binding.tvMemberType.setText("Thành viên mới");
        }

        // Display points
        if (totalPoints != null) {
            binding.tvPoints.setText(totalPoints + " PI");
        } else {
            binding.tvPoints.setText("0 PI");
        }
    }

    private void setupRecyclerViews() {
        // Setup categories RecyclerView
        categoryAdapter = new CategoryAdapter();
        binding.rvCategories.setLayoutManager(
            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvCategories.setAdapter(categoryAdapter);

        categoryAdapter.setOnCategoryClickListener((category, position) -> {
            categoryAdapter.setSelectedPosition(position);
            // Filter products by selected category
            if (category != null) {
                catalogViewModel.selectCategory(category.getId());
            } else {
                catalogViewModel.showAllProducts();
            }
        });

        // Setup products RecyclerView
        productAdapter = new ProductAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        binding.rvProducts.setLayoutManager(gridLayoutManager);
        binding.rvProducts.setAdapter(productAdapter);

        productAdapter.setOnProductClickListener(product -> {
            // Navigate to product detail
            Intent intent = new Intent(requireContext(), com.example.chillcup02_ui.ui.productdetail.ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
    }

    private void setupSearchAndFilter() {
        // Search functionality
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                catalogViewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Price filter functionality
        binding.spinnerPriceFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catalogViewModel.setPriceFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupObservers() {
        // Observe categories
        catalogViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryAdapter.setCategories(categories);
            }
        });

        // Observe products
        catalogViewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            android.util.Log.d("CatalogFragment", "Received products: " + (products != null ? products.size() : "null"));
            if (products != null && !products.isEmpty()) {
                productAdapter.setProducts(products);
                binding.tvProductsTitle.setText("Sản phẩm (" + products.size() + ")");
            } else {
                productAdapter.setProducts(new ArrayList<>()); // Clear the list
                binding.tvProductsTitle.setText("Sản phẩm (0)");
            }
        });

        // Observe loading states
        catalogViewModel.getLoadingCategories().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }
        });

        catalogViewModel.getLoadingProducts().observe(getViewLifecycleOwner(), isLoading -> {
            if (!isLoading) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        // Observe errors
        catalogViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.tvError.setText(error);
                binding.tvError.setVisibility(View.VISIBLE);
            } else {
                binding.tvError.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
