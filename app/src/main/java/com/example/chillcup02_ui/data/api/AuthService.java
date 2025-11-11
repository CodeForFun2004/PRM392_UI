package com.example.chillcup02_ui.data.api;

import android.util.Log;

import com.example.chillcup02_ui.data.dto.AuthResponse;
import com.example.chillcup02_ui.data.dto.LoginRequest;
import com.example.chillcup02_ui.data.dto.RegisterRequest;
import com.example.chillcup02_ui.data.dto.RegisterResponse;
import com.example.chillcup02_ui.data.dto.VerifyOtpRequest;
import com.example.chillcup02_ui.util.Result;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthService {
    private static final String TAG = "AuthService";
    private final ApiService apiService;

    public AuthService() {
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    public void registerRequest(RegisterRequest request, ResultCallback<RegisterResponse> callback) {
        Call<RegisterResponse> call = apiService.registerRequest(request);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(Result.success(response.body()));
                } else {
                    String errorMessage = "Đăng ký thất bại";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response", e);
                        }
                    }
                    callback.onResult(Result.error(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e(TAG, "Register request failed", t);
                callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
            }
        });
    }

    public void verifyRegister(VerifyOtpRequest request, ResultCallback<AuthResponse> callback) {
        Call<AuthResponse> call = apiService.verifyRegister(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(Result.success(response.body()));
                } else {
                    String errorMessage = "Xác thực OTP thất bại";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response", e);
                        }
                    }
                    callback.onResult(Result.error(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Verify register failed", t);
                callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
            }
        });
    }

    public void login(LoginRequest request, ResultCallback<AuthResponse> callback) {
        Call<AuthResponse> call = apiService.login(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(Result.success(response.body()));
                } else {
                    String errorMessage = "Đăng nhập thất bại";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response", e);
                        }
                    }
                    callback.onResult(Result.error(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Login failed", t);
                callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
            }
        });
    }

    public void refreshToken(String refreshToken, ResultCallback<Map<String, String>> callback) {
        Map<String, String> request = new HashMap<>();
        request.put("refreshToken", refreshToken);

        Call<Map<String, String>> call = apiService.refreshToken(request);
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(Result.success(response.body()));
                } else {
                    String errorMessage = "Làm mới token thất bại";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response", e);
                        }
                    }
                    callback.onResult(Result.error(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Refresh token failed", t);
                callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
            }
        });
    }

    public void logout(ResultCallback<Map<String, String>> callback) {
        Call<Map<String, String>> call = apiService.logout();
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    callback.onResult(Result.success(response.body()));
                } else {
                    String errorMessage = "Đăng xuất thất bại";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error parsing error response", e);
                        }
                    }
                    callback.onResult(Result.error(errorMessage));
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Logout failed", t);
                callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
            }
        });
    }

    public interface ResultCallback<T> {
        void onResult(Result<T> result);
    }
}
