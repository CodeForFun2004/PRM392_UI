package com.example.chillcup02_ui.domain.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    public String _id;
    public String orderNumber;
    public String createdAt; // ISO
    public String status;
    public String paymentMethod;
    public int total;
    public List<OrderItem> items;
    public String deliveryTime;
    public String deliveryAddress;
    public String phone;
    public List<RefundRequest> refundRequests;

    public static class OrderItem implements Serializable {
        public String name;
        public int quantity;
        public int price;
    }

    public static class RefundRequest implements Serializable {
        public String status;
        public String note;
    }
}
