package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    private String id;
    private String userId;
    private String storeId;
    private String orderNumber;
    private List<OrderItem> items;
    private double subtotal;
    private double deliveryFee;
    private double tax;
    private double total;
    private String deliveryAddress;
    private String phone;
    private PaymentMethod paymentMethod;
    private String qrCodeUrl;
    private String deliveryTime;
    private OrderStatus status;
    private String cancelReason;
    private String shipperAssigned;
    private Date createdAt;

    public enum OrderStatus {
        PENDING, PROCESSING, PREPARING, READY, DELIVERING, COMPLETED, CANCELLED
    }

    public enum PaymentMethod {
        COD, VIETQR
    }

    public Order(String id, String userId, String storeId, String orderNumber, List<OrderItem> items, double subtotal, double deliveryFee, double tax, double total, String deliveryAddress, String phone, PaymentMethod paymentMethod, String qrCodeUrl, String deliveryTime, OrderStatus status, String cancelReason, String shipperAssigned, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.storeId = storeId;
        this.orderNumber = orderNumber;
        this.items = items;
        this.subtotal = subtotal;
        this.deliveryFee = deliveryFee;
        this.tax = tax;
        this.total = total;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.qrCodeUrl = qrCodeUrl;
        this.deliveryTime = deliveryTime;
        this.status = status;
        this.cancelReason = cancelReason;
        this.shipperAssigned = shipperAssigned;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getStoreId() { return storeId; }
    public String getOrderNumber() { return orderNumber; }
    public List<OrderItem> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
    public double getDeliveryFee() { return deliveryFee; }
    public double getTax() { return tax; }
    public double getTotal() { return total; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getPhone() { return phone; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public String getQrCodeUrl() { return qrCodeUrl; }
    public String getDeliveryTime() { return deliveryTime; }
    public OrderStatus getStatus() { return status; }
    public String getCancelReason() { return cancelReason; }
    public String getShipperAssigned() { return shipperAssigned; }
    public Date getCreatedAt() { return createdAt; }
}
