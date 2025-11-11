package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private double basePrice;
    private String image;
    private ProductStatus status;
    private double rating;
    private List<String> sizeOptions;
    private List<String> toppingOptions;
    private String storeId;
    private List<String> categoryIds;
    private boolean isBanned;

    public enum ProductStatus {
        NEW, OLD
    }

    public Product(String id, String name, String description, double basePrice, String image, ProductStatus status, double rating, List<String> sizeOptions, List<String> toppingOptions, String storeId, List<String> categoryIds, boolean isBanned) {
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
        this.categoryIds = categoryIds;
        this.isBanned = isBanned;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getBasePrice() { return basePrice; }
    public String getImage() { return image; }
    public ProductStatus getStatus() { return status; }
    public double getRating() { return rating; }
    public List<String> getSizeOptions() { return sizeOptions; }
    public List<String> getToppingOptions() { return toppingOptions; }
    public String getStoreId() { return storeId; }
    public List<String> getCategoryIds() { return categoryIds; }
    public boolean isBanned() { return isBanned; }
}
