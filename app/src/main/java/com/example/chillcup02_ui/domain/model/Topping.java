package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;

public class Topping implements Serializable {
    private String id;
    private String name;
    private double price;
    private String icon;

    public Topping(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Topping(String id, String name, double price, String icon) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.icon = icon;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getIcon() { return icon; }
}
