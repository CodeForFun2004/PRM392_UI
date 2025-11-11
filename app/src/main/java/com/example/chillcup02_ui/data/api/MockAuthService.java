package com.example.chillcup02_ui.data.api;

import android.os.Handler;
import android.os.Looper;

import com.example.chillcup02_ui.data.dto.AuthResponse;
import com.example.chillcup02_ui.data.dto.LoginRequest;
import com.example.chillcup02_ui.data.dto.RegisterRequest;
import com.example.chillcup02_ui.data.dto.RegisterResponse;
import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.data.dto.VerifyOtpRequest;
import com.example.chillcup02_ui.util.Result;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MockAuthService {
    
    private static MockAuthService instance;
    private final Map<String, MockUser> mockUsers = new HashMap<>();
    private final Map<String, PendingRegistration> pendingRegistrations = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    
    private MockAuthService() {
        // Tạo một số user mẫu để test
        MockUser user1 = new MockUser("user1", "user1@test.com", "User One", "password123");
        user1.id = "user_001";
        mockUsers.put("user1@test.com", user1);
        mockUsers.put("user1", user1);
        mockUsers.put("user_001", user1);
        
        MockUser testUser = new MockUser("testuser", "test@test.com", "Test User", "test123");
        testUser.id = "user_002";
        mockUsers.put("test@test.com", testUser);
        mockUsers.put("testuser", testUser);
        mockUsers.put("user_002", testUser);
    }
    
    public static synchronized MockAuthService getInstance() {
        if (instance == null) {
            instance = new MockAuthService();
        }
        return instance;
    }
    
    public void login(LoginRequest request, AuthCallback callback) {
        // Simulate network delay
        handler.postDelayed(() -> {
            String usernameOrEmail = request.getUsernameOrEmail();
            String password = request.getPassword();
            
            if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
                callback.onResult(Result.error("Vui lòng nhập email hoặc username"));
                return;
            }
            
            if (password == null || password.trim().isEmpty()) {
                callback.onResult(Result.error("Vui lòng nhập mật khẩu"));
                return;
            }
            
            MockUser user = mockUsers.get(usernameOrEmail.toLowerCase());
            
            if (user == null) {
                callback.onResult(Result.error("Tài khoản không tồn tại"));
                return;
            }
            
            if (!user.password.equals(password)) {
                callback.onResult(Result.error("Sai mật khẩu"));
                return;
            }
            
            if (user.isBanned) {
                callback.onResult(Result.error("Tài khoản của bạn đã bị khóa"));
                return;
            }
            
            // Tạo mock tokens
            String accessToken = "mock_access_token_" + System.currentTimeMillis();
            String refreshToken = "mock_refresh_token_" + System.currentTimeMillis();
            
            UserDto userDto = new UserDto(
                user.id,
                user.username,
                user.fullname,
                user.email,
                user.phone,
                user.avatar,
                user.address,
                user.role
            );
            userDto.setIsBanned(user.isBanned);
            userDto.setCreatedAt(new java.util.Date());
            userDto.setUpdatedAt(new java.util.Date());
            
            // Sync with MockUserService
            syncUserToMockUserService(userDto);
            
            AuthResponse response = new AuthResponse(userDto, accessToken, refreshToken);
            callback.onResult(Result.success(response));
        }, 1000); // 1 second delay
    }
    
    public void register(RegisterRequest request, RegisterCallback callback) {
        handler.postDelayed(() -> {
            // Validate
            if (request.getFullname() == null || request.getFullname().trim().isEmpty()) {
                callback.onResult(Result.error("Vui lòng nhập họ tên"));
                return;
            }
            
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                callback.onResult(Result.error("Vui lòng nhập username"));
                return;
            }
            
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                callback.onResult(Result.error("Vui lòng nhập email"));
                return;
            }
            
            if (!isValidEmail(request.getEmail())) {
                callback.onResult(Result.error("Email không hợp lệ"));
                return;
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                callback.onResult(Result.error("Vui lòng nhập mật khẩu"));
                return;
            }
            
            if (request.getPassword().length() < 6) {
                callback.onResult(Result.error("Mật khẩu phải có ít nhất 6 ký tự"));
                return;
            }
            
            // Check if user already exists
            String emailLower = request.getEmail().toLowerCase();
            String usernameLower = request.getUsername().toLowerCase();
            
            for (MockUser user : mockUsers.values()) {
                if (user.email.toLowerCase().equals(emailLower) || 
                    user.username.toLowerCase().equals(usernameLower)) {
                    callback.onResult(Result.error("Email hoặc username đã được sử dụng"));
                    return;
                }
            }
            
            // Generate OTP
            String otp = generateOTP();
            long expiresAt = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minutes
            
            pendingRegistrations.put(emailLower, new PendingRegistration(
                request.getFullname(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                otp,
                expiresAt
            ));
            
            // In production, OTP would be sent via email
            // For mock, we'll just return success
            RegisterResponse response = new RegisterResponse("Mã OTP đã được gửi đến email của bạn. OTP: " + otp);
            callback.onResult(Result.success(response));
        }, 1000);
    }
    
    public void verifyOtp(VerifyOtpRequest request, AuthCallback callback) {
        handler.postDelayed(() -> {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                callback.onResult(Result.error("Email không được để trống"));
                return;
            }
            
            if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
                callback.onResult(Result.error("Vui lòng nhập mã OTP"));
                return;
            }
            
            String emailLower = request.getEmail().toLowerCase();
            PendingRegistration pending = pendingRegistrations.get(emailLower);
            
            if (pending == null) {
                callback.onResult(Result.error("Không tìm thấy yêu cầu đăng ký"));
                return;
            }
            
            if (System.currentTimeMillis() > pending.expiresAt) {
                pendingRegistrations.remove(emailLower);
                callback.onResult(Result.error("Mã OTP đã hết hạn"));
                return;
            }
            
            if (!pending.otp.equals(request.getOtp())) {
                callback.onResult(Result.error("Mã OTP không đúng"));
                return;
            }
            
            // Create user
            String userId = "user_" + System.currentTimeMillis();
            MockUser newUser = new MockUser(
                pending.username,
                pending.email,
                pending.fullname,
                pending.password
            );
            newUser.id = userId;
            
            // Store by multiple keys for easy lookup
            mockUsers.put(pending.email.toLowerCase(), newUser);
            mockUsers.put(pending.username.toLowerCase(), newUser);
            mockUsers.put(userId, newUser); // Store by ID too
            
            // Remove pending registration
            pendingRegistrations.remove(emailLower);
            
            // Generate tokens
            String accessToken = "mock_access_token_" + System.currentTimeMillis();
            String refreshToken = "mock_refresh_token_" + System.currentTimeMillis();
            
            UserDto userDto = new UserDto(
                userId,
                pending.username,
                pending.fullname,
                pending.email,
                null,
                null,
                null,
                "customer"
            );
            userDto.setIsBanned(false);
            userDto.setCreatedAt(new java.util.Date());
            userDto.setUpdatedAt(new java.util.Date());
            
            // Sync with MockUserService
            syncUserToMockUserService(userDto);
            
            AuthResponse response = new AuthResponse(userDto, accessToken, refreshToken);
            callback.onResult(Result.success(response));
        }, 1000);
    }
    
    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    private void syncUserToMockUserService(UserDto userDto) {
        // Sync user to MockUserService so ProfileFragment can access it
        try {
            MockUserService mockUserService = MockUserService.getInstance();
            mockUserService.addOrUpdateUser(userDto);
        } catch (Exception e) {
            // Silently fail - MockUserService will create user on demand
        }
    }
    
    public interface AuthCallback {
        void onResult(Result<AuthResponse> result);
    }
    
    public interface RegisterCallback {
        void onResult(Result<RegisterResponse> result);
    }
    
    private static class MockUser {
        String id;
        String username;
        String email;
        String fullname;
        String password;
        String phone;
        String avatar;
        String address;
        String role = "customer";
        boolean isBanned = false;
        
        MockUser(String username, String email, String fullname, String password) {
            this.username = username;
            this.email = email;
            this.fullname = fullname;
            this.password = password;
        }
    }
    
    private static class PendingRegistration {
        String fullname;
        String username;
        String email;
        String password;
        String otp;
        long expiresAt;
        
        PendingRegistration(String fullname, String username, String email, String password, String otp, long expiresAt) {
            this.fullname = fullname;
            this.username = username;
            this.email = email;
            this.password = password;
            this.otp = otp;
            this.expiresAt = expiresAt;
        }
    }
}


