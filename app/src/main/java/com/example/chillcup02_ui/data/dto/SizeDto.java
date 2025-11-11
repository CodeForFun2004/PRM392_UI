package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;

public class SizeDto {
    @SerializedName("_id")
    private String id;
    private String size;
    private String name;
    private double multiplier;
    private String volume;

    // Getters
    public String getId() { return id; }
    public String getSize() { return size; }
    public String getName() { return name; }
    public double getMultiplier() { return multiplier; }
    public String getVolume() { return volume; }
}
