package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("basePrice")
    private double basePrice;
    @SerializedName("image")
    private String image;
    @SerializedName("status")
    private String status;
    @SerializedName("rating")
    private double rating;
    @SerializedName("sizeOptions")
    private List<String> sizeOptions; // Sent as ObjectId strings
    @SerializedName("toppingOptions")
    private List<String> toppingOptions; // Sent as ObjectId strings
    @SerializedName("storeId")
    private String storeId;
    @SerializedName("categoryId")
    private List<String> categoryId;
    @SerializedName("isBanned")
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
