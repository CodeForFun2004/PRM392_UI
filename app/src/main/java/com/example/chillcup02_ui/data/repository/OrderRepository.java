package com.example.chillcup02_ui.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chillcup02_ui.data.dto.OrderDto;
import com.example.chillcup02_ui.data.mapper.OrderMapper;
import com.example.chillcup02_ui.domain.model.Order;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Singleton Pattern: To ensure a single source of truth for order data.
public class OrderRepository {

    private static volatile OrderRepository instance;
    private final OrderMapper orderMapper = new OrderMapper();
    
    // Use LiveData to hold the list of orders, allowing automatic UI updates.
    private final MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>(new ArrayList<>());

    // Private constructor to prevent direct instantiation.
    private OrderRepository() {}

    public static OrderRepository getInstance() {
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository();
                }
            }
        }
        return instance;
    }

    // Load data from assets. Should only be called once.
    public void loadOrdersFromAssets(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("mock_orders.json");
            InputStreamReader reader = new InputStreamReader(inputStream);
            Type listType = new TypeToken<List<OrderDto>>() {}.getType();
            List<OrderDto> orderDtos = new Gson().fromJson(reader, listType);

            List<Order> orders = orderDtos.stream()
                    .map(orderMapper::toDomainModel)
                    .collect(Collectors.toList());
            ordersLiveData.postValue(orders);

        } catch (IOException e) {
            e.printStackTrace();
            ordersLiveData.postValue(Collections.emptyList());
        }
    }

    // Public method for ViewModels to get observable order data.
    public LiveData<List<Order>> getOrders() {
        return ordersLiveData;
    }

    // --- CRUD Operations ---

    /**
     * Updates the status of a specific order.
     * This simulates a PUT/PATCH request to a real API.
     */
    public void updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        List<Order> currentOrders = new ArrayList<>(ordersLiveData.getValue());
        
        List<Order> updatedOrders = currentOrders.stream()
                .map(order -> {
                    if (order.getId().equals(orderId)) {
                        // Create a new Order object with the updated status
                        return new Order(order.getId(), order.getUserId(), order.getStoreId(), order.getOrderNumber(),
                                order.getItems(), order.getSubtotal(), order.getDeliveryFee(), order.getTax(),
                                order.getTotal(), order.getDeliveryAddress(), order.getPhone(), order.getPaymentMethod(),
                                order.getQrCodeUrl(), order.getDeliveryTime(), newStatus, order.getCancelReason(),
                                order.getShipperAssigned(), order.getCreatedAt());
                    }
                    return order;
                })
                .collect(Collectors.toList());

        // Post the updated list to LiveData to notify observers.
        ordersLiveData.postValue(updatedOrders);
    }
}
