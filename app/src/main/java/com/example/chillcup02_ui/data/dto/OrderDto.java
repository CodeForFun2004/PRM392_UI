package com.example.chillcup02_ui.data.dto;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderDto {

    @SerializedName("_id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("storeId")
    private String storeId;
    @SerializedName("orderNumber")
    private String orderNumber;
    @SerializedName("items")
    private List<OrderItemDto> items;
    @SerializedName("subtotal")
    private double subtotal;
    @SerializedName("deliveryFee")
    private double deliveryFee;
    @SerializedName("tax")
    private double tax;
    @SerializedName("total")
    private double total;
    @SerializedName("deliveryAddress")
    private String deliveryAddress;
    @SerializedName("phone")
    private String phone;
    @SerializedName("paymentMethod")
    private String paymentMethod;
    @SerializedName("qrCodeUrl")
    private String qrCodeUrl;
    @SerializedName("deliveryTime")
    private String deliveryTime;
    @SerializedName("status")
    private String status;
    @SerializedName("cancelReason")
    private String cancelReason;
    @SerializedName("shipperAssigned")
    private String shipperAssigned;
    @SerializedName("createdAt")
    private String createdAt; // Sent as ISO date string

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getStoreId() { return storeId; }
    public String getOrderNumber() { return orderNumber; }
    public List<OrderItemDto> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getTax() { return tax; }
    public double getTotal() { return total; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getPhone() { return phone; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getQrCodeUrl() { return qrCodeUrl; }
    public String getDeliveryTime() { return deliveryTime; }
    public String getStatus() { return status; }
    public String getCancelReason() { return cancelReason; }
    public String getShipperAssigned() { return shipperAssigned; }
    public String getCreatedAt() { return createdAt; }
}
