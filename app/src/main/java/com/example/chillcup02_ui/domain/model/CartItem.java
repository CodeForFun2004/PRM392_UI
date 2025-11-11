package com.example.chillcup02_ui.domain.model;

public class CartItem {
    public Product product;
    public int qty;
    public int unitPrice;

    public CartItem(Product product, int qty, int unitPrice) {
        this.product = product;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public int getTotal() { return unitPrice * qty; }
}

