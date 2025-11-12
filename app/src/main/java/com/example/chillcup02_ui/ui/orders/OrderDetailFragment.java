package com.example.chillcup02_ui.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;

public class OrderDetailFragment extends Fragment {

    private static final String ARG_ORDER = "arg_order";
    private Order order;

    public static OrderDetailFragment newInstance(Order order) {
        OrderDetailFragment f = new OrderDetailFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_ORDER, order);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ARG_ORDER);
        }
        TextView tvNumber = view.findViewById(R.id.tvDetailOrderNumber);
        TextView tvDate = view.findViewById(R.id.tvDetailDate);
        TextView tvStatus = view.findViewById(R.id.tvDetailStatus);
        TextView tvDeliveryTime = view.findViewById(R.id.tvDetailDeliveryTime);
        android.widget.LinearLayout llItems = view.findViewById(R.id.llItemsContainer);
        TextView tvSubtotal = view.findViewById(R.id.tvSubtotal);
        TextView tvDelivery = view.findViewById(R.id.tvDelivery);
        TextView tvDiscount = view.findViewById(R.id.tvDiscount);
        TextView tvTotal = view.findViewById(R.id.tvTotal);
        TextView tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvPhone = view.findViewById(R.id.tvPhone);
    android.widget.Button btnTrack = view.findViewById(R.id.btnTrack);

        if (order != null) {
            tvNumber.setText(order.orderNumber != null ? order.orderNumber : "-");
            tvDate.setText(order.createdAt != null ? order.createdAt.replace('T',' ').replace("Z","") : "");
            tvStatus.setText(order.status != null ? capitalize(order.status) : "");
            tvDeliveryTime.setText(order.deliveryTime != null ? ("• " + order.deliveryTime) : "");

            // populate items
            llItems.removeAllViews();
            int subtotal = 0;
            if (order.items != null) {
                for (Order.OrderItem it : order.items) {
                    View row = LayoutInflater.from(requireContext()).inflate(R.layout.item_order_detail_row, llItems, false);
                    TextView tvName = row.findViewById(R.id.tvItemName);
                    TextView tvQty = row.findViewById(R.id.tvItemQty);
                    TextView tvPrice = row.findViewById(R.id.tvItemPrice);
                    tvName.setText(it.name);
                    tvQty.setText("x" + it.quantity);
                    tvPrice.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(it.price));
                    llItems.addView(row);
                    subtotal += it.price * it.quantity;
                }
            }

            tvSubtotal.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(subtotal));
            // simple delivery and discount heuristics for mock
            int deliveryFee = order.deliveryTime != null ? 15000 : 0; // use VND numbers
            int discount = 0;
            tvDelivery.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(deliveryFee));
            tvDiscount.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(discount));
            tvTotal.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(order.total));

            tvPaymentMethod.setText(order.paymentMethod != null ? order.paymentMethod.toUpperCase() : "COD");
            tvAddress.setText(order.deliveryAddress != null ? order.deliveryAddress : "");
            tvPhone.setText(order.phone != null ? order.phone : "");
        }

        btnTrack.setOnClickListener(v -> {
            OrderTrackingFragment f = OrderTrackingFragment.newInstance(order);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, f)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
