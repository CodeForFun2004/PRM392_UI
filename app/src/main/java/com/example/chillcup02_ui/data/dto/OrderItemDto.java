package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderItemDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("productId")
    private String productId;
    @SerializedName("name")
    private String name;
    @SerializedName("size")
    private String size;
    @SerializedName("toppings")
    private List<ToppingDto> toppings; 
    
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("price")
    private double price;

    // Getters
    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public List<ToppingDto> getToppings() { return toppings; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}
