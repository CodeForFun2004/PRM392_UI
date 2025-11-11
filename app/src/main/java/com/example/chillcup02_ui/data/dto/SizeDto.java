package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;

public class SizeDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("size")
    private String size; // e.g., "S", "M", "L"
    @SerializedName("name")
    private String name; // e.g., "Small", "Medium", "Large"
    @SerializedName("multiplier")
    private double multiplier;
    @SerializedName("volume")
    private String volume;

    // Getters
    public String getId() { return id; }
    public String getSize() { return size; }
    public String getName() { return name; }
    public double getMultiplier() { return multiplier; }
    public String getVolume() { return volume; }
}
