package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartItemDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("productId")
    private String productId;
    @SerializedName("size")
    private String size;
    @SerializedName("toppings")
    private List<String> toppings; // Sent as ObjectId strings
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("price")
    private double price;

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getProductId() { return productId; }
    public String getSize() { return size; }
    public List<String> getToppings() { return toppings; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}
