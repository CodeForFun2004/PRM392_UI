package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;

public class Category implements Serializable {
    private String id;
    private String name;
    private String icon;
    private String description;

    public Category(String id, String name, String icon, String description) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
}
