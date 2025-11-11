package com.example.chillcup02_ui.network.model;

import java.util.List;

public class OrderRequest {
    public String deliveryAddress;
    public String phone;
    public String paymentMethod;
    public List<Item> items;
    public double total;

    public static class Item {
        public String name;
        public int quantity;
        public double price;
    }
}
