package com.example.chillcup02_ui.domain.model;

public class Size {
    private String id;
    private String size;
    private String name;
    private double multiplier;
    private String volume;

    public Size() {}

    public Size(String id, String size, String name, double multiplier, String volume) {
        this.id = id;
        this.size = size;
        this.name = name;
        this.multiplier = multiplier;
        this.volume = volume;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getMultiplier() { return multiplier; }
    public void setMultiplier(double multiplier) { this.multiplier = multiplier; }

    public String getVolume() { return volume; }
    public void setVolume(String volume) { this.volume = volume; }
}
