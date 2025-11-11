package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;

public class FavouriteDto {
    @SerializedName("_id")
    private String id;

    @SerializedName("user")
    private String userId;

    @SerializedName("product")
    private ProductDto product;

    @SerializedName("createdAt")
    private String createdAt;

    public FavouriteDto() {}

    public FavouriteDto(String id, String userId, ProductDto product, String createdAt) {
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

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
