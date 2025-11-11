package com.example.chillcup02_ui.ui.staff;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chillcup02_ui.data.repository.OrderRepository;
import com.example.chillcup02_ui.domain.model.Order;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StaffViewModel extends AndroidViewModel {

    private final OrderRepository orderRepository;
    private final LiveData<List<Order>> sourceOrders;

    // LiveData for the filtered order list
    private final MediatorLiveData<List<Order>> filteredOrdersLiveData = new MediatorLiveData<>();
    private final MutableLiveData<String> currentFilter = new MutableLiveData<>("All Statuses");

    // LiveData for Dashboard Metrics
    private final MutableLiveData<Double> totalRevenue = new MutableLiveData<>(0.0);
    private final MutableLiveData<Integer> pendingOrdersCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> totalOrdersCount = new MutableLiveData<>(0);
    private final MutableLiveData<List<Order>> recentOrders = new MutableLiveData<>();

    public StaffViewModel(@NonNull Application application) {
        super(application);
        this.orderRepository = OrderRepository.getInstance();
        this.sourceOrders = orderRepository.getOrders();

        filteredOrdersLiveData.addSource(sourceOrders, orders -> {
            calculateDashboardMetrics(orders);
            applyFilter(orders, currentFilter.getValue());
        });

        filteredOrdersLiveData.addSource(currentFilter, filter -> {
            applyFilter(sourceOrders.getValue(), filter);
        });

        if (this.sourceOrders.getValue() == null || this.sourceOrders.getValue().isEmpty()) {
            forceRefresh();
        }
    }

    // --- Getters for UI to Observe ---
    public LiveData<List<Order>> getFilteredOrders() { return filteredOrdersLiveData; }
    public LiveData<Double> getTotalRevenue() { return totalRevenue; }
    public LiveData<Integer> getPendingOrdersCount() { return pendingOrdersCount; }
    public LiveData<Integer> getTotalOrdersCount() { return totalOrdersCount; }
    public LiveData<List<Order>> getRecentOrders() { return recentOrders; }

    // --- Actions from UI ---
    public void forceRefresh() {
        orderRepository.loadOrdersFromAssets(getApplication().getApplicationContext());
    }

    public void setFilter(@NonNull String status) {
        currentFilter.setValue(status);
    }

    public void updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        orderRepository.updateOrderStatus(orderId, newStatus);
    }

    // --- Private Logic ---
    private void calculateDashboardMetrics(List<Order> orders) {
        if (orders == null) return;

        List<Order> sortedOrders = orders.stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .collect(Collectors.toList());

        // Limit to 3 recent orders for the dashboard
        recentOrders.setValue(sortedOrders.stream().limit(3).collect(Collectors.toList()));

        double revenue = sortedOrders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.COMPLETED)
                .mapToDouble(Order::getTotal)
                .sum();
        totalRevenue.setValue(revenue);

        long pendingCount = sortedOrders.stream()
                .filter(o -> o.getStatus() == Order.OrderStatus.PENDING)
                .count();
        pendingOrdersCount.setValue((int) pendingCount);

        totalOrdersCount.setValue(sortedOrders.size());
    }

    private void applyFilter(List<Order> orders, String filter) {
        if (orders == null) return;
        if (filter == null || filter.equalsIgnoreCase("All Statuses")) {
            filteredOrdersLiveData.setValue(orders);
        } else {
            List<Order> filteredList = orders.stream()
                    .filter(order -> order.getStatus().toString().equalsIgnoreCase(filter))
                    .collect(Collectors.toList());
            filteredOrdersLiveData.setValue(filteredList);
        }
    }
}
