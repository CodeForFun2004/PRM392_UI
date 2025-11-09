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

import com.example.chillcup02_ui.auth.LoginActivity;
import com.example.chillcup02_ui.databinding.FragmentCatalogBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class CatalogFragment extends Fragment {
    
    private FragmentCatalogBinding binding;
    private AuthViewModel authViewModel;
    
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
        
        setupUI();
        
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
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
