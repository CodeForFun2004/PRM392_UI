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
import com.example.chillcup02_ui.domain.model.OrderItem;

import java.util.List;

public class StaffOrderAdapter extends RecyclerView.Adapter<StaffOrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private final OnDetailsClickListener detailsClickListener;

    // Updated interface for the new "Details" button
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
        Order order = orders.get(position);
        holder.bind(order, detailsClickListener);
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

    // ViewHolder now matches the new item_staff_order.xml
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvOrderNumber;
        private final TextView tvOrderStatus;
        private final TextView tvCustomer;
        private final TextView tvOrderTotal;
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
            
            // In a real app, you would fetch the customer name based on order.getUserId()
            // For mock data, we can use the delivery address or a placeholder.
            tvCustomer.setText(order.getDeliveryAddress());
            
            String totalString = String.format("%,.0f VND", order.getTotal());
            tvOrderTotal.setText(totalString);

            // Set the click listener on the "Details" button
            btnViewDetails.setOnClickListener(v -> listener.onDetailsClick(order));

            // You can enhance this with more colors later
            int colorRes, backgroundRes;
            switch (order.getStatus()) {
                case PENDING:
                    backgroundRes = R.drawable.status_background_pending; // Example: a yellow background
                    colorRes = R.color.status_pending_text; // Example: a dark yellow text
                    break;
                case PREPARING:
                case PROCESSING:
                    backgroundRes = R.drawable.status_background_preparing; // Example: a blue background
                    colorRes = R.color.status_preparing_text; // Example: a dark blue text
                    break;
                case READY:
                    backgroundRes = R.drawable.status_background_ready; // Example: a green background
                    colorRes = R.color.status_ready_text; // Example: a dark green text
                    break;
                default:
                    backgroundRes = R.drawable.status_background_default; // A default gray background
                    colorRes = R.color.status_default_text; // A default dark gray text
                    break;
            }
            tvOrderStatus.setBackgroundResource(backgroundRes);
           // tvOrderStatus.setTextColor(ContextCompat.getColor(context, colorRes));
        }
    }
}
