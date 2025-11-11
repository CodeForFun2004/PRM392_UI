package com.example.chillcup02_ui.ui.staff.order;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;
import com.example.chillcup02_ui.ui.staff.StaffViewModel;

import java.util.Collections;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;
    private StaffViewModel staffViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Get the shared ViewModel
        staffViewModel = new ViewModelProvider(this).get(StaffViewModel.class);

        if (getIntent().hasExtra("ORDER_EXTRA")) {
            order = (Order) getIntent().getSerializableExtra("ORDER_EXTRA");
        } else {
            Toast.makeText(this, "Error: Order not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bindOrderData();
    }

    private void bindOrderData() {
        TextView tvDetailOrderNumber = findViewById(R.id.tvDetailOrderNumber);
        TextView tvDetailStatus = findViewById(R.id.tvDetailStatus);
        TextView tvDetailCustomerName = findViewById(R.id.tvDetailCustomerName);
        TextView tvDetailAddress = findViewById(R.id.tvDetailAddress);
        TextView tvDetailPhone = findViewById(R.id.tvDetailPhone);
        TextView tvDetailSubtotal = findViewById(R.id.tvDetailSubtotal);
        TextView tvDetailDeliveryFee = findViewById(R.id.tvDetailDeliveryFee);
        TextView tvDetailTotal = findViewById(R.id.tvDetailTotal);
        RecyclerView rvOrderItems = findViewById(R.id.rvOrderItems);
        Button btnAdvanceStatus = findViewById(R.id.btnAdvanceStatus);
        Button btnCancelOrder = findViewById(R.id.btnCancelOrder);

        tvDetailOrderNumber.setText(order.getOrderNumber());
        tvDetailStatus.setText(order.getStatus().toString());
        tvDetailCustomerName.setText("Customer Name Placeholder");
        tvDetailAddress.setText(order.getDeliveryAddress());
        tvDetailPhone.setText(order.getPhone());

        tvDetailSubtotal.setText(String.format("%,.0f VND", order.getSubtotal()));
        tvDetailDeliveryFee.setText(String.format("%,.0f VND", order.getDeliveryFee()));
        tvDetailTotal.setText(String.format("%,.0f VND", order.getTotal()));

        OrderItemAdapter itemsAdapter = new OrderItemAdapter(order.getItems() != null ? order.getItems() : Collections.emptyList());
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(itemsAdapter);

        // --- Set up button actions ---
        setupActionButtons(btnAdvanceStatus, btnCancelOrder);
    }

    private void setupActionButtons(Button btnAdvanceStatus, Button btnCancelOrder) {
        List<Order.OrderStatus> nextStatuses = getNextPossibleStatuses(order.getStatus());

        // Only show the advance status button if there is a next logical status
        if (!nextStatuses.isEmpty() && nextStatuses.get(0) != Order.OrderStatus.CANCELLED) {
            btnAdvanceStatus.setVisibility(View.VISIBLE);
            Order.OrderStatus nextStatus = nextStatuses.get(0);
            btnAdvanceStatus.setText("Advance to " + nextStatus.toString());
            btnAdvanceStatus.setOnClickListener(v -> {
                staffViewModel.updateOrderStatus(order.getId(), nextStatus);
                Toast.makeText(this, "Order updated to " + nextStatus, Toast.LENGTH_SHORT).show();
                finish(); // Go back to the order list
            });
        } else {
            btnAdvanceStatus.setVisibility(View.GONE);
        }

        // Allow cancellation unless the order is already completed or cancelled
        if (order.getStatus() != Order.OrderStatus.COMPLETED && order.getStatus() != Order.OrderStatus.CANCELLED) {
            btnCancelOrder.setVisibility(View.VISIBLE);
            btnCancelOrder.setOnClickListener(v -> {
                staffViewModel.updateOrderStatus(order.getId(), Order.OrderStatus.CANCELLED);
                Toast.makeText(this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                finish(); // Go back to the order list
            });
        } else {
            btnCancelOrder.setVisibility(View.GONE);
        }
    }
    
    // This helper method can be expanded later
    private List<Order.OrderStatus> getNextPossibleStatuses(Order.OrderStatus currentStatus) {
        switch (currentStatus) {
            case PENDING: return Collections.singletonList(Order.OrderStatus.PROCESSING);
            case PROCESSING: return Collections.singletonList(Order.OrderStatus.PREPARING);
            case PREPARING: return Collections.singletonList(Order.OrderStatus.READY);
            case READY: return Collections.singletonList(Order.OrderStatus.DELIVERING);
            case DELIVERING: return Collections.singletonList(Order.OrderStatus.COMPLETED);
            default: return Collections.emptyList();
        }
    }
}
