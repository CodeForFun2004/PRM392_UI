package com.example.chillcup02_ui.ui.orders;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Order order = null;
        if (getIntent() != null && getIntent().hasExtra("order")) {
            order = (Order) getIntent().getSerializableExtra("order");
        }

        if (order == null) {
            finish();
            return;
        }

        // populate views directly in activity layout
        android.widget.TextView tvNumber = findViewById(R.id.tvDetailOrderNumber);
        android.widget.TextView tvDate = findViewById(R.id.tvDetailDate);
        android.widget.TextView tvStatus = findViewById(R.id.tvDetailStatus);
        android.widget.TextView tvDeliveryTime = findViewById(R.id.tvDetailDeliveryTime);
        android.widget.LinearLayout llItems = findViewById(R.id.llItemsContainer);
        android.widget.TextView tvSubtotal = findViewById(R.id.tvSubtotal);
        android.widget.TextView tvDelivery = findViewById(R.id.tvDelivery);
        android.widget.TextView tvDiscount = findViewById(R.id.tvDiscount);
        android.widget.TextView tvTotal = findViewById(R.id.tvTotal);
        android.widget.TextView tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        android.widget.TextView tvAddress = findViewById(R.id.tvAddress);
        android.widget.TextView tvPhone = findViewById(R.id.tvPhone);
        android.widget.Button btnBack = findViewById(R.id.btnBackHome);
        android.widget.Button btnTrack = findViewById(R.id.btnTrackOrder);

        // set fields
        tvNumber.setText(order.orderNumber != null ? order.orderNumber : "-");
        // format createdAt to 'dd/MM/yyyy at HH:mm'
        if (order.createdAt != null && !order.createdAt.isEmpty()) {
            try {
                java.time.Instant inst = java.time.Instant.parse(order.createdAt);
                java.time.ZonedDateTime zdt = inst.atZone(java.time.ZoneId.systemDefault());
                java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' HH:mm");
                tvDate.setText(zdt.format(fmt));
            } catch (Exception e) {
                tvDate.setText(order.createdAt.replace('T',' ').replace("Z",""));
            }
        } else {
            tvDate.setText("");
        }

        tvStatus.setText(order.status != null ? order.status : "");
        tvDeliveryTime.setText(order.deliveryTime != null ? ("Est. delivery: " + order.deliveryTime) : "");

        // items
        llItems.removeAllViews();
        int subtotal = 0;
        if (order.items != null) {
            for (com.example.chillcup02_ui.domain.model.Order.OrderItem it : order.items) {
                android.view.View row = android.view.LayoutInflater.from(this).inflate(R.layout.item_order_detail_row, llItems, false);
                android.widget.TextView tvName = row.findViewById(R.id.tvItemName);
                android.widget.TextView tvQty = row.findViewById(R.id.tvItemQty);
                android.widget.TextView tvPrice = row.findViewById(R.id.tvItemPrice);
                android.widget.TextView tvSize = row.findViewById(R.id.tvItemSize);
                android.widget.ImageView ivImg = row.findViewById(R.id.ivItemImg);
                tvName.setText(it.name);
                tvQty.setText("Qty: " + it.quantity);
                tvPrice.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(it.price));
                // size not available in model; hide by default
                if (tvSize != null) tvSize.setVisibility(android.view.View.GONE);
                // placeholder image
                if (ivImg != null) ivImg.setImageResource(R.drawable.ic_launcher_background);
                llItems.addView(row);
                subtotal += it.price * it.quantity;
            }
        }

        tvSubtotal.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(subtotal));
        int deliveryFee = order.deliveryTime != null ? 10000 : 0;
        int discount = 0;
        tvDelivery.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(deliveryFee));
        tvDiscount.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(discount));
        tvTotal.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(order.total));
        if (order.paymentMethod != null) {
            String pm = order.paymentMethod.toLowerCase();
            if (pm.contains("cod")) tvPaymentMethod.setText("Cash on Delivery (COD)");
            else tvPaymentMethod.setText(order.paymentMethod);
        } else tvPaymentMethod.setText("Cash on Delivery (COD)");
        tvAddress.setText(order.deliveryAddress != null ? order.deliveryAddress : "");
        tvPhone.setText(order.phone != null ? order.phone : "");

    // button actions
    final Order orderFinal = order;
        btnBack.setOnClickListener(v -> {
            // go to main
            android.content.Intent it = new android.content.Intent(this, com.example.chillcup02_ui.MainActivity.class);
            it.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
            finish();
        });

        btnTrack.setOnClickListener(v -> {
            // open tracking activity to show full-screen tracking
            android.content.Intent it = new android.content.Intent(this, com.example.chillcup02_ui.ui.orders.OrderTrackingActivity.class);
            it.putExtra("order", orderFinal);
            startActivity(it);
        });
    }
}
