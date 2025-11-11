package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("cartItems")
    private List<String> cartItems; // Sent as ObjectId strings
    @SerializedName("subtotal")
    private double subtotal;
    @SerializedName("deliveryFee")
    private double deliveryFee;
    @SerializedName("discount")
    private double discount;
    @SerializedName("total")
    private double total;
    @SerializedName("promoCode")
    private String promoCode;
    @SerializedName("isCheckedOut")
    private boolean isCheckedOut;

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public List<String> getCartItems() { return cartItems; }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }
    public String getPromoCode() { return promoCode; }
    public boolean isCheckedOut() { return isCheckedOut; }
}
