package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.List;

public class Cart implements Serializable {
    private String id;
    private String userId;
    // In the domain model, you might want to hold the full CartItem objects, 
    // but for now, we'll stick to IDs for simplicity to match the DTO.
    private List<String> cartItemIds;
    private double subtotal;
    private double deliveryFee;
    private double discount;
    private double total;
    private String promoCode;
    private boolean isCheckedOut;

    public Cart(String id, String userId, List<String> cartItemIds, double subtotal, double deliveryFee, double discount, double total, String promoCode, boolean isCheckedOut) {
        this.id = id;
        this.userId = userId;
        this.cartItemIds = cartItemIds;
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.discount = discount;
        this.total = total;
        this.promoCode = promoCode;
        this.isCheckedOut = isCheckedOut;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public List<String> getCartItemIds() { return cartItemIds; }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    public String getPromoCode() { return promoCode; }
    public boolean isCheckedOut() { return isCheckedOut; }
}
