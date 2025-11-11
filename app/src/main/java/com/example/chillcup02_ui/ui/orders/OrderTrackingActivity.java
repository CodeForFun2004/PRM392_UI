package com.example.chillcup02_ui.ui.orders;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;

public class OrderTrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        Order order = null;
        if (getIntent() != null && getIntent().hasExtra("order")) {
            order = (Order) getIntent().getSerializableExtra("order");
        }

        if (order == null) {
            finish();
            return;
        }

        OrderTrackingFragment frag = OrderTrackingFragment.newInstance(order);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerTracking, frag)
                .commit();
    }
}
