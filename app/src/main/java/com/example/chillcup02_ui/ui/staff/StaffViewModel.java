package com.example.chillcup02_ui.ui.staff;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chillcup02_ui.data.repository.OrderRepository;
import com.example.chillcup02_ui.domain.model.Order;

import java.util.ArrayList;
import java.util.List;

public class StaffViewModel extends ViewModel {

    private final OrderRepository orderRepository;

    // Use MutableLiveData to allow us to post changes from within the ViewModel
    private MutableLiveData<List<Order>> ordersLiveData;

    public StaffViewModel() {
        this.orderRepository = new OrderRepository();
        this.ordersLiveData = new MutableLiveData<>();
        // Observe the data from the repository and post it to our own LiveData
        // This decouples our ViewModel from how the repository gets the data
        orderRepository.getOrdersForStore("mock_store_id").observeForever(orders -> {
            ordersLiveData.postValue(orders);
        });
    }

    // The Activity/Fragment will observe this LiveData for UI updates
    public LiveData<List<Order>> getOrders() {
        return ordersLiveData;
    }

    /**
     * Updates the status of a specific order in the current list.
     * This simulates a real-world action for UI development.
     */
    public void updateOrderStatus(String orderId, Order.OrderStatus newStatus) {
        if (ordersLiveData.getValue() == null) {
            return; // No orders loaded yet
        }

        List<Order> currentOrders = new ArrayList<>(ordersLiveData.getValue());
        for (int i = 0; i < currentOrders.size(); i++) {
            Order order = currentOrders.get(i);
            if (order.getId().equals(orderId)) {
                // Create a new Order object with the updated status
                // This is good practice to ensure immutability
                Order updatedOrder = new Order(order.getId(), order.getUserId(), order.getStoreId(),
                        order.getOrderNumber(), order.getItems(), order.getSubtotal(), order.getDeliveryFee(),
                        order.getTax(), order.getTotal(), order.getDeliveryAddress(), order.getPhone(),
                        order.getPaymentMethod(), order.getQrCodeUrl(), order.getDeliveryTime(),
                        newStatus, // The only change is the new status
                        order.getCancelReason(), order.getShipperAssigned(), order.getCreatedAt());

                currentOrders.set(i, updatedOrder);
                break;
            }
        }
        // Post the entire modified list to LiveData to trigger the UI update
        ordersLiveData.postValue(currentOrders);
    }
}
