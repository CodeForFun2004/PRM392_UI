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
import com.example.chillcup02_ui.data.api.MockUserService;
import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.databinding.FragmentProfileBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.example.chillcup02_ui.util.Result;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    
    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;
    private MockUserService mockUserService;
    private UserDto currentUser;
    
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
        mockUserService = MockUserService.getInstance();
        
        // Observe auth state
        authViewModel.isLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                loadUserProfile();
            } else {
                showLoginPrompt();
            }
        });
        
        // Check initial state
        if (authViewModel.isUserLoggedIn()) {
            loadUserProfile();
        } else {
            showLoginPrompt();
        }
        
        setupClickListeners();
    }
    
    private void setupClickListeners() {
        binding.btnLogout.setOnClickListener(v -> {
            authViewModel.signOut();
            Toast.makeText(requireContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            showLoginPrompt();
        });
        
        binding.btnEditProfile.setOnClickListener(v -> {
            if (currentUser != null) {
                showEditProfileDialog();
            }
        });
        
        binding.btnFavourites.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Tính năng sản phẩm yêu thích sẽ được phát triển sau", Toast.LENGTH_SHORT).show();
        });

        binding.btnOrderHistory.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Tính năng lịch sử đơn hàng sẽ được phát triển sau", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadUserProfile() {
        binding.containerLoginPrompt.setVisibility(View.GONE);
        binding.containerProfileContent.setVisibility(View.VISIBLE);
        
        // Get user from AuthViewModel first
        UserDto mockUser = authViewModel.getMockUser();
        if (mockUser != null) {
            // User is logged in via Mock Auth, use it directly
            currentUser = mockUser;
            displayUserInfo(mockUser);
            return;
        }
        
        // Try to get from MockUserService
        String userId = authViewModel.getUserId();
        String email = authViewModel.getUserEmail();
        
        if (userId == null && email == null) {
            // Fallback: use basic info from AuthViewModel (Firebase user)
            displayBasicUserInfo();
            return;
        }
        
        // Load user profile from MockUserService
        String identifier = userId != null ? userId : email;
        mockUserService.getCurrentUser(identifier, result -> {
            if (result.isSuccess()) {
                currentUser = result.getData();
                displayUserInfo(currentUser);
            } else {
                // Fallback: use basic info from AuthViewModel
                displayBasicUserInfo();
            }
        });
    }
    
    private void displayUserInfo(UserDto user) {
        currentUser = user;
        
        // Profile picture
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Glide.with(this)
                    .load(user.getAvatar())
                    .apply(RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile))
                    .into(binding.ivProfilePicture);
        } else {
            Glide.with(this).clear(binding.ivProfilePicture);
            binding.ivProfilePicture.setImageResource(R.drawable.ic_profile);
            binding.ivProfilePicture.setBackgroundResource(R.drawable.circle_background);
        }
        
        // User name
        if (user.getFullname() != null && !user.getFullname().isEmpty()) {
            binding.tvUserName.setText(user.getFullname());
        } else {
            binding.tvUserName.setText("Người dùng");
        }
        
        // Username
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            binding.tvUsername.setText(user.getUsername());
        } else {
            binding.tvUsername.setText("N/A");
        }
        
        // Email
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            binding.tvEmail.setText(user.getEmail());
        } else {
            binding.tvEmail.setText("Chưa cập nhật");
        }
        
        // Phone
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            binding.tvPhone.setText(user.getPhone());
        } else {
            binding.tvPhone.setText("Chưa cập nhật");
        }
        
        // Address
        if (user.getAddress() != null && !user.getAddress().isEmpty()) {
            binding.tvAddress.setText(user.getAddress());
        } else {
            binding.tvAddress.setText("Chưa cập nhật");
        }
    }
    
    private void displayBasicUserInfo() {
        // Fallback: use info from AuthViewModel
        String displayName = authViewModel.getUserDisplayName();
        String email = authViewModel.getUserEmail();
        String avatarUrl = authViewModel.getUserAvatar();
        
        if (displayName != null) {
            binding.tvUserName.setText(displayName);
        } else {
            binding.tvUserName.setText("Người dùng");
        }
        
        if (email != null) {
            binding.tvEmail.setText(email);
        } else {
            binding.tvEmail.setText("Chưa cập nhật");
        }
        
        binding.tvUsername.setText("N/A");
        binding.tvPhone.setText("Chưa cập nhật");
        binding.tvAddress.setText("Chưa cập nhật");
        
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .apply(RequestOptions.circleCropTransform()
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile))
                    .into(binding.ivProfilePicture);
        } else {
            Glide.with(this).clear(binding.ivProfilePicture);
            binding.ivProfilePicture.setImageResource(R.drawable.ic_profile);
            binding.ivProfilePicture.setBackgroundResource(R.drawable.circle_background);
        }
    }
    
    private void showLoginPrompt() {
        binding.containerLoginPrompt.setVisibility(View.VISIBLE);
        binding.containerProfileContent.setVisibility(View.GONE);
    }
    
    private void showEditProfileDialog() {
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Không thể tải thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }
        
        EditProfileDialog dialog = EditProfileDialog.newInstance(currentUser);
        dialog.setListener(new EditProfileDialog.EditProfileListener() {
            @Override
            public void onProfileUpdated(UserDto updatedUser) {
                currentUser = updatedUser;
                
                // Update AuthViewModel to keep data in sync
                authViewModel.updateMockUser(updatedUser);
                
                // Update UI
                displayUserInfo(updatedUser);
                Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getParentFragmentManager(), "EditProfileDialog");
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
