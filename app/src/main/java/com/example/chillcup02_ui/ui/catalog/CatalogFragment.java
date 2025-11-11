package com.example.chillcup02_ui.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chillcup02_ui.auth.LoginActivity;
import com.example.chillcup02_ui.databinding.FragmentCatalogBinding;
import com.example.chillcup02_ui.domain.model.Category;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class CatalogFragment extends Fragment {

    private FragmentCatalogBinding binding;
    private AuthViewModel authViewModel;
    private CatalogViewModel catalogViewModel;
    private CategoryAdapter categoryAdapter;

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
        setupRecyclerView();
        setupObservers();

        // Load categories
        catalogViewModel.loadCategories();

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

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter();
        binding.rvCategories.setLayoutManager(
            new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvCategories.setAdapter(categoryAdapter);

        categoryAdapter.setOnCategoryClickListener((category, position) -> {
            categoryAdapter.setSelectedPosition(position);
            // TODO: Load products for selected category
        });
    }

    private void setupObservers() {
        // Observe categories
        catalogViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                categoryAdapter.setCategories(categories);
            }
        });

        // Observe loading state
        catalogViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
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
