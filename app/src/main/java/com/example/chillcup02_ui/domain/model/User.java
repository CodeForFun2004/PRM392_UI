package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private UserRole role;
    private String staffId;
    private StaffStatus status;
    private boolean isBanned;
    private String banReason;
    private Date banExpires;
    private String googleId;
    private String storeId;

    public enum UserRole {
        CUSTOMER, ADMIN, STAFF, SHIPPER
    }

    public enum StaffStatus {
        AVAILABLE, ASSIGNED
    }

    public User(String id, String username, String fullname, String email, String phone, String avatar, String address, UserRole role, String staffId, StaffStatus status, boolean isBanned, String banReason, Date banExpires, String googleId, String storeId) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.address = address;
        this.role = role;
        this.staffId = staffId;
        this.status = status;
        this.isBanned = isBanned;
        this.banReason = banReason;
        this.banExpires = banExpires;
        this.googleId = googleId;
        this.storeId = storeId;
    }

    // Getters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getFullname() { return fullname; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAvatar() { return avatar; }
    public String getAddress() { return address; }
    public UserRole getRole() { return role; }
    public String getStaffId() { return staffId; }
    public StaffStatus getStatus() { return status; }
    public boolean isBanned() { return isBanned; }
    public String getBanReason() { return banReason; }
    public Date getBanExpires() { return banExpires; }
    public String getGoogleId() { return googleId; }
    public String getStoreId() { return storeId; }
}
