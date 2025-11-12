package com.example.chillcup02_ui.util;

import com.example.chillcup02_ui.domain.model.CartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple in-memory cart manager for UI mock flow.
 */
public class CartManager {
    private static CartManager instance;
    private final List<CartItem> items = new ArrayList<>();
    private String appliedPromo = null;

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem item) {
        // if same product id exists, increase qty
        for (CartItem it : items) {
            if (it.product != null && item.product != null && it.product.id.equals(item.product.id)) {
                it.qty += item.qty;
                return;
            }
        }
        items.add(item);
    }

    public void clear() {
        items.clear();
        appliedPromo = null;
    }

    public void setAppliedPromo(String code) { this.appliedPromo = code; }
    public String getAppliedPromo() { return appliedPromo; }

    public int getSubtotal() {
        int subtotal = 0;
        for (CartItem it : items) subtotal += it.getTotal();
        return subtotal;
    }

    public int getDeliveryFee() {
        return items.isEmpty() ? 0 : 5;
    }

    public int getDiscountAmount() {
        if (appliedPromo != null && appliedPromo.equalsIgnoreCase("DISCOUNT10")) {
            return (int) Math.round(getSubtotal() * 0.10);
        }
        return 0;
    }

    public int getTotal() {
        return getSubtotal() + getDeliveryFee() - getDiscountAmount();
    }
}
