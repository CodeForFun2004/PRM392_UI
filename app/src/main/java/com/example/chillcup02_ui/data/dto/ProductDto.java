package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductDto {
    @SerializedName("_id")
    private String id;
    private String name;
    private String description;
    private double basePrice;
    private String image;
    private String status;
    private double rating;
    private List<String> sizeOptions;
    private List<String> toppingOptions;
    private String storeId;
    private List<String> categoryId;
    private boolean isBanned;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getBasePrice() { return basePrice; }
    public String getImage() { return image; }
    public String getStatus() { return status; }
    public double getRating() { return rating; }
    public List<String> getSizeOptions() { return sizeOptions; }
    public List<String> getToppingOptions() { return toppingOptions; }
    public String getStoreId() { return storeId; }
    public List<String> getCategoryId() { return categoryId; }
    public boolean isBanned() { return isBanned; }
}
