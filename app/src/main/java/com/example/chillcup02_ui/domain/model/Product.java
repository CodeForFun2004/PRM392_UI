package com.example.chillcup02_ui.domain.model;

import java.util.List;

public class Product {

    private String id;
    private String name;
    private String description;
    private double basePrice;
    private String image;
    private String status;
    private double rating;
    private List<Size> sizeOptions;
    private List<Topping> toppingOptions;
    private String storeName;
    private List<Category> categories;
    private boolean isBanned;

    public Product() {}

    public Product(String id, String name, String description, double basePrice, String image,
                   String status, double rating, List<Size> sizeOptions, List<Topping> toppingOptions,
                   String storeName, List<Category> categories, boolean isBanned) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.image = image;
        this.status = status;
        this.rating = rating;
        this.sizeOptions = sizeOptions;
        this.toppingOptions = toppingOptions;
        this.storeName = storeName;
        this.categories = categories;
        this.isBanned = isBanned;
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

    public List<Size> getSizeOptions() { return sizeOptions; }
    public void setSizeOptions(List<Size> sizeOptions) { this.sizeOptions = sizeOptions; }

    public List<Topping> getToppingOptions() { return toppingOptions; }
    public void setToppingOptions(List<Topping> toppingOptions) { this.toppingOptions = toppingOptions; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public List<Category> getCategories() { return categories; }
    public void setCategories(List<Category> categories) { this.categories = categories; }

    public boolean isBanned() { return isBanned; }
    public void setBanned(boolean banned) { isBanned = banned; }
}

