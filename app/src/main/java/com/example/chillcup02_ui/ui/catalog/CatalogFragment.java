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
import com.example.chillcup02_ui.databinding.FragmentCatalogBinding;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private FragmentCatalogBinding binding;
    private AuthViewModel authViewModel;
    private CatalogViewModel catalogViewModel;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

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

        setupUI();
        setupRecyclerViews();
        setupSearchAndFilter();
        setupObservers();

        // Load categories and products
        catalogViewModel.loadCategories();
        catalogViewModel.loadProducts();

        // Observe auth state to show/hide login button
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.cardWelcome.setVisibility(View.GONE);
            } else {
                binding.cardWelcome.setVisibility(View.VISIBLE);
            }
        });

        // Login button click
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void setupUI() {
        FirebaseUser user = authViewModel.getUser();
        if (user != null) {
            binding.cardWelcome.setVisibility(View.GONE);
        } else {
            binding.cardWelcome.setVisibility(View.VISIBLE);
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
