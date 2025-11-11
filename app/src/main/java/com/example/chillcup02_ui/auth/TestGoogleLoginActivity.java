package com.example.chillcup02_ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.ui.common.BaseActivity;
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

public class TestGoogleLoginActivity extends BaseActivity {
    
    private static final String TAG = "TestGoogleLogin";
    private static final int RC_SIGN_IN = 9001;
    
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    
    private Button btnSignIn;
    private Button btnSignOut;
    private TextView tvStatus;
    private TextView tvUserInfo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_google_login);
        
        // Apply window insets to root view
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            applyWindowInsets(rootView);
        }
        
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        // Initialize views
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignOut = findViewById(R.id.btnSignOut);
        tvStatus = findViewById(R.id.tvStatus);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        
        // Configure Google Sign-In
        // Web Client ID tự động được generate từ google-services.json bởi Google Services plugin
        // R.string.default_web_client_id được tạo tự động từ OAuth client có client_type = 3
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        
        // Set click listeners
        btnSignIn.setOnClickListener(v -> signIn());
        btnSignOut.setOnClickListener(v -> signOut());
        
        // Check if user is already signed in
        updateUI(mAuth.getCurrentUser());
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();
        
        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
                Toast.makeText(TestGoogleLoginActivity.this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign-In failed
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
    }
    
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(TestGoogleLoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(TestGoogleLoginActivity.this, "Đăng nhập Firebase thất bại: " + 
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // User is signed in
            tvStatus.setText("Trạng thái: Đã đăng nhập");
            tvUserInfo.setText("Email: " + user.getEmail() + "\n" +
                             "Tên: " + user.getDisplayName() + "\n" +
                             "UID: " + user.getUid());
            btnSignIn.setEnabled(false);
            btnSignOut.setEnabled(true);
        } else {
            // User is signed out
            tvStatus.setText("Trạng thái: Chưa đăng nhập");
            tvUserInfo.setText("Vui lòng đăng nhập bằng Google");
            btnSignIn.setEnabled(true);
            btnSignOut.setEnabled(false);
        }
    }
}

