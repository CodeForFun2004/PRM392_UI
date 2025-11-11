package com.example.chillcup02_ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.data.api.MockAuthService;
import com.example.chillcup02_ui.data.dto.AuthResponse;
import com.example.chillcup02_ui.data.dto.LoginRequest;
import com.example.chillcup02_ui.data.dto.RegisterRequest;
import com.example.chillcup02_ui.data.dto.VerifyOtpRequest;
import com.example.chillcup02_ui.databinding.ActivityLoginBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;
import com.example.chillcup02_ui.ui.common.BaseActivity;
import com.example.chillcup02_ui.util.Result;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class    LoginActivity extends BaseActivity {
    
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private MockAuthService mockAuthService;
    private AuthViewModel authViewModel;
    
    private String pendingRegisterEmail;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Apply window insets to root view
        applyWindowInsets(binding.getRoot());
        
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        
        // Initialize Mock Auth Service
        mockAuthService = MockAuthService.getInstance();
        
        // Initialize AuthViewModel
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.init(this);
        
        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        
        setupTabs();
        setupLoginForm();
        setupSignUpForm();
        setupOtpForm();
        setupGoogleSignIn();
        
        // Set welcome message
        binding.tvWelcomeTitle.setText(R.string.welcome_title);
    }
    
    private void setupTabs() {
        // Add tabs
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.login));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.sign_up));
        
        // Set default to login tab
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0));
        showLoginForm();
        
        // Handle tab selection
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showLoginForm();
                } else {
                    showSignUpForm();
                }
            }
            
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
    
    private void showLoginForm() {
        binding.loginFormContainer.setVisibility(View.VISIBLE);
        binding.signUpFormContainer.setVisibility(View.GONE);
        binding.otpContainer.setVisibility(View.GONE);
        binding.divider.setVisibility(View.VISIBLE);
        binding.tvOrContinue.setVisibility(View.VISIBLE);
        binding.btnLoginGoogle.setVisibility(View.VISIBLE);
    }
    
    private void showSignUpForm() {
        binding.loginFormContainer.setVisibility(View.GONE);
        binding.signUpFormContainer.setVisibility(View.VISIBLE);
        binding.otpContainer.setVisibility(View.GONE);
        binding.divider.setVisibility(View.GONE);
        binding.tvOrContinue.setVisibility(View.GONE);
        binding.btnLoginGoogle.setVisibility(View.GONE);
    }
    
    private void showOtpForm() {
        binding.loginFormContainer.setVisibility(View.GONE);
        binding.signUpFormContainer.setVisibility(View.GONE);
        binding.otpContainer.setVisibility(View.VISIBLE);
        binding.divider.setVisibility(View.GONE);
        binding.tvOrContinue.setVisibility(View.GONE);
        binding.btnLoginGoogle.setVisibility(View.GONE);
    }
    
    private void setupLoginForm() {
        binding.btnLogin.setOnClickListener(v -> handleLogin());
        
        binding.tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng quên mật khẩu sẽ được phát triển sau", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupSignUpForm() {
        binding.btnSignUp.setOnClickListener(v -> handleSignUp());
    }
    
    private void setupOtpForm() {
        binding.btnVerifyOtp.setOnClickListener(v -> handleVerifyOtp());
        
        binding.tvResendOtp.setOnClickListener(v -> {
            if (pendingRegisterEmail != null) {
                // Resend registration request
                RegisterRequest request = new RegisterRequest();
                request.setEmail(pendingRegisterEmail);
                // In real app, we'd need to store the original request
                Toast.makeText(this, "Vui lòng đăng ký lại", Toast.LENGTH_SHORT).show();
                showSignUpForm();
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1));
            }
        });
    }
    
    private void setupGoogleSignIn() {
        binding.btnLoginGoogle.setOnClickListener(v -> signInWithGoogle());
    }
    
    private void handleLogin() {
        String usernameOrEmail = binding.etEmailOrUsername.getText() != null 
                ? binding.etEmailOrUsername.getText().toString().trim() 
                : "";
        String password = binding.etPassword.getText() != null 
                ? binding.etPassword.getText().toString() 
                : "";
        
        if (TextUtils.isEmpty(usernameOrEmail)) {
            binding.tilEmailOrUsername.setError("Vui lòng nhập email hoặc username");
            return;
        }
        
        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setError("Vui lòng nhập mật khẩu");
            return;
        }
        
        binding.tilEmailOrUsername.setError(null);
        binding.tilPassword.setError(null);
        
        showProgress(true);
        
        LoginRequest request = new LoginRequest(usernameOrEmail, password);
        mockAuthService.login(request, result -> {
            showProgress(false);
            if (result.isSuccess()) {
                AuthResponse response = result.getData();
                // Save user info to ViewModel
                authViewModel.setMockUser(
                    response.getUser(),
                    response.getAccessToken(),
                    response.getRefreshToken()
                );
                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            } else {
                Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void handleSignUp() {
        String fullname = binding.etFullname.getText() != null 
                ? binding.etFullname.getText().toString().trim() 
                : "";
        String username = binding.etSignUpUsername.getText() != null 
                ? binding.etSignUpUsername.getText().toString().trim() 
                : "";
        String email = binding.etSignUpEmail.getText() != null 
                ? binding.etSignUpEmail.getText().toString().trim() 
                : "";
        String password = binding.etSignUpPassword.getText() != null 
                ? binding.etSignUpPassword.getText().toString() 
                : "";
        String confirmPassword = binding.etConfirmPassword.getText() != null 
                ? binding.etConfirmPassword.getText().toString() 
                : "";
        
        // Validate
        boolean hasError = false;
        
        if (TextUtils.isEmpty(fullname)) {
            binding.tilFullname.setError("Vui lòng nhập họ tên");
            hasError = true;
        } else {
            binding.tilFullname.setError(null);
        }
        
        if (TextUtils.isEmpty(username)) {
            binding.tilSignUpUsername.setError("Vui lòng nhập username");
            hasError = true;
        } else {
            binding.tilSignUpUsername.setError(null);
        }
        
        if (TextUtils.isEmpty(email)) {
            binding.tilSignUpEmail.setError("Vui lòng nhập email");
            hasError = true;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilSignUpEmail.setError("Email không hợp lệ");
            hasError = true;
        } else {
            binding.tilSignUpEmail.setError(null);
        }
        
        if (TextUtils.isEmpty(password)) {
            binding.tilSignUpPassword.setError("Vui lòng nhập mật khẩu");
            hasError = true;
        } else if (password.length() < 6) {
            binding.tilSignUpPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            hasError = true;
        } else {
            binding.tilSignUpPassword.setError(null);
        }
        
        if (TextUtils.isEmpty(confirmPassword)) {
            binding.tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            hasError = true;
        } else if (!password.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError("Mật khẩu không khớp");
            hasError = true;
        } else {
            binding.tilConfirmPassword.setError(null);
        }
        
        if (hasError) {
            return;
        }
        
        showProgress(true);
        
        RegisterRequest request = new RegisterRequest(fullname, username, email, password);
        mockAuthService.register(request, result -> {
            showProgress(false);
            if (result.isSuccess()) {
                pendingRegisterEmail = email;
                String message = result.getData().getMessage();
                binding.tvOtpMessage.setText(message);
                showOtpForm();
                Toast.makeText(this, "Vui lòng kiểm tra email để lấy mã OTP", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void handleVerifyOtp() {
        if (pendingRegisterEmail == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy email đăng ký", Toast.LENGTH_SHORT).show();
            showSignUpForm();
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1));
            return;
        }
        
        String otp = binding.etOtp.getText() != null 
                ? binding.etOtp.getText().toString().trim() 
                : "";
        
        if (TextUtils.isEmpty(otp)) {
            binding.tilOtp.setError("Vui lòng nhập mã OTP");
            return;
        }
        
        binding.tilOtp.setError(null);
        showProgress(true);
        
        VerifyOtpRequest request = new VerifyOtpRequest(pendingRegisterEmail, otp);
        mockAuthService.verifyOtp(request, result -> {
            showProgress(false);
            if (result.isSuccess()) {
                AuthResponse response = result.getData();
                // Save user info to ViewModel
                authViewModel.setMockUser(
                    response.getUser(),
                    response.getAccessToken(),
                    response.getRefreshToken()
                );
                pendingRegisterEmail = null; // Clear pending email
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                navigateToMain();
            } else {
                Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already signed in (Firebase)
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, navigate to MainActivity
            navigateToMain();
        }
    }
    
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void firebaseAuthWithGoogle(String idToken) {
        showProgress(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            navigateToMain();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Đăng nhập Firebase thất bại: " + 
                                    (task.getException() != null ? task.getException().getMessage() : "Unknown error"), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    
    private void navigateToMain() {
        Intent intent = new Intent(this, com.example.chillcup02_ui.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    private void showProgress(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
        binding.btnSignUp.setEnabled(!show);
        binding.btnVerifyOtp.setEnabled(!show);
        binding.btnLoginGoogle.setEnabled(!show);
    }
}
