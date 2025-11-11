package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;

public class StoreDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("contact")
    private String contact;
    @SerializedName("openHours")
    private String openHours;
    @SerializedName("isActive")
    private boolean isActive;
    @SerializedName("mapUrl")
    private String mapUrl;
    @SerializedName("image")
    private String image;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("staff")
    private String staff; // The staff is sent as an ObjectId string

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getContact() { return contact; }
    public String getOpenHours() { return openHours; }
    public boolean isActive() { return isActive; }
    public String getMapUrl() { return mapUrl; }
    public String getImage() { return image; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getStaff() { return staff; }
}
