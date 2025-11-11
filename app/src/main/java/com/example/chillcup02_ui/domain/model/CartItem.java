package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.List;

public class CartItem implements Serializable {
    private String id;
    private String userId;
    private String productId;
    private String size; // e.g., "S", "M", "L"
    private List<String> toppingIds;
    private int quantity;
    private double price;

    public CartItem(String id, String userId, String productId, String size, List<String> toppingIds, int quantity, double price) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.size = size;
        this.toppingIds = toppingIds;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getProductId() { return productId; }
    public String getSize() { return size; }
    public List<String> getToppingIds() { return toppingIds; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}
