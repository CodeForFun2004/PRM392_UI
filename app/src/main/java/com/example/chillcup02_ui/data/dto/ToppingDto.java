package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;

public class ToppingDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private double price;
    @SerializedName("icon")
    private String icon;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getIcon() { return icon; }
}
