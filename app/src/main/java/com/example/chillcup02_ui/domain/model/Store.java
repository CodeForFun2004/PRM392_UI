package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;

public class Store implements Serializable {
    private String id;
    private String name;
    private String address;
    private String contact;
    private String openHours;
    private boolean isActive;
    private String mapUrl;
    private String image;
    private double latitude;
    private double longitude;
    private String staffId; // The ID of the main staff member

    public Store(String id, String name, String address, String contact, String openHours, boolean isActive, String mapUrl, String image, double latitude, double longitude, String staffId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.openHours = openHours;
        this.isActive = isActive;
        this.mapUrl = mapUrl;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.staffId = staffId;
    }

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
    public String getStaffId() { return staffId; }
}
