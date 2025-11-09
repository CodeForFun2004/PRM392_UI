package com.example.chillcup02_ui.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.auth.LoginActivity;
import com.example.chillcup02_ui.databinding.FragmentProfileBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    
    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        
        // Observe auth state
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                showProfileContent(user);
            } else {
                showLoginPrompt();
            }
        });
        
        // Check initial state
        FirebaseUser user = authViewModel.getUser();
        if (user != null) {
            showProfileContent(user);
        } else {
            showLoginPrompt();
        }
        
        // Logout button
        binding.btnLogout.setOnClickListener(v -> {
            authViewModel.signOut();
            Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            showLoginPrompt();
        });
    }
    
    private void showLoginPrompt() {
        binding.containerLoginPrompt.setVisibility(View.VISIBLE);
        binding.containerProfileContent.setVisibility(View.GONE);
        
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
    
    private void showProfileContent(FirebaseUser user) {
        binding.containerLoginPrompt.setVisibility(View.GONE);
        binding.containerProfileContent.setVisibility(View.VISIBLE);
        
        // Populate user info
        if (user.getDisplayName() != null) {
            binding.tvUserName.setText(user.getDisplayName());
        } else {
            binding.tvUserName.setText("Người dùng");
        }
        
        if (user.getEmail() != null) {
            binding.tvEmail.setText(user.getEmail());
        } else {
            binding.tvEmail.setText("Chưa có email");
        }
        
        // Load profile picture from Google using Glide
        android.net.Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null && !photoUrl.toString().isEmpty()) {
            // Use Glide to load the image with circular transformation
            // Glide's circleCropTransform() will automatically make the image circular
            Glide.with(this)
                    .load(photoUrl)
                    .apply(RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile))
                    .into(binding.ivProfilePicture);
        } else {
            // If no photo URL, show default profile icon
            Glide.with(this).clear(binding.ivProfilePicture);
            binding.ivProfilePicture.setImageResource(R.drawable.ic_profile);
            // Set a circular background for the placeholder icon
            binding.ivProfilePicture.setBackgroundResource(R.drawable.circle_background);
        }
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

