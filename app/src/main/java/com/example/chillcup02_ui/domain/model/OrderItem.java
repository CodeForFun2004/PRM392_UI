package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.List;

public class OrderItem implements Serializable {
    private String id;
    private String productId;
    private String name;
    private String size;
    private List<Topping> toppings; // CORRECTED: This now uses the proper Topping model
    private int quantity;
    private double price;

    public OrderItem(String id, String productId, String name, String size, List<Topping> toppings, int quantity, double price) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.size = size;
        this.toppings = toppings;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getId() { return id; }
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public String getSize() { return size; }
    public List<Topping> getToppings() { return toppings; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}
