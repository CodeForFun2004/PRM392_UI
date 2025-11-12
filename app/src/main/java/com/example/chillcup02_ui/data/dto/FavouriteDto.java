package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FavouriteDto {
    @SerializedName("_id")
    private String id;

    @SerializedName("user")
    private String userId;

    @SerializedName("product")
    private FavouriteProductDto product;

    @SerializedName("createdAt")
    private String createdAt;

    public FavouriteDto() {}

    public FavouriteDto(String id, String userId, FavouriteProductDto product, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public FavouriteProductDto getProduct() {
        return product;
    }

    public void setProduct(FavouriteProductDto product) {
        this.product = product;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Wrapper class for the API response
    public static class FavouritesResponse {
        @SerializedName("favourites")
        private List<FavouriteDto> favourites;

        public FavouritesResponse() {}

        public List<FavouriteDto> getFavourites() {
            return favourites;
        }

        public void setFavourites(List<FavouriteDto> favourites) {
            this.favourites = favourites;
        }
    }

    // Simplified product DTO for favourites (sizeOptions/toppingOptions as strings)
    public static class FavouriteProductDto {
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
        private List<String> sizeOptions; // Just ObjectIds as strings

        @SerializedName("toppingOptions")
        private List<String> toppingOptions; // Just ObjectIds as strings

        @SerializedName("storeId")
        private String storeId;

        @SerializedName("categoryId")
        private List<String> categoryId; // Just ObjectIds as strings

        @SerializedName("isBanned")
        private boolean isBanned;

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
        public List<String> getSizeOptions() { return sizeOptions; }
        public void setSizeOptions(List<String> sizeOptions) { this.sizeOptions = sizeOptions; }
        public List<String> getToppingOptions() { return toppingOptions; }
        public void setToppingOptions(List<String> toppingOptions) { this.toppingOptions = toppingOptions; }
        public String getStoreId() { return storeId; }
        public void setStoreId(String storeId) { this.storeId = storeId; }
        public List<String> getCategoryId() { return categoryId; }
        public void setCategoryId(List<String> categoryId) { this.categoryId = categoryId; }
        public boolean isBanned() { return isBanned; }
        public void setBanned(boolean banned) { isBanned = banned; }
    }
}
