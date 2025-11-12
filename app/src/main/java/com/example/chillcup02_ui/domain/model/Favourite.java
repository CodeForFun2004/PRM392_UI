package com.example.chillcup02_ui.domain.model;

public class Favourite {
    private String id;
    private String userId;
    private Product product;
    private String createdAt;

    public Favourite() {}

    public Favourite(String id, String userId, Product product, String createdAt) {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
