package com.example.chillcup02_ui.ui.staff.order;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;
// Corrected import path
import com.example.chillcup02_ui.ui.staff.order.OrderItemAdapter;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        if (getIntent().hasExtra("ORDER_EXTRA")) {
            order = (Order) getIntent().getSerializableExtra("ORDER_EXTRA");
        }

        if (order != null) {
            populateOrderDetails();
        }
    }

    private void populateOrderDetails() {
        TextView tvOrderId = findViewById(R.id.tvOrderId);
        TextView tvOrderDate = findViewById(R.id.tvOrderDate);
        TextView tvOrderStatus = findViewById(R.id.tvOrderStatus);
        TextView tvCustomerName = findViewById(R.id.tvCustomerName);
        TextView tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        TextView tvCustomerAddress = findViewById(R.id.tvCustomerAddress);
        TextView tvOrderTotal = findViewById(R.id.tvOrderTotal);

        tvOrderId.setText("Order #" + order.getOrderNumber());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvOrderDate.setText("Date: " + sdf.format(order.getCreatedAt()));
        tvOrderStatus.setText("Status: " + order.getStatus().toString());
        tvCustomerName.setText("Name: John Doe"); // Mock data
        tvCustomerPhone.setText("Phone: 0123456789"); // Mock data
        tvCustomerAddress.setText("Address: " + order.getDeliveryAddress());
        tvOrderTotal.setText(String.format(Locale.getDefault(), "Total: %,.0f VND", order.getTotal()));

        RecyclerView rvOrderItems = findViewById(R.id.rvOrderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderItemAdapter adapter = new OrderItemAdapter(order.getItems());
        rvOrderItems.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
