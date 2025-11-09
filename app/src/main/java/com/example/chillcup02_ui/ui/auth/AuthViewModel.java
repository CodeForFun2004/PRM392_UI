package com.example.chillcup02_ui.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    
    private final MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private final FirebaseAuth mAuth;
    
    public AuthViewModel() {
        mAuth = FirebaseAuth.getInstance();
        checkAuthState();
        
        // Listen for auth state changes
        mAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            currentUser.setValue(user);
            isLoggedIn.setValue(user != null);
        });
    }
    
    private void checkAuthState() {
        FirebaseUser user = mAuth.getCurrentUser();
        currentUser.setValue(user);
        isLoggedIn.setValue(user != null);
    }
    
    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }
    
    public LiveData<Boolean> isLoggedIn() {
        return isLoggedIn;
    }
    
    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }
    
    public void signOut() {
        mAuth.signOut();
    }
}
