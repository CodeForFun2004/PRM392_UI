package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;

public class CategoryDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("category")
    private String category;
    @SerializedName("icon")
    private String icon;
    @SerializedName("description")
    private String description;

    // Getters
    public String getId() { return id; }
    public String getCategory() { return category; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
}
