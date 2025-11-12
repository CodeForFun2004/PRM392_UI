package com.example.chillcup02_ui.data.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.chillcup02_ui.data.dto.LoyaltyDto;
import com.example.chillcup02_ui.util.Result;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoyaltyService {

    private static LoyaltyService instance;
    private final ApiService apiService;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private LoyaltyService() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public static synchronized LoyaltyService getInstance() {
        if (instance == null) {
            instance = new LoyaltyService();
        }
        return instance;
    }

    /**
     * Get user's loyalty points
     * Calls GET /api/loyalty/me
     */
    public void getMyPoints(ResultCallback<Map<String, Integer>> callback) {
        Log.d("LoyaltyService", "🔄 [REQUEST] getMyPoints");
        Log.d("LoyaltyService", "🔄 [API CALL] GET /api/loyalty/me");

        Call<Map<String, Integer>> call = apiService.getMyPoints();

        call.enqueue(new Callback<Map<String, Integer>>() {
            @Override
            public void onResponse(Call<Map<String, Integer>> call, Response<Map<String, Integer>> response) {
                Log.d("LoyaltyService", "📡 [RESPONSE] Status: " + response.code() + " " + response.message());

                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        Map<String, Integer> data = response.body();
                        Log.d("LoyaltyService", "✅ [SUCCESS] Retrieved loyalty points: " + data.get("totalPoints"));
                        callback.onResult(Result.success(response.body()));
                    } else {
                        String errorMessage = "Không thể tải điểm tích lũy";
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] Body: " + errorBody);
                                errorMessage = errorBody;
                            } else {
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] No error body, status: " + response.code());
                            }
                        } catch (IOException e) {
                            Log.e("LoyaltyService", "❌ [ERROR PARSING] Failed to read error body", e);
                            e.printStackTrace();
                        }
                        callback.onResult(Result.error(errorMessage));
                    }
                });
            }

            @Override
            public void onFailure(Call<Map<String, Integer>> call, Throwable t) {
                Log.e("LoyaltyService", "💥 [NETWORK FAILURE] " + t.getMessage(), t);
                handler.post(() -> {
                    callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
                });
            }
        });
    }

    /**
     * Get user's point history
     * Calls GET /api/loyalty/history
     */
    public void getPointHistory(ResultCallback<List<LoyaltyDto.PointHistory>> callback) {
        Log.d("LoyaltyService", "🔄 [REQUEST] getPointHistory");
        Log.d("LoyaltyService", "🔄 [API CALL] GET /api/loyalty/history");

        Call<List<LoyaltyDto.PointHistory>> call = apiService.getPointHistory();

        call.enqueue(new Callback<List<LoyaltyDto.PointHistory>>() {
            @Override
            public void onResponse(Call<List<LoyaltyDto.PointHistory>> call, Response<List<LoyaltyDto.PointHistory>> response) {
                Log.d("LoyaltyService", "📡 [RESPONSE] Status: " + response.code() + " " + response.message());

                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        List<LoyaltyDto.PointHistory> history = response.body();
                        Log.d("LoyaltyService", "✅ [SUCCESS] Retrieved " + history.size() + " history entries");
                        callback.onResult(Result.success(response.body()));
                    } else {
                        String errorMessage = "Không thể tải lịch sử điểm";
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] Body: " + errorBody);
                                errorMessage = errorBody;
                            } else {
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] No error body, status: " + response.code());
                            }
                        } catch (IOException e) {
                            Log.e("LoyaltyService", "❌ [ERROR PARSING] Failed to read error body", e);
                            e.printStackTrace();
                        }
                        callback.onResult(Result.error(errorMessage));
                    }
                });
            }

            @Override
            public void onFailure(Call<List<LoyaltyDto.PointHistory>> call, Throwable t) {
                Log.e("LoyaltyService", "💥 [NETWORK FAILURE] " + t.getMessage(), t);
                handler.post(() -> {
                    callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
                });
            }
        });
    }

    /**
     * Get available vouchers for redemption
     * Calls GET /api/loyalty/available-vouchers
     */
    public void getAvailableVouchers(ResultCallback<Map<String, Object>> callback) {
        Log.d("LoyaltyService", "🔄 [REQUEST] getAvailableVouchers");
        Log.d("LoyaltyService", "🔄 [API CALL] GET /api/loyalty/available-vouchers");

        Call<Map<String, Object>> call = apiService.getAvailableVouchers();

        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Log.d("LoyaltyService", "📡 [RESPONSE] Status: " + response.code() + " " + response.message());

                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        Map<String, Object> data = response.body();
                        Log.d("LoyaltyService", "✅ [SUCCESS] Retrieved available vouchers");
                        callback.onResult(Result.success(response.body()));
                    } else {
                        String errorMessage = "Không thể tải danh sách voucher";
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] Body: " + errorBody);
                                errorMessage = errorBody;
                            } else {
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] No error body, status: " + response.code());
                            }
                        } catch (IOException e) {
                            Log.e("LoyaltyService", "❌ [ERROR PARSING] Failed to read error body", e);
                            e.printStackTrace();
                        }
                        callback.onResult(Result.error(errorMessage));
                    }
                });
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("LoyaltyService", "💥 [NETWORK FAILURE] " + t.getMessage(), t);
                handler.post(() -> {
                    callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
                });
            }
        });
    }

    /**
     * Redeem voucher using points
     * Calls POST /api/loyalty/redeem
     */
    public void redeemVoucher(Map<String, String> request, ResultCallback<Map<String, String>> callback) {
        Log.d("LoyaltyService", "🔄 [REQUEST] redeemVoucher");
        Log.d("LoyaltyService", "🔄 [API CALL] POST /api/loyalty/redeem");

        Call<Map<String, String>> call = apiService.redeemVoucher(request);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Log.d("LoyaltyService", "📡 [RESPONSE] Status: " + response.code() + " " + response.message());

                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        Map<String, String> data = response.body();
                        Log.d("LoyaltyService", "✅ [SUCCESS] Voucher redeemed successfully");
                        callback.onResult(Result.success(response.body()));
                    } else {
                        String errorMessage = "Không thể đổi voucher";
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] Body: " + errorBody);
                                errorMessage = errorBody;
                            } else {
                                Log.e("LoyaltyService", "❌ [ERROR RESPONSE] No error body, status: " + response.code());
                            }
                        } catch (IOException e) {
                            Log.e("LoyaltyService", "❌ [ERROR PARSING] Failed to read error body", e);
                            e.printStackTrace();
                        }
                        callback.onResult(Result.error(errorMessage));
                    }
                });
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e("LoyaltyService", "💥 [NETWORK FAILURE] " + t.getMessage(), t);
                handler.post(() -> {
                    callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
                });
            }
        });
    }

    public interface ResultCallback<T> {
        void onResult(Result<T> result);
    }
}
