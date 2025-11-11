package com.example.chillcup02_ui.ui.staff;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.chillcup02_ui.data.repository.OrderRepository;
import com.example.chillcup02_ui.domain.model.Order;

import java.util.List;

public class StaffViewModel extends AndroidViewModel {

    private final OrderRepository orderRepository;
    private final LiveData<List<Order>> ordersLiveData;

    public StaffViewModel(@NonNull Application application) {
        super(application);
        // Get the singleton instance of the repository
        this.orderRepository = OrderRepository.getInstance();
        this.ordersLiveData = orderRepository.getOrders();

        // Load initial data only if the repository is empty
        if (this.ordersLiveData.getValue() == null || this.ordersLiveData.getValue().isEmpty()) {
            orderRepository.loadOrdersFromAssets(application.getApplicationContext());
        }
    }

    public LiveData<List<Order>> getOrders() {
        return ordersLiveData;
    }

    /**
     * Passes the update request to the central repository.
     * The repository will handle the update and notify all observers.
     */
    public void updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        orderRepository.updateOrderStatus(orderId, newStatus);
    }
}
