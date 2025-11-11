package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private double basePrice;
    private String image;
    private String status;
    private double rating;
    private List<Size> sizeOptions;
    private List<Topping> toppingOptions;
    private String storeId;
    private List<String> categoryId; // Assuming category is just an ID for now
    private boolean isBanned;

    public Product(String id, String name, String description, double basePrice, String image, String status, double rating, List<Size> sizeOptions, List<Topping> toppingOptions, String storeId, List<String> categoryId, boolean isBanned) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.image = image;
        this.status = status;
        this.rating = rating;
        this.sizeOptions = sizeOptions;
        this.toppingOptions = toppingOptions;
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.isBanned = isBanned;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getBasePrice() { return basePrice; }
    public String getImage() { return image; }
    public String getStatus() { return status; }
    public double getRating() { return rating; }
    public List<Size> getSizeOptions() { return sizeOptions; }
    public List<Topping> getToppingOptions() { return toppingOptions; }
    public String getStoreId() { return storeId; }
    public List<String> getCategoryId() { return categoryId; }
    public boolean isBanned() { return isBanned; }
}
