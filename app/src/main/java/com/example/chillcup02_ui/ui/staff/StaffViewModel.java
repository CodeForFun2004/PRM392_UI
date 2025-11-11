package com.example.chillcup02_ui.ui.staff;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chillcup02_ui.data.repository.OrderRepository;
import com.example.chillcup02_ui.domain.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class StaffViewModel extends AndroidViewModel {

    private final OrderRepository orderRepository;
    private final LiveData<List<Order>> ordersLiveData;
    private final MediatorLiveData<List<Order>> filteredOrdersLiveData = new MediatorLiveData<>();
    private final MutableLiveData<String> filterStatusLiveData = new MutableLiveData<>("All");

    public StaffViewModel(@NonNull Application application) {
        super(application);
        this.orderRepository = OrderRepository.getInstance();
        this.ordersLiveData = orderRepository.getOrders();

        filteredOrdersLiveData.addSource(ordersLiveData, orders -> {
            applyFilter(filterStatusLiveData.getValue(), orders);
        });

        filteredOrdersLiveData.addSource(filterStatusLiveData, status -> {
            applyFilter(status, ordersLiveData.getValue());
        });

        if (this.ordersLiveData.getValue() == null || this.ordersLiveData.getValue().isEmpty()) {
            orderRepository.loadOrdersFromAssets(application.getApplicationContext());
        }
    }

    private void applyFilter(String status, List<Order> orders) {
        if (orders == null) {
            return;
        }
        if (status == null || "All".equalsIgnoreCase(status)) {
            filteredOrdersLiveData.setValue(orders);
        } else {
            List<Order> filteredList = orders.stream()
                    .filter(order -> order.getStatus().name().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
            filteredOrdersLiveData.setValue(filteredList);
        }
    }

    public LiveData<List<Order>> getOrders() {
        return filteredOrdersLiveData;
    }

    public void filterOrdersByStatus(String status) {
        filterStatusLiveData.setValue(status);
    }

    public void updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        orderRepository.updateOrderStatus(orderId, newStatus);
    }
}
