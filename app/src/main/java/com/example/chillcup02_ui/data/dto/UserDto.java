package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;

public class UserDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("address")
    private String address;
    @SerializedName("role")
    private String role;
    @SerializedName("staffId")
    private String staffId;
    @SerializedName("status")
    private String status;
    @SerializedName("isBanned")
    private boolean isBanned;
    @SerializedName("banReason")
    private String banReason;
    @SerializedName("banExpires")
    private String banExpires; // Sent as ISO date string
    @SerializedName("googleId")
    private String googleId;
    @SerializedName("storeId")
    private String storeId;

    // Getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAvatar() { return avatar; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
    public String getStaffId() { return staffId; }
    public String getStatus() { return status; }
    public boolean isBanned() { return isBanned; }
    public String getBanReason() { return banReason; }
    public String getBanExpires() { return banExpires; }
    public String getGoogleId() { return googleId; }
    public String getStoreId() { return storeId; }
}
