package com.example.chillcup02_ui.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.auth.LoginActivity;
import com.example.chillcup02_ui.databinding.FragmentOrdersBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class OrdersFragment extends Fragment {
    
    private FragmentOrdersBinding binding;
    private AuthViewModel authViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        
        // Observe auth state
        authViewModel.isLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                showOrdersContent();
            } else {
                showLoginPrompt();
            }
        });
        
        // Check initial state
        if (authViewModel.getUser() != null) {
            showOrdersContent();
        } else {
            showLoginPrompt();
        }
    }
    
    private void showLoginPrompt() {
        binding.containerLoginPrompt.setVisibility(View.VISIBLE);
        binding.containerOrdersContent.setVisibility(View.GONE);
        
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
    
    private void showOrdersContent() {
        binding.containerLoginPrompt.setVisibility(View.GONE);
        binding.containerOrdersContent.setVisibility(View.VISIBLE);
        
        // Load orders here
        // For now, show empty state
        binding.tvNoOrders.setText(R.string.no_orders);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
