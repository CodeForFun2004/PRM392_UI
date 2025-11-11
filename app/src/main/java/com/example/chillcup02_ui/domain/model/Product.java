package com.example.chillcup02_ui.domain.model;

public class Product {
    public String id;
    public String name;
    public int price;
    public String image;

    public Product(String id, String name, int price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }
}

