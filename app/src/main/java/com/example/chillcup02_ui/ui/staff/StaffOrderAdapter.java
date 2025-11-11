package com.example.chillcup02_ui.ui.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;

import java.util.List;

public class StaffOrderAdapter extends RecyclerView.Adapter<StaffOrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private final OnDetailsClickListener detailsClickListener;

    public interface OnDetailsClickListener {
        void onDetailsClick(Order order);
    }

    public StaffOrderAdapter(List<Order> orders, OnDetailsClickListener listener) {
        this.orders = orders;
        this.detailsClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.bind(orders.get(position), detailsClickListener);
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders.clear();
        this.orders.addAll(newOrders);
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvOrderNumber, tvOrderStatus, tvCustomer, tvOrderTotal;
        private final Button btnViewDetails;
        private final Context context;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }

        public void bind(final Order order, final OnDetailsClickListener listener) {
            tvOrderNumber.setText(order.getOrderNumber());
            tvOrderStatus.setText(order.getStatus().toString());
            tvCustomer.setText(order.getDeliveryAddress());
            tvOrderTotal.setText(String.format("%,.0f VND", order.getTotal()));
            btnViewDetails.setOnClickListener(v -> listener.onDetailsClick(order));

            // Set background and text color based on status
            int colorRes;
            int backgroundRes;
            Order.OrderStatus status = order.getStatus();

            if (status == Order.OrderStatus.PENDING) {
                backgroundRes = R.drawable.status_background_pending;
                colorRes = R.color.status_pending_text;
            } else if (status == Order.OrderStatus.PROCESSING || status == Order.OrderStatus.PREPARING) {
                backgroundRes = R.drawable.status_background_preparing;
                colorRes = R.color.status_preparing_text;
            } else if (status == Order.OrderStatus.READY) {
                backgroundRes = R.drawable.status_background_ready;
                colorRes = R.color.status_ready_text;
            } else if (status == Order.OrderStatus.DELIVERING) {
                backgroundRes = R.drawable.status_background_delivering;
                colorRes = R.color.status_delivering_text;
            } else if (status == Order.OrderStatus.COMPLETED) {
                backgroundRes = R.drawable.status_background_completed;
                colorRes = R.color.status_completed_text;
            } else if (status == Order.OrderStatus.CANCELLED) {
                backgroundRes = R.drawable.status_background_cancelled;
                colorRes = R.color.status_cancelled_text;
            } else {
                backgroundRes = R.drawable.status_background_default;
                colorRes = R.color.status_default_text;
            }

            tvOrderStatus.setBackgroundResource(backgroundRes);
            tvOrderStatus.setTextColor(ContextCompat.getColor(context, colorRes));
        }
    }
}
