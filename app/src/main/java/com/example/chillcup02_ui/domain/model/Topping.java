package com.example.chillcup02_ui.domain.model;

public class Topping {
    private String id;
    private String name;
    private double price;
    private String icon;

    public Topping() {}

    public Topping(String id, String name, double price, String icon) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.icon = icon;
    }

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
