package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;

public class Size implements Serializable {
    private String id;
    private String size;
    private String name;
    private double multiplier;
    private String volume;

    public Size(String id, String size, String name, double multiplier, String volume) {
        this.id = id;
        this.size = size;
        this.name = name;
        this.multiplier = multiplier;
        this.volume = volume;
    }

    // Getters
    public String getId() { return id; }
    public String getSize() { return size; }
    public String getName() { return name; }
    public double getMultiplier() { return multiplier; }
    public String getVolume() { return volume; }
}
