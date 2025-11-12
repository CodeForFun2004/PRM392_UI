package com.example.chillcup02_ui.data.api;

import android.os.Handler;
import android.os.Looper;

import com.example.chillcup02_ui.data.dto.LoyaltyDto;
import com.example.chillcup02_ui.util.Result;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MockLoyaltyService {
    
    private static MockLoyaltyService instance;
    private final Map<String, LoyaltyDto> mockLoyaltyPoints = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    
    private MockLoyaltyService() {
        initializeMockData();
    }
    
    public static synchronized MockLoyaltyService getInstance() {
        if (instance == null) {
            instance = new MockLoyaltyService();
        }
        return instance;
    }
    
    private void initializeMockData() {
        // Mock loyalty points for user_001
        LoyaltyDto loyalty1 = new LoyaltyDto();
        loyalty1.setUserId("user_001");
        loyalty1.setTotalPoints(21291);
        loyalty1.setCreatedAt(new Date(System.currentTimeMillis() - 86400000 * 30));
        loyalty1.setUpdatedAt(new Date());
        mockLoyaltyPoints.put("user_001", loyalty1);
        
        // Mock loyalty points for user_002
        LoyaltyDto loyalty2 = new LoyaltyDto();
        loyalty2.setUserId("user_002");
        loyalty2.setTotalPoints(15000);
        loyalty2.setCreatedAt(new Date(System.currentTimeMillis() - 86400000 * 15));
        loyalty2.setUpdatedAt(new Date());
        mockLoyaltyPoints.put("user_002", loyalty2);
    }
    
    /**
     * Get loyalty points for a user
     * Simulates GET /api/loyalty/me or GET /api/loyalty/my-points
     */
    public void getMyPoints(String userId, LoyaltyCallback callback) {
        handler.postDelayed(() -> {
            LoyaltyDto loyalty = mockLoyaltyPoints.get(userId);
            
            if (loyalty == null) {
                // Create default loyalty with 0 points for new users
                loyalty = new LoyaltyDto();
                loyalty.setUserId(userId);
                loyalty.setTotalPoints(0);
                loyalty.setCreatedAt(new Date());
                loyalty.setUpdatedAt(new Date());
                mockLoyaltyPoints.put(userId, loyalty);
            }
            
            LoyaltyDto loyaltyCopy = copyLoyalty(loyalty);
            callback.onResult(Result.success(loyaltyCopy));
        }, 500);
    }
    
    /**
     * Get point history for a user
     * Simulates GET /api/loyalty/history
     */
    public void getPointHistory(String userId, PointHistoryCallback callback) {
        handler.postDelayed(() -> {
            LoyaltyDto loyalty = mockLoyaltyPoints.get(userId);
            
            if (loyalty == null || loyalty.getHistory() == null || loyalty.getHistory().isEmpty()) {
                callback.onResult(Result.success(new java.util.ArrayList<>()));
                return;
            }
            
            callback.onResult(Result.success(loyalty.getHistory()));
        }, 500);
    }
    
    private LoyaltyDto copyLoyalty(LoyaltyDto original) {
        LoyaltyDto copy = new LoyaltyDto();
        copy.setUserId(original.getUserId());
        copy.setTotalPoints(original.getTotalPoints());
        copy.setHistory(original.getHistory());
        copy.setCreatedAt(original.getCreatedAt());
        copy.setUpdatedAt(original.getUpdatedAt());
        return copy;
    }
    
    public interface LoyaltyCallback {
        void onResult(Result<LoyaltyDto> result);
    }
    
    public interface PointHistoryCallback {
        void onResult(Result<java.util.List<LoyaltyDto.PointHistory>> result);
    }
}

