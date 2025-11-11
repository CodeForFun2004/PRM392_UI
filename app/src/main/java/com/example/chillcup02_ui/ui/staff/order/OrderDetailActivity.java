package com.example.chillcup02_ui.ui.staff.order;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Get the Order object passed from the previous fragment
        if (getIntent().hasExtra("ORDER_EXTRA")) {
            order = (Order) getIntent().getSerializableExtra("ORDER_EXTRA");
        } else {
            // If no order is found, show an error and finish the activity
            Toast.makeText(this, "Error: Order not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Bind the data from the Order object to the views
        bindOrderData();
    }

    private void bindOrderData() {
        // Find all the views from the layout
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

        // Set the text for each view
        tvDetailOrderNumber.setText(order.getOrderNumber());
        tvDetailStatus.setText(order.getStatus().toString());
        tvDetailCustomerName.setText("Customer Name Placeholder"); // You would get this from a UserRepository
        tvDetailAddress.setText(order.getDeliveryAddress());
        tvDetailPhone.setText(order.getPhone());

        tvDetailSubtotal.setText(String.format("%,.0f VND", order.getSubtotal()));
        tvDetailDeliveryFee.setText(String.format("%,.0f VND", order.getDeliveryFee()));
        tvDetailTotal.setText(String.format("%,.0f VND", order.getTotal()));

        // Setup the RecyclerView for the order items
        OrderItemAdapter itemsAdapter = new OrderItemAdapter(order.getItems());
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(itemsAdapter);

        // We can add click listeners to btnAdvanceStatus and btnCancelOrder later
    }
}
