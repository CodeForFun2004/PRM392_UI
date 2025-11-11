package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;

public class Size implements Serializable {
    private String id;
    private String sizeShortName; // e.g., "S", "M", "L"
    private String name; // e.g., "Small", "Medium", "Large"
    private double priceMultiplier;
    private String volume;

    public Size(String id, String sizeShortName, String name, double priceMultiplier, String volume) {
        this.id = id;
        this.sizeShortName = sizeShortName;
        this.name = name;
        this.priceMultiplier = priceMultiplier;
        this.volume = volume;
    }

    // Getters
    public String getId() { return id; }
    public String getSizeShortName() { return sizeShortName; }
    public String getName() { return name; }
    public double getPriceMultiplier() { return priceMultiplier; }
    public String getVolume() { return volume; }
}
