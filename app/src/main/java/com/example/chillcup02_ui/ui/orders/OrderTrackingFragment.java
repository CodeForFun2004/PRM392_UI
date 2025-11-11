package com.example.chillcup02_ui.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;

public class OrderTrackingFragment extends Fragment {

    private static final String ARG_ORDER = "arg_order";
    private Order order;

    public static OrderTrackingFragment newInstance(Order order) {
        OrderTrackingFragment f = new OrderTrackingFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_ORDER, order);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) order = (Order) getArguments().getSerializable(ARG_ORDER);

        TextView tvTitle = view.findViewById(R.id.tvTrackingTitle);
        tvTitle.setText(order != null ? order.orderNumber : "Order");

        // delivery/info and total views
        TextView tvAddr = view.findViewById(R.id.tvTrackingAddress);
        TextView tvPhone = view.findViewById(R.id.tvTrackingPhone);
        TextView tvTotal = view.findViewById(R.id.tvTrackingTotal);
        android.widget.Button btnView = view.findViewById(R.id.btnViewOrderDetails);
        if (order != null) {
            tvAddr.setText(order.deliveryAddress != null ? order.deliveryAddress : "");
            tvPhone.setText(order.phone != null ? order.phone : "");
            tvTotal.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(order.total));
        }

        // build simple step UI using new icons/backgrounds
        android.widget.LinearLayout container = view.findViewById(R.id.llTrackingSteps);
        container.removeAllViews();
        if (order != null) {
            java.util.List<String> steps = new java.util.ArrayList<>();
            steps.add("Order Confirmed");
            steps.add("Preparing");
            steps.add("Ready");
            steps.add("Out for Delivery");
            steps.add("Delivered");

            // determine index based on status
            int currentIndex = 0;
            if (order.status != null) {
                switch (order.status.toLowerCase()) {
                    case "processing":
                    case "preparing": currentIndex = 1; break;
                    case "ready": currentIndex = 2; break;
                    case "delivering": currentIndex = 3; break;
                    case "completed": currentIndex = 4; break;
                    case "cancelled": currentIndex = -1; break;
                }
            }

            for (int i = 0; i < steps.size(); i++) {
                View row = LayoutInflater.from(requireContext()).inflate(R.layout.item_tracking_step, container, false);
                TextView tvStepTitle = row.findViewById(R.id.tvStepTitle);
                TextView tvStepDesc = row.findViewById(R.id.tvStepDesc);
                TextView tvStepTime = row.findViewById(R.id.tvStepTime);
                android.widget.ImageView ivStepBg = row.findViewById(R.id.ivStepBg);
                android.widget.ImageView ivStepIcon = row.findViewById(R.id.ivStepIcon);
                View vLine = row.findViewById(R.id.viewLine);

                tvStepTitle.setText(steps.get(i));
                boolean done = i <= currentIndex && currentIndex >= 0;
                tvStepDesc.setText(done ? "Hoàn thành" : "Chưa xử lý");

                if (done) {
                    ivStepBg.setImageResource(R.drawable.circle_active);
                    ivStepIcon.setImageResource(R.drawable.ic_check_circle_green);
                    ivStepIcon.setColorFilter(android.graphics.Color.WHITE);
                    tvStepTime.setVisibility(View.VISIBLE);
                    // show a simple time - prefer order.createdAt if available
                    try {
                        String time = "";
                        if (order.createdAt != null && !order.createdAt.isEmpty()) {
                            // createdAt may be ISO instant; show only date/time short
                            time = order.createdAt.replace('T', ' ').split("\\.")[0];
                        } else {
                            time = java.time.LocalTime.now().toString();
                        }
                        tvStepTime.setText(time);
                    } catch (Exception ex) {
                        tvStepTime.setText("");
                    }
                } else {
                    ivStepBg.setImageResource(R.drawable.circle_inactive);
                    ivStepIcon.setImageResource(R.drawable.ic_orders);
                    ivStepIcon.setColorFilter(android.graphics.Color.parseColor("#8E8E93"));
                    tvStepTime.setVisibility(View.GONE);
                }

                // line color: active if next step is done
                if (vLine != null) {
                    if (i < currentIndex) vLine.setBackgroundColor(android.graphics.Color.parseColor("#2196F3"));
                    else vLine.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
                }

                container.addView(row);
            }
        }

        // button action to open order detail
        final Order orderFinal = order;
        if (btnView != null) {
            btnView.setOnClickListener(v -> {
                if (orderFinal == null) return;
                android.content.Intent it = new android.content.Intent(requireContext(), com.example.chillcup02_ui.ui.orders.OrderDetailActivity.class);
                it.putExtra("order", orderFinal);
                startActivity(it);
            });
        }
        
    }
}
