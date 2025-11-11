package com.example.chillcup02_ui.ui.auth;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.data.dto.UserDto;
import com.example.chillcup02_ui.data.local.AuthPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    
    private final MutableLiveData<FirebaseUser> currentFirebaseUser = new MutableLiveData<>();
    private final MutableLiveData<UserDto> currentMockUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private final FirebaseAuth mAuth;
    
    // Store tokens for mock auth
    private String accessToken;
    private String refreshToken;
    private AuthPreferences authPreferences;
    
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
