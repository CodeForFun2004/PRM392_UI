package com.example.chillcup02_ui.data.api;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.chillcup02_ui.data.dto.DiscountDto;
import com.example.chillcup02_ui.util.Result;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscountService {

    private static DiscountService instance;
    private final ApiService apiService;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private DiscountService() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public static synchronized DiscountService getInstance() {
        if (instance == null) {
            instance = new DiscountService();
        }
        return instance;
    }

    /**
     * Get all global discounts
     * Calls GET /api/discounts
     */
    public void getAllDiscounts(DiscountListCallback callback) {
        Log.d("DiscountService", "🔄 [REQUEST] getAllDiscounts");
        Log.d("DiscountService", "🔄 [API CALL] GET /api/discounts");

        Call<List<DiscountDto>> call = apiService.getAllDiscounts();

        call.enqueue(new Callback<List<DiscountDto>>() {
            @Override
            public void onResponse(Call<List<DiscountDto>> call, Response<List<DiscountDto>> response) {
                Log.d("DiscountService", "📡 [RESPONSE] Status: " + response.code() + " " + response.message());

                handler.post(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        List<DiscountDto> discounts = response.body();
                        Log.d("DiscountService", "✅ [SUCCESS] Retrieved " + discounts.size() + " discount(s)");
                        for (int i = 0; i < Math.min(discounts.size(), 3); i++) {
                            DiscountDto discount = discounts.get(i);
                            Log.d("DiscountService", "   - " + discount.getTitle() + " (" + discount.getPromotionCode() + ") - Used: " + discount.getIsUsed());
                        }
                        if (discounts.size() > 3) {
                            Log.d("DiscountService", "   ... and " + (discounts.size() - 3) + " more");
                        }
                        callback.onResult(Result.success(response.body()));
                    } else {
                        String errorMessage = "Không thể tải danh sách mã giảm giá";
                        try {
                            if (response.errorBody() != null) {
                                String errorBody = response.errorBody().string();
                                Log.e("DiscountService", "❌ [ERROR RESPONSE] Body: " + errorBody);
                                errorMessage = errorBody;
                            } else {
                                Log.e("DiscountService", "❌ [ERROR RESPONSE] No error body, status: " + response.code());
                            }
                        } catch (IOException e) {
                            Log.e("DiscountService", "❌ [ERROR PARSING] Failed to read error body", e);
                            e.printStackTrace();
                        }
                        callback.onResult(Result.error(errorMessage));
                    }
                });
            }

            @Override
            public void onFailure(Call<List<DiscountDto>> call, Throwable t) {
                Log.e("DiscountService", "💥 [NETWORK FAILURE] " + t.getMessage(), t);
                handler.post(() -> {
                    callback.onResult(Result.error("Lỗi kết nối: " + t.getMessage()));
                });
            }
        });
    }

    public interface DiscountListCallback {
        void onResult(Result<List<DiscountDto>> result);
    }
}
