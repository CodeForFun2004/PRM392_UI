package com.example.chillcup02_ui.ui.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chillcup02_ui.R;

public class OrderSuccessFragment extends Fragment {

    private static final String ARG_ORDER_ID = "arg_order_id";

    public static OrderSuccessFragment newInstance(String orderId) {
        OrderSuccessFragment f = new OrderSuccessFragment();
        android.os.Bundle b = new android.os.Bundle();
        b.putString(ARG_ORDER_ID, orderId);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_success, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnHome = view.findViewById(R.id.btnBackHome);
        Button btnViewOrder = null;
        if (view.findViewById(R.id.btnViewOrder) != null) {
            btnViewOrder = view.findViewById(R.id.btnViewOrder);
        }

        final String orderId = getArguments() != null ? getArguments().getString(ARG_ORDER_ID) : null;

        // show order summary if available
        if (orderId != null) {
            com.example.chillcup02_ui.domain.model.Order o = com.example.chillcup02_ui.util.MockOrders.getOrderById(orderId);
            if (o != null) {
                TextView tv = view.findViewById(R.id.tvOrderPlacedSummary);
                if (tv != null) {
                    tv.setText("Order " + o.orderNumber + " — Total: $" + o.total);
                }
            }
        }

        btnHome.setOnClickListener(v -> {
            // pop back to home fragment
            requireActivity().getSupportFragmentManager().popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });

        if (btnViewOrder != null && orderId != null) {
            btnViewOrder.setOnClickListener(v -> {
                com.example.chillcup02_ui.domain.model.Order o = com.example.chillcup02_ui.util.MockOrders.getOrderById(orderId);
                if (o != null) {
                    OrderDetailFragment frag = OrderDetailFragment.newInstance(o);
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, frag)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}
