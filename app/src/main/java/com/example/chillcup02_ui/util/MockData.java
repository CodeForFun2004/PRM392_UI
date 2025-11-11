package com.example.chillcup02_ui.util;

import com.example.chillcup02_ui.domain.model.CartItem;
import com.example.chillcup02_ui.domain.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MockData {
    public static List<CartItem> getMockCartItems() {
        List<CartItem> list = new ArrayList<>();
        Product p1 = new Product("p1", "Iced Latte", 45, "");
        Product p2 = new Product("p2", "Caramel Frappe", 55, ""); // sửa chính tả nếu muốn

        list.add(new CartItem(p1, 2, p1.getBasePrice()));
        list.add(new CartItem(p2, 1, p2.getBasePrice()));
        return list;
    }
}

