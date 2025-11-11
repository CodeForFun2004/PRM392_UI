package com.example.chillcup02_ui.data.api;

import android.os.Handler;
import android.os.Looper;

import com.example.chillcup02_ui.data.dto.DiscountDto;
import com.example.chillcup02_ui.util.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockDiscountService {
    
    private static MockDiscountService instance;
    private final Map<String, DiscountDto> mockDiscounts = new HashMap<>();
    private final Map<String, Map<String, Boolean>> userDiscounts = new HashMap<>(); // userId -> discountId -> isUsed
    private final Handler handler = new Handler(Looper.getMainLooper());
    
    private MockDiscountService() {
        initializeMockData();
    }
    
    public static synchronized MockDiscountService getInstance() {
        if (instance == null) {
            instance = new MockDiscountService();
        }
        return instance;
    }
    
    private void initializeMockData() {
        // Mock discount 1
        DiscountDto discount1 = new DiscountDto();
        discount1.setId("discount_001");
        discount1.setTitle("Giảm 20% cho đơn hôm ni");
        discount1.setDescription("Áp dụng cho đơn hàng đầu tiên từ 3.000.000₫");
        discount1.setPromotionCode("TCC-149788");
        discount1.setDiscountPercent(20);
        discount1.setMinOrder(500000);
        discount1.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L * 365)); // 1 year from now
        discount1.setIsLock(false);
        discount1.setRequiredPoints(0);
        discount1.setCreatedAt(new Date(System.currentTimeMillis() - 86400000L * 30));
        discount1.setUpdatedAt(new Date());
        mockDiscounts.put("discount_001", discount1);
        
        // Mock discount 2
        DiscountDto discount2 = new DiscountDto();
        discount2.setId("discount_002");
        discount2.setTitle("Giảm 25% cho đơn đầu tiên");
        discount2.setDescription("Áp dụng cho đơn hàng đầu tiên từ 300.000₫");
        discount2.setPromotionCode("TCC-364858");
        discount2.setDiscountPercent(25);
        discount2.setMinOrder(300000);
        discount2.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L * 365));
        discount2.setIsLock(false);
        discount2.setRequiredPoints(0);
        discount2.setCreatedAt(new Date(System.currentTimeMillis() - 86400000L * 20));
        discount2.setUpdatedAt(new Date());
        mockDiscounts.put("discount_002", discount2);
        
        // Mock discount 3
        DiscountDto discount3 = new DiscountDto();
        discount3.setId("discount_003");
        discount3.setTitle("Giảm 35% cho đơn đầu tiên");
        discount3.setDescription("Áp dụng cho đơn hàng đầu tiên từ 400.000₫");
        discount3.setPromotionCode("TCC-887199");
        discount3.setDiscountPercent(35);
        discount3.setMinOrder(400000);
        discount3.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L * 365));
        discount3.setIsLock(false);
        discount3.setRequiredPoints(0);
        discount3.setCreatedAt(new Date(System.currentTimeMillis() - 86400000L * 15));
        discount3.setUpdatedAt(new Date());
        mockDiscounts.put("discount_003", discount3);
        
        // Mock discount 4 (used)
        DiscountDto discount4 = new DiscountDto();
        discount4.setId("discount_004");
        discount4.setTitle("Giảm 20% cho đơn đầu tiên");
        discount4.setDescription("Áp dụng cho đơn hàng đầu tiên từ 50.000₫");
        discount4.setPromotionCode("TCC-887199");
        discount4.setDiscountPercent(20);
        discount4.setMinOrder(50000);
        discount4.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L * 365));
        discount4.setIsLock(false);
        discount4.setRequiredPoints(0);
        discount4.setCreatedAt(new Date(System.currentTimeMillis() - 86400000L * 60));
        discount4.setUpdatedAt(new Date());
        mockDiscounts.put("discount_004", discount4);
        
        // Mock discount 5 (used)
        DiscountDto discount5 = new DiscountDto();
        discount5.setId("discount_005");
        discount5.setTitle("Giảm 30% cho đơn 700000");
        discount5.setDescription("Áp dụng cho đơn hàng đầu tiên từ 3.000.000");
        discount5.setPromotionCode("TCC-950669");
        discount5.setDiscountPercent(30);
        discount5.setMinOrder(700000);
        discount5.setExpiryDate(new Date(System.currentTimeMillis() + 86400000L * 365));
        discount5.setIsLock(false);
        discount5.setRequiredPoints(0);
        discount5.setCreatedAt(new Date(System.currentTimeMillis() - 86400000L * 45));
        discount5.setUpdatedAt(new Date());
        mockDiscounts.put("discount_005", discount5);
        
        // Mock user discounts (user_001 has some used vouchers)
        Map<String, Boolean> user1Discounts = new HashMap<>();
        user1Discounts.put("discount_004", true); // used
        user1Discounts.put("discount_005", true); // used
        user1Discounts.put("discount_001", false); // not used yet
        userDiscounts.put("user_001", user1Discounts);
    }
    
    /**
     * Get user discounts with filter
     * Simulates GET /api/user-discounts?isUsed=true/false
     */
    public void getUserDiscounts(String userId, String filterIsUsed, DiscountListCallback callback) {
        handler.postDelayed(() -> {
            List<DiscountDto> result = new ArrayList<>();
            Map<String, Boolean> userDiscountMap = userDiscounts.get(userId);
            
            // If user doesn't have any discounts yet, initialize with some default vouchers
            if (userDiscountMap == null) {
                userDiscountMap = new HashMap<>();
                // Give user some usable vouchers by default
                userDiscountMap.put("discount_001", false); // not used
                userDiscountMap.put("discount_002", false); // not used
                userDiscountMap.put("discount_003", false); // not used
                userDiscounts.put(userId, userDiscountMap);
            }
            
            for (DiscountDto discount : mockDiscounts.values()) {
                if (discount.getIsLock() != null && discount.getIsLock()) {
                    continue; // Skip locked discounts
                }
                
                if (discount.getExpiryDate() != null && discount.getExpiryDate().before(new Date())) {
                    continue; // Skip expired discounts
                }
                
                // Check if user has this discount
                Boolean isUsed = null;
                if (userDiscountMap != null && userDiscountMap.containsKey(discount.getId())) {
                    isUsed = userDiscountMap.get(discount.getId());
                } else {
                    // If user doesn't have this discount in their map, they can't use it
                    // Only show discounts that are in their map
                    continue;
                }
                
                // Apply filter
                if (filterIsUsed != null) {
                    if ("true".equals(filterIsUsed) && !Boolean.TRUE.equals(isUsed)) {
                        continue;
                    }
                    if ("false".equals(filterIsUsed) && !Boolean.FALSE.equals(isUsed)) {
                        continue;
                    }
                }
                
                DiscountDto discountCopy = copyDiscount(discount);
                discountCopy.setIsUsed(isUsed);
                result.add(discountCopy);
            }
            
            callback.onResult(Result.success(result));
        }, 500);
    }
    
    /**
     * Get available vouchers for redemption
     * Simulates GET /api/loyalty/available-vouchers
     */
    public void getAvailableVouchers(String userId, AvailableVouchersCallback callback) {
        handler.postDelayed(() -> {
            List<DiscountDto> vouchers = new ArrayList<>();
            
            for (DiscountDto discount : mockDiscounts.values()) {
                if (discount.getIsLock() != null && discount.getIsLock()) {
                    continue;
                }
                
                if (discount.getExpiryDate() != null && discount.getExpiryDate().before(new Date())) {
                    continue;
                }
                
                DiscountDto discountCopy = copyDiscount(discount);
                vouchers.add(discountCopy);
            }
            
            callback.onResult(Result.success(vouchers));
        }, 500);
    }
    
    private DiscountDto copyDiscount(DiscountDto original) {
        DiscountDto copy = new DiscountDto();
        copy.setId(original.getId());
        copy.setTitle(original.getTitle());
        copy.setDescription(original.getDescription());
        copy.setPromotionCode(original.getPromotionCode());
        copy.setDiscountPercent(original.getDiscountPercent());
        copy.setExpiryDate(original.getExpiryDate());
        copy.setMinOrder(original.getMinOrder());
        copy.setIsLock(original.getIsLock());
        copy.setImage(original.getImage());
        copy.setRequiredPoints(original.getRequiredPoints());
        copy.setIsUsed(original.getIsUsed());
        copy.setCreatedAt(original.getCreatedAt());
        copy.setUpdatedAt(original.getUpdatedAt());
        return copy;
    }
    
    public interface DiscountListCallback {
        void onResult(Result<List<DiscountDto>> result);
    }
    
    public interface AvailableVouchersCallback {
        void onResult(Result<List<DiscountDto>> result);
    }
}

