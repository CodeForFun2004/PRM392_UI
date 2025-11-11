package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.JsonAdapter;
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
    private List<SizeDto> sizeOptions;

    @SerializedName("toppingOptions")
    private List<ToppingDto> toppingOptions;

    @SerializedName("storeId")
    @JsonAdapter(StoreIdDeserializer.class)
    private String storeId;

    @SerializedName("categoryId")
    private List<CategoryDto> categoryId;

    @SerializedName("isBanned")
    private boolean isBanned;

    // Nested DTOs
    public static class SizeDto {
        @SerializedName("_id")
        private String id;
        @SerializedName("size")
        private String size;
        @SerializedName("name")
        private String name;
        @SerializedName("multiplier")
        private double multiplier;
        @SerializedName("volume")
        private String volume;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getMultiplier() { return multiplier; }
        public void setMultiplier(double multiplier) { this.multiplier = multiplier; }
        public String getVolume() { return volume; }
        public void setVolume(String volume) { this.volume = volume; }
    }

    public static class ToppingDto {
        @SerializedName("_id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("price")
        private double price;
        @SerializedName("icon")
        private String icon;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
    }

    public static class StoreDto {
        @SerializedName("_id")
        private String id;
        @SerializedName("name")
        private String name;

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public List<SizeDto> getSizeOptions() { return sizeOptions; }
    public void setSizeOptions(List<SizeDto> sizeOptions) { this.sizeOptions = sizeOptions; }
    public List<ToppingDto> getToppingOptions() { return toppingOptions; }
    public void setToppingOptions(List<ToppingDto> toppingOptions) { this.toppingOptions = toppingOptions; }
    public String getStoreId() { return storeId; }
    public void setStoreId(String storeId) { this.storeId = storeId; }
    public List<CategoryDto> getCategoryId() { return categoryId; }
    public void setCategoryId(List<CategoryDto> categoryId) { this.categoryId = categoryId; }
    public boolean isBanned() { return isBanned; }
    public void setBanned(boolean banned) { isBanned = banned; }
}
