package com.example.chillcup02_ui.network.model;

import java.util.List;

public class OrderResponse {
    public String message;
    public Order order;

    public static class Order {
        public String _id;
        public String orderNumber;
        public String status;
        public String deliveryAddress;
        public String phone;
        public String paymentMethod;
        public double total;
        public List<OrderRequest.Item> items;
    }
}
