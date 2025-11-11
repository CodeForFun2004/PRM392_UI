package com.example.chillcup02_ui.util;

import com.example.chillcup02_ui.domain.model.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockOrders {
    private static final List<Order> ORDERS = new ArrayList<>();

    static {
        // seed with a couple of orders
        Order o1 = new Order();
        o1._id = "1";
        o1.orderNumber = "ORD-1001";
        o1.createdAt = "2025-11-10T10:00:00Z";
        o1.status = "processing";
        o1.total = 145;
        Order.OrderItem oi1 = new Order.OrderItem(); oi1.name = "Iced Latte"; oi1.quantity = 2; oi1.price = 45;
        Order.OrderItem oi2 = new Order.OrderItem(); oi2.name = "Caramel Frapp"; oi2.quantity = 1; oi2.price = 55;
        o1.items = Arrays.asList(oi1, oi2);
        o1.deliveryTime = "30-40 mins";
        o1.deliveryAddress = "123 Main Street";
        o1.phone = "+84900000000";

        Order o2 = new Order();
        o2._id = "2";
        o2.orderNumber = "ORD-1002";
        o2.createdAt = "2025-11-09T12:30:00Z";
        o2.status = "completed";
        o2.total = 80;
        Order.OrderItem oi3 = new Order.OrderItem(); oi3.name = "Espresso"; oi3.quantity = 1; oi3.price = 30;
        Order.OrderItem oi4 = new Order.OrderItem(); oi4.name = "Croissant"; oi4.quantity = 1; oi4.price = 50;
        o2.items = Arrays.asList(oi3, oi4);
        o2.deliveryTime = null;
        o2.deliveryAddress = "456 Second Ave";
        o2.phone = "+84911111111";

        ORDERS.add(o1);
        ORDERS.add(o2);
        // add more statuses for review
        Order o3 = new Order();
        o3._id = "3";
        o3.orderNumber = "ORD-1003";
        o3.createdAt = "2025-11-10T11:15:00Z";
        o3.status = "delivering";
        o3.total = 120;
        Order.OrderItem oi5 = new Order.OrderItem(); oi5.name = "Cappuccino"; oi5.quantity = 1; oi5.price = 40;
        Order.OrderItem oi6 = new Order.OrderItem(); oi6.name = "Bagel"; oi6.quantity = 2; oi6.price = 40;
        o3.items = Arrays.asList(oi5, oi6);
        o3.deliveryTime = "15-25 mins";
        o3.deliveryAddress = "789 Third Blvd";
        o3.phone = "+84922222222";

        Order o4 = new Order();
        o4._id = "4";
        o4.orderNumber = "ORD-1004";
        o4.createdAt = "2025-11-08T09:00:00Z";
        o4.status = "cancelled";
        o4.total = 60;
        Order.OrderItem oi7 = new Order.OrderItem(); oi7.name = "Americano"; oi7.quantity = 1; oi7.price = 30;
        Order.OrderItem oi8 = new Order.OrderItem(); oi8.name = "Muffin"; oi8.quantity = 1; oi8.price = 30;
        o4.items = Arrays.asList(oi7, oi8);
        o4.deliveryTime = null;
        o4.deliveryAddress = "321 Fourth St";
        o4.phone = "+84933333333";

        Order o5 = new Order();
        o5._id = "5";
        o5.orderNumber = "ORD-1005";
        o5.createdAt = "2025-11-07T14:20:00Z";
        o5.status = "refunded";
        o5.total = 200;
        Order.OrderItem oi9 = new Order.OrderItem(); oi9.name = "Signature Latte"; oi9.quantity = 2; oi9.price = 100;
        o5.items = Arrays.asList(oi9);
        o5.deliveryTime = null;
        o5.deliveryAddress = "555 Fifth Ave";
        o5.phone = "+84944444444";
        Order.RefundRequest rr = new Order.RefundRequest(); rr.status = "processed"; rr.note = "Refund approved";
        o5.refundRequests = Arrays.asList(rr);

        ORDERS.add(o3);
        ORDERS.add(o4);
        ORDERS.add(o5);
    }

    public static List<Order> getOrders() {
        return ORDERS;
    }

    public static void addOrder(Order o) {
        // generate simple id if missing
        if (o._id == null) o._id = String.valueOf(System.currentTimeMillis());
        ORDERS.add(0, o); // newest first
    }

    public static Order getOrderById(String id) {
        if (id == null) return null;
        for (Order o : ORDERS) {
            if (id.equals(o._id) || id.equals(o.orderNumber)) return o;
        }
        return null;
    }
}
