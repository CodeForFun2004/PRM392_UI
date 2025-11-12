package com.example.chillcup02_ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Centralized Firebase Authentication Manager
 * Handles Firebase Auth operations for new users (hybrid backend)
 */
public class FirebaseAuthManager {

    private static final String TAG = "FirebaseAuthManager";
    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private static FirebaseAuthManager instance;
    private final FirebaseAuth mAuth;
    private final MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
    private final MutableLiveData<String> idToken = new MutableLiveData<>();
    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuthManager() {
        mAuth = FirebaseAuth.getInstance();

        // Listen for auth state changes
        mAuth.addAuthStateListener(firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            currentUser.setValue(user);

            // Get fresh ID token when user changes
            if (user != null) {
                user.getIdToken(true).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult().getToken();
                        idToken.setValue(token);
                        Log.d(TAG, "Firebase ID token updated");
                    } else {
                        Log.e(TAG, "Failed to get ID token", task.getException());
                    }
                });
            } else {
                idToken.setValue(null);
            }
        });
    }

    public static synchronized FirebaseAuthManager getInstance() {
        if (instance == null) {
            instance = new FirebaseAuthManager();
        }
        return instance;
    }

    /**
     * Initialize Google Sign-In client
     */
    public void initGoogleSignIn(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(com.example.chillcup02_ui.R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
    }

    /**
     * Get current Firebase user
     */
    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    /**
     * Get current Firebase ID token
     */
    public LiveData<String> getIdToken() {
        return idToken;
    }

    /**
     * Get current Firebase ID token synchronously
     */
    public String getCurrentIdToken() {
        return idToken.getValue();
    }

    /**
     * Check if user is signed in with Firebase
     */
    public boolean isSignedIn() {
        return mAuth.getCurrentUser() != null;
    }

    /**
     * Sign in with email and password (for Firebase users)
     */
    public void signInWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    /**
     * Create user with email and password (for new Firebase users)
     */
    public void createUserWithEmailAndPassword(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    /**
     * Sign in with custom token (received from backend)
     */
    public void signInWithCustomToken(String customToken, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithCustomToken(customToken).addOnCompleteListener(listener);
    }

    /**
     * Start Google Sign-In flow
     */
    public Intent getGoogleSignInIntent() {
        if (mGoogleSignInClient == null) {
            throw new IllegalStateException("Google Sign-In client not initialized. Call initGoogleSignIn() first.");
        }
        return mGoogleSignInClient.getSignInIntent();
    }

    /**
     * Handle Google Sign-In result
     */
    public void handleGoogleSignInResult(Intent data, GoogleSignInResultCallback callback) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(TAG, "Google sign in success: " + account.getEmail());

            // Authenticate with Firebase using Google credential
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            Log.d(TAG, "Firebase auth with Google success");
                            callback.onSuccess(account, mAuth.getCurrentUser());
                        } else {
                            Log.e(TAG, "Firebase auth with Google failed", authTask.getException());
                            callback.onError(authTask.getException());
                        }
                    });

        } catch (ApiException e) {
            Log.e(TAG, "Google sign in failed", e);
            callback.onError(e);
        }
    }

    /**
     * Sign out from Firebase
     */
    public void signOut() {
        mAuth.signOut();
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
        }
    }

    /**
     * Force refresh ID token
     */
    public void refreshIdToken(OnCompleteListener<String> listener) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String token = task.getResult().getToken();
                    idToken.setValue(token);
                    listener.onComplete(com.google.android.gms.tasks.Tasks.forResult(token));
                } else {
                    listener.onComplete(com.google.android.gms.tasks.Tasks.forException(task.getException()));
                }
            });
        } else {
            listener.onComplete(com.google.android.gms.tasks.Tasks.forException(new Exception("No user signed in")));
        }
    }

    /**
     * Callback interface for Google Sign-In results
     */
    public interface GoogleSignInResultCallback {
        void onSuccess(GoogleSignInAccount googleAccount, FirebaseUser firebaseUser);
        void onError(Exception exception);
    }

    /**
     * Get request code for Google Sign-In
     */
    public static int getGoogleSignInRequestCode() {
        return RC_GOOGLE_SIGN_IN;
    }
}
