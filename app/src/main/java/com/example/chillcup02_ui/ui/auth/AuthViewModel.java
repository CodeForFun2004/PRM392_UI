package com.example.chillcup02_ui.ui.auth;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.data.api.AuthService;
import com.example.chillcup02_ui.data.dto.AuthResponse;
import com.example.chillcup02_ui.data.dto.LoginRequest;
import com.example.chillcup02_ui.data.dto.RegisterRequest;
import com.example.chillcup02_ui.data.dto.RegisterResponse;
import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.data.dto.VerifyOtpRequest;
import com.example.chillcup02_ui.data.local.AuthPreferences;
import com.example.chillcup02_ui.util.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class AuthViewModel extends ViewModel {
    
    private final MutableLiveData<FirebaseUser> currentFirebaseUser = new MutableLiveData<>();
    private final MutableLiveData<UserDto> currentMockUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private final FirebaseAuth mAuth;
    
    // Store tokens for mock auth
    private String accessToken;
    private String refreshToken;
    private AuthPreferences authPreferences;
    private AuthService authService;
    
    public AuthViewModel() {
        mAuth = FirebaseAuth.getInstance();
        checkAuthState();
        
        // Listen for auth state changes (Firebase)
        mAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            currentFirebaseUser.setValue(user);
            updateLoginState();
        });
    }
    
    public void init(Context context) {
        if (authPreferences == null) {
            authPreferences = new AuthPreferences(context);
            // Restore mock user from preferences
            restoreMockUser();
        }
    }
    
    private void restoreMockUser() {
        if (authPreferences != null && authPreferences.hasMockUser()) {
            UserDto user = authPreferences.getMockUser();
            if (user != null) {
                currentMockUser.setValue(user);
                accessToken = authPreferences.getAccessToken();
                refreshToken = authPreferences.getRefreshToken();
                updateLoginState();
            }
        }
    }
    
    private void checkAuthState() {
        FirebaseUser user = mAuth.getCurrentUser();
        currentFirebaseUser.setValue(user);
        updateLoginState();
    }
    
    private void updateLoginState() {
        boolean loggedIn = currentFirebaseUser.getValue() != null || currentMockUser.getValue() != null;
        isLoggedIn.setValue(loggedIn);
    }
    
    // Firebase Auth methods
    public LiveData<FirebaseUser> getCurrentFirebaseUser() {
        return currentFirebaseUser;
    }
    
    // Backward compatibility: returns FirebaseUser LiveData (for existing code)
    public LiveData<FirebaseUser> getCurrentUser() {
        return currentFirebaseUser;
    }
    
    public FirebaseUser getFirebaseUser() {
        return mAuth.getCurrentUser();
    }
    
    // Backward compatibility: returns FirebaseUser (for existing code)
    // Note: This will return null if user logged in via Mock Auth
    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }
    
    // Mock Auth methods
    public void setMockUser(UserDto user, String accessToken, String refreshToken) {
        this.currentMockUser.setValue(user);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        
        // Save to preferences for persistence
        if (authPreferences != null) {
            authPreferences.saveMockUser(user, accessToken, refreshToken);
        }
        
        updateLoginState();
    }
    
    public LiveData<UserDto> getCurrentMockUser() {
        return currentMockUser;
    }
    
    public UserDto getMockUser() {
        return currentMockUser.getValue();
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    // Combined login state
    public LiveData<Boolean> isLoggedIn() {
        return isLoggedIn;
    }
    
    public boolean isUserLoggedIn() {
        return currentFirebaseUser.getValue() != null || currentMockUser.getValue() != null;
    }
    
    // Get user info (prefer mock user if available, otherwise Firebase user)
    public String getUserDisplayName() {
        UserDto mockUser = currentMockUser.getValue();
        if (mockUser != null && mockUser.getFullname() != null) {
            return mockUser.getFullname();
        }
        
        FirebaseUser firebaseUser = currentFirebaseUser.getValue();
        if (firebaseUser != null && firebaseUser.getDisplayName() != null) {
            return firebaseUser.getDisplayName();
        }
        
        return null;
    }
    
    public String getUserEmail() {
        UserDto mockUser = currentMockUser.getValue();
        if (mockUser != null && mockUser.getEmail() != null) {
            return mockUser.getEmail();
        }
        
        FirebaseUser firebaseUser = currentFirebaseUser.getValue();
        if (firebaseUser != null && firebaseUser.getEmail() != null) {
            return firebaseUser.getEmail();
        }
        
        return null;
    }
    
    public String getUserAvatar() {
        UserDto mockUser = currentMockUser.getValue();
        if (mockUser != null && mockUser.getAvatar() != null) {
            return mockUser.getAvatar();
        }
        
        FirebaseUser firebaseUser = currentFirebaseUser.getValue();
        if (firebaseUser != null && firebaseUser.getPhotoUrl() != null) {
            return firebaseUser.getPhotoUrl().toString();
        }
        
        return null;
    }
    
    public String getUserId() {
        UserDto mockUser = currentMockUser.getValue();
        if (mockUser != null && mockUser.getId() != null) {
            return mockUser.getId();
        }
        
        FirebaseUser firebaseUser = currentFirebaseUser.getValue();
        if (firebaseUser != null && firebaseUser.getUid() != null) {
            return firebaseUser.getUid();
        }
        
        return null;
    }
    
    /**
     * Update mock user data (after profile update)
     */
    public void updateMockUser(UserDto updatedUser) {
        if (updatedUser != null) {
            this.currentMockUser.setValue(updatedUser);

            // Save to preferences for persistence
            if (authPreferences != null) {
                authPreferences.saveMockUser(updatedUser, accessToken, refreshToken);
            }
        }
    }

    // API Authentication methods
    public void loginWithApi(LoginRequest request, AuthService.ResultCallback<AuthResponse> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.login(request, result -> {
            if (result.isSuccess()) {
                AuthResponse response = result.getData();
                setMockUser(response.getUser(), response.getAccessToken(), response.getRefreshToken());
            }
            callback.onResult(result);
        });
    }

    public void registerWithApi(RegisterRequest request, AuthService.ResultCallback<RegisterResponse> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.registerRequest(request, callback);
    }

    public void verifyOtpWithApi(VerifyOtpRequest request, AuthService.ResultCallback<AuthResponse> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.verifyRegister(request, result -> {
            if (result.isSuccess()) {
                AuthResponse response = result.getData();
                setMockUser(response.getUser(), response.getAccessToken(), response.getRefreshToken());
            }
            callback.onResult(result);
        });
    }

    public void refreshTokenWithApi(AuthService.ResultCallback<Map<String, String>> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        if (refreshToken != null) {
            authService.refreshToken(refreshToken, result -> {
                if (result.isSuccess()) {
                    Map<String, String> response = result.getData();
                    String newAccessToken = response.get("accessToken");
                    if (newAccessToken != null) {
                        accessToken = newAccessToken;
                        // Save updated token to preferences
                        if (authPreferences != null && currentMockUser.getValue() != null) {
                            authPreferences.saveMockUser(currentMockUser.getValue(), accessToken, refreshToken);
                        }
                    }
                }
                callback.onResult(result);
            });
        } else {
            callback.onResult(Result.error("No refresh token available"));
        }
    }

    public void logoutWithApi(AuthService.ResultCallback<Map<String, String>> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.logout(result -> {
            // Always clear local data regardless of API response
            signOut();
            callback.onResult(result);
        });
    }

    // Firebase Auth methods (for new users - hybrid backend)
    public void firebaseRegisterWithApi(String email, String password, String fullname, AuthService.ResultCallback<Map<String, Object>> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.firebaseRegister(email, password, fullname, result -> {
            if (result.isSuccess()) {
                Map<String, Object> response = result.getData();
                String customToken = (String) response.get("customToken");

                if (customToken != null) {
                    // Sign in with Firebase using custom token
                    mAuth.signInWithCustomToken(customToken)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("AuthViewModel", "Firebase custom token sign in success");
                                // Extract user data from response
                                @SuppressWarnings("unchecked")
                                Map<String, Object> userData = (Map<String, Object>) response.get("user");
                                if (userData != null) {
                                    // Create UserDto from response
                                    UserDto user = createUserDtoFromFirebaseResponse(userData);
                                    setMockUser(user, customToken, null); // Firebase users don't use refresh tokens
                                }
                                callback.onResult(result);
                            } else {
                                Log.e("AuthViewModel", "Firebase custom token sign in failed", task.getException());
                                callback.onResult(Result.error("Đăng ký thành công nhưng đăng nhập Firebase thất bại"));
                            }
                        });
                } else {
                    callback.onResult(Result.error("Không nhận được custom token"));
                }
            } else {
                callback.onResult(result);
            }
        });
    }

    public void firebaseLoginWithApi(String email, String password, AuthService.ResultCallback<Map<String, Object>> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.firebaseLogin(email, password, result -> {
            if (result.isSuccess()) {
                Map<String, Object> response = result.getData();
                String customToken = (String) response.get("customToken");

                if (customToken != null) {
                    // Sign in with Firebase using custom token
                    mAuth.signInWithCustomToken(customToken)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("AuthViewModel", "Firebase custom token sign in success");
                                // Extract user data from response
                                @SuppressWarnings("unchecked")
                                Map<String, Object> userData = (Map<String, Object>) response.get("user");
                                if (userData != null) {
                                    UserDto user = createUserDtoFromFirebaseResponse(userData);
                                    setMockUser(user, customToken, null);
                                }
                                callback.onResult(result);
                            } else {
                                Log.e("AuthViewModel", "Firebase custom token sign in failed", task.getException());
                                callback.onResult(Result.error("Đăng nhập thành công nhưng xác thực Firebase thất bại"));
                            }
                        });
                } else {
                    callback.onResult(Result.error("Không nhận được custom token"));
                }
            } else {
                callback.onResult(result);
            }
        });
    }

    public void firebaseGoogleLoginWithApi(String idToken, AuthService.ResultCallback<Map<String, Object>> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.firebaseGoogleLogin(idToken, result -> {
            if (result.isSuccess()) {
                Map<String, Object> response = result.getData();
                // For Google login, we don't get a custom token, just user data
                // Firebase Auth state will be updated automatically
                @SuppressWarnings("unchecked")
                Map<String, Object> userData = (Map<String, Object>) response.get("user");
                if (userData != null) {
                    UserDto user = createUserDtoFromFirebaseResponse(userData);
                    setMockUser(user, idToken, null);
                }
                callback.onResult(result);
            } else {
                callback.onResult(result);
            }
        });
    }

    public void firebaseLogoutWithApi(AuthService.ResultCallback<Map<String, String>> callback) {
        if (authService == null) {
            authService = new AuthService();
        }
        authService.firebaseLogout(result -> {
            // Always clear local data regardless of API response
            signOut();
            callback.onResult(result);
        });
    }

    private UserDto createUserDtoFromFirebaseResponse(Map<String, Object> userData) {
        UserDto user = new UserDto();
        user.setId((String) userData.get("id"));
        user.setEmail((String) userData.get("email"));
        user.setFullname((String) userData.get("fullname"));
        user.setUsername((String) userData.get("username"));
        user.setAvatar((String) userData.get("avatar"));
        // Note: Firebase users don't have role/staffId in the same way, but we can set defaults
        return user;
    }

    public void signOut() {
        // Sign out from Firebase
        mAuth.signOut();
        
        // Clear mock user
        currentMockUser.setValue(null);
        accessToken = null;
        refreshToken = null;
        
        // Clear preferences
        if (authPreferences != null) {
            authPreferences.clear();
        }
        
        updateLoginState();
    }
}
