package com.example.chillcup02_ui.data.api;

import android.os.Handler;
import android.os.Looper;

import com.example.chillcup02_ui.data.dto.UpdateUserRequest;
import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.util.Result;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MockUserService {
    
    private static MockUserService instance;
    private final Map<String, UserDto> mockUsers = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    
    private MockUserService() {
        // Initialize with some mock users
        initializeMockUsers();
    }
    
    public static synchronized MockUserService getInstance() {
        if (instance == null) {
            instance = new MockUserService();
        }
        return instance;
    }
    
    private void initializeMockUsers() {
        // Mock user 1
        UserDto user1 = new UserDto();
        user1.setId("user_001");
        user1.setUsername("user1");
        user1.setFullname("Nguyễn Văn A");
        user1.setEmail("user1@test.com");
        user1.setPhone("0901234567");
        user1.setAvatar(null);
        user1.setAddress("123 Đường ABC, Quận 1, TP.HCM");
        user1.setRole("customer");
        user1.setStaffId("cus0001");
        user1.setIsBanned(false);
        user1.setCreatedAt(new Date(System.currentTimeMillis() - 86400000 * 30)); // 30 days ago
        user1.setUpdatedAt(new Date());
        mockUsers.put("user_001", user1);
        mockUsers.put("user1@test.com", user1);
        
        // Mock user 2
        UserDto user2 = new UserDto();
        user2.setId("user_002");
        user2.setUsername("testuser");
        user2.setFullname("Trần Thị B");
        user2.setEmail("test@test.com");
        user2.setPhone("0987654321");
        user2.setAvatar(null);
        user2.setAddress("456 Đường XYZ, Quận 2, TP.HCM");
        user2.setRole("customer");
        user2.setStaffId("cus0002");
        user2.setIsBanned(false);
        user2.setCreatedAt(new Date(System.currentTimeMillis() - 86400000 * 15)); // 15 days ago
        user2.setUpdatedAt(new Date());
        mockUsers.put("user_002", user2);
        mockUsers.put("test@test.com", user2);
    }
    
    /**
     * Get current user by ID or email
     * Simulates GET /api/users/me
     */
    public void getCurrentUser(String userId, UserCallback callback) {
        handler.postDelayed(() -> {
            UserDto user = mockUsers.get(userId);
            if (user == null) {
                // Try to find by email
                for (UserDto u : mockUsers.values()) {
                    if (u.getEmail() != null && u.getEmail().equals(userId)) {
                        user = u;
                        break;
                    }
                }
            }
            
            if (user == null) {
                callback.onResult(Result.error("User not found"));
                return;
            }
            
            // Return a copy to avoid external modifications
            UserDto userCopy = copyUser(user);
            callback.onResult(Result.success(userCopy));
        }, 500);
    }
    
    /**
     * Get user by ID
     * Simulates GET /api/users/:id
     */
    public void getUserById(String userId, UserCallback callback) {
        handler.postDelayed(() -> {
            UserDto user = mockUsers.get(userId);
            if (user == null) {
                callback.onResult(Result.error("User not found"));
                return;
            }
            
            UserDto userCopy = copyUser(user);
            callback.onResult(Result.success(userCopy));
        }, 500);
    }
    
    /**
     * Update user
     * Simulates PUT /api/users/:id
     */
    public void updateUser(String userId, UpdateUserRequest request, UserCallback callback) {
        handler.postDelayed(() -> {
            UserDto user = mockUsers.get(userId);
            
            // If not found by ID, try to find by email
            if (user == null) {
                for (UserDto u : mockUsers.values()) {
                    if (u.getId() != null && u.getId().equals(userId)) {
                        user = u;
                        break;
                    }
                    if (u.getEmail() != null && u.getEmail().equals(userId)) {
                        user = u;
                        break;
                    }
                }
            }
            
            if (user == null) {
                callback.onResult(Result.error("User not found"));
                return;
            }
            
            // Store old email for map update
            String oldEmail = user.getEmail();
            
            // Update fields
            if (request.getFullname() != null) {
                user.setFullname(request.getFullname());
            }
            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
            }
            if (request.getAddress() != null) {
                user.setAddress(request.getAddress());
            }
            if (request.getAvatar() != null) {
                user.setAvatar(request.getAvatar());
            }
            
            user.setUpdatedAt(new Date());
            
            // Update in map - remove old email entry if email changed
            if (oldEmail != null && !oldEmail.equals(user.getEmail())) {
                mockUsers.remove(oldEmail);
            }
            
            // Update by ID
            mockUsers.put(user.getId(), user);
            
            // Update by new email
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                mockUsers.put(user.getEmail(), user);
            }
            
            UserDto userCopy = copyUser(user);
            callback.onResult(Result.success(userCopy));
        }, 800);
    }
    
    /**
     * Get user order history
     * Simulates GET /api/users/me/orders
     */
    public void getUserOrderHistory(String userId, OrderHistoryCallback callback) {
        handler.postDelayed(() -> {
            // Mock empty order history for now
            // This will be implemented when order functionality is added
            callback.onResult(Result.success(new java.util.ArrayList<>()));
        }, 500);
    }
    
    private UserDto copyUser(UserDto original) {
        UserDto copy = new UserDto();
        copy.setId(original.getId());
        copy.setUsername(original.getUsername());
        copy.setFullname(original.getFullname());
        copy.setEmail(original.getEmail());
        copy.setPhone(original.getPhone());
        copy.setAvatar(original.getAvatar());
        copy.setAddress(original.getAddress());
        copy.setRole(original.getRole());
        copy.setStaffId(original.getStaffId());
        copy.setStatus(original.getStatus());
        copy.setIsBanned(original.getIsBanned());
        copy.setBanReason(original.getBanReason());
        copy.setBanExpires(original.getBanExpires());
        copy.setGoogleId(original.getGoogleId());
        copy.setStoreId(original.getStoreId());
        copy.setCreatedAt(original.getCreatedAt());
        copy.setUpdatedAt(original.getUpdatedAt());
        return copy;
    }
    
    // Helper method to get user by email (for AuthViewModel integration)
    public UserDto getUserByEmail(String email) {
        for (UserDto user : mockUsers.values()) {
            if (user.getEmail() != null && user.getEmail().equals(email)) {
                return copyUser(user);
            }
        }
        return null;
    }
    
    /**
     * Add or update user in mock storage
     * Called from MockAuthService to sync users
     */
    public void addOrUpdateUser(UserDto user) {
        if (user == null || user.getId() == null) {
            return;
        }
        
        // Store by ID
        mockUsers.put(user.getId(), copyUser(user));
        
        // Also store by email for easy lookup
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            mockUsers.put(user.getEmail(), copyUser(user));
        }
    }
    
    public interface UserCallback {
        void onResult(Result<UserDto> result);
    }
    
    public interface OrderHistoryCallback {
        void onResult(Result<java.util.List<Object>> result);
    }
}

