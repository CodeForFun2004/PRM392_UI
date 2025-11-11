package com.example.chillcup02_ui.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chillcup02_ui.data.api.ApiClient;
import com.example.chillcup02_ui.data.api.ApiService;
import com.example.chillcup02_ui.data.dto.OrderDto;
import com.example.chillcup02_ui.data.mapper.OrderMapper;
import com.example.chillcup02_ui.domain.model.Order;
import com.example.chillcup02_ui.domain.model.OrderItem;
import com.example.chillcup02_ui.domain.model.OrderItemTopping;
import com.example.chillcup02_ui.domain.model.Topping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepository {

    // We keep these for when we switch back to the real API
    private final ApiService apiService;
    private final OrderMapper orderMapper;

    public OrderRepository() {
        // In a real app with dependency injection, these would be provided.
        this.apiService = ApiClient.getApiService(); // This might fail if ApiClient is not restored
        this.orderMapper = new OrderMapper();
    }

    public LiveData<List<Order>> getOrdersForStore(String storeId) {
        MutableLiveData<List<Order>> ordersLiveData = new MutableLiveData<>();

        // --- MOCK DATA IMPLEMENTATION ---
        // The real API call is commented out. We now create a fake list.
        ordersLiveData.postValue(createMockOrders(storeId));

        /* --- REAL API IMPLEMENTATION ---
        // When you're ready, you can remove the mock data line above and uncomment this.
        apiService.getOrdersForStore(storeId).enqueue(new Callback<List<OrderDto>>() {
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> orders = response.body().stream()
                            .map(orderMapper::toDomainModel)
                            .collect(Collectors.toList());
                    ordersLiveData.postValue(orders);
                } else {
                    ordersLiveData.postValue(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                ordersLiveData.postValue(Collections.emptyList());
            }
        });
        */

        return ordersLiveData;
    }

    // Helper method to generate a list of fake orders for UI development
    private List<Order> createMockOrders(String storeId) {
        List<Order> mockOrders = new ArrayList<>();

        // Mock Order 1: Pending
        List<OrderItem> items1 = new ArrayList<>();
        items1.add(new OrderItem("oi_1", "prod_1", "Caramel Macchiato", "M", Collections.emptyList(), 1, 55000));
        Order order1 = new Order("ord_1", "user_1", storeId, "#ORD-001", items1, 55000, 15000, 5000, 75000,
                "123 Main St", "0912345678", Order.PaymentMethod.COD, null, "25-35 mins",
                Order.OrderStatus.PENDING, null, null, new Date());

        // Mock Order 2: Preparing
        List<OrderItem> items2 = new ArrayList<>();
        List<Topping> toppings2 = new ArrayList<>();
        toppings2.add(new Topping("topping_1", "Chocolate", 5000));
        items2.add(new OrderItem("oi_2", "prod_2", "Americano", "L", toppings2, 1, 45000));
        items2.add(new OrderItem("oi_3", "prod_3", "Croissant", "S", Collections.emptyList(), 2, 30000));
        Order order2 = new Order("ord_2", "user_2", storeId, "#ORD-002", items2, 105000, 15000, 10000, 130000,
                "456 Oak Ave", "0987654321", Order.PaymentMethod.VIETQR, "http://example.com/qr", "20-30 mins",
                Order.OrderStatus.PREPARING, null, null, new Date());

        // Mock Order 3: Ready
        List<OrderItem> items3 = new ArrayList<>();
        items3.add(new OrderItem("oi_4", "prod_4", "Matcha Latte", "M", Collections.emptyList(), 1, 60000));
        Order order3 = new Order("ord_3", "user_3", storeId, "#ORD-003", items3, 60000, 15000, 6000, 81000,
                "789 Pine Ln", "0911223344", Order.PaymentMethod.COD, null, "15-20 mins",
                Order.OrderStatus.READY, null, null, new Date());

        mockOrders.add(order1);
        mockOrders.add(order2);
        mockOrders.add(order3);

        return mockOrders;
    }
}
