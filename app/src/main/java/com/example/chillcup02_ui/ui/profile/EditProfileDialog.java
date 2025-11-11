package com.example.chillcup02_ui.ui.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.chillcup02_ui.data.api.MockUserService;
import com.example.chillcup02_ui.data.dto.UpdateUserRequest;
import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.databinding.DialogEditProfileBinding;
import com.example.chillcup02_ui.util.Result;

public class EditProfileDialog extends DialogFragment {
    
    private DialogEditProfileBinding binding;
    private UserDto user;
    private EditProfileListener listener;
    
    public static EditProfileDialog newInstance(UserDto user) {
        EditProfileDialog dialog = new EditProfileDialog();
        dialog.user = user;
        return dialog;
    }
    
    public void setListener(EditProfileListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        if (user == null) {
            dismiss();
            return;
        }
        
        // Populate fields
        binding.etFullname.setText(user.getFullname() != null ? user.getFullname() : "");
        binding.etEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        binding.etPhone.setText(user.getPhone() != null ? user.getPhone() : "");
        binding.etAddress.setText(user.getAddress() != null ? user.getAddress() : "");
        
        // Setup click listeners
        binding.btnCancel.setOnClickListener(v -> dismiss());
        
        binding.btnSave.setOnClickListener(v -> saveProfile());
    }
    
    private void saveProfile() {
        String fullname = binding.etFullname.getText() != null 
                ? binding.etFullname.getText().toString().trim() 
                : "";
        String email = binding.etEmail.getText() != null 
                ? binding.etEmail.getText().toString().trim() 
                : "";
        String phone = binding.etPhone.getText() != null 
                ? binding.etPhone.getText().toString().trim() 
                : "";
        String address = binding.etAddress.getText() != null 
                ? binding.etAddress.getText().toString().trim() 
                : "";
        
        // Validation
        if (TextUtils.isEmpty(fullname)) {
            binding.tilFullname.setError("Vui lòng nhập họ tên");
            return;
        }
        
        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setError("Vui lòng nhập email");
            return;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setError("Email không hợp lệ");
            return;
        }
        
        binding.tilFullname.setError(null);
        binding.tilEmail.setError(null);
        
        // Create update request
        UpdateUserRequest request = new UpdateUserRequest();
        request.setFullname(fullname);
        request.setEmail(email);
        request.setPhone(phone.isEmpty() ? null : phone);
        request.setAddress(address.isEmpty() ? null : address);
        request.setAvatar(null); // Avatar upload will be implemented later
        
        // Show loading
        binding.btnSave.setEnabled(false);
        binding.btnCancel.setEnabled(false);
        
        // Update user
        MockUserService mockUserService = MockUserService.getInstance();
        mockUserService.updateUser(user.getId(), request, result -> {
            binding.btnSave.setEnabled(true);
            binding.btnCancel.setEnabled(true);
            
            if (result.isSuccess()) {
                UserDto updatedUser = result.getData();
                if (listener != null) {
                    listener.onProfileUpdated(updatedUser);
                }
                dismiss();
            } else {
                Toast.makeText(requireContext(), 
                        result.getError() != null ? result.getError() : "Cập nhật thất bại", 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
    
    public interface EditProfileListener {
        void onProfileUpdated(UserDto updatedUser);
    }
}

