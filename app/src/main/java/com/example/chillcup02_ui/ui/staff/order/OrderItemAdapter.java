package com.example.chillcup02_ui.ui.staff.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.OrderItem;
import com.example.chillcup02_ui.domain.model.Topping;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private final List<OrderItem> orderItems;

    public OrderItemAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.bind(orderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItems != null ? orderItems.size() : 0;
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItemName;
        private final TextView tvItemMeta;
        private final TextView tvItemQtyPrice;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemMeta = itemView.findViewById(R.id.tvItemMeta);
            tvItemQtyPrice = itemView.findViewById(R.id.tvItemQtyPrice);
        }

        public void bind(OrderItem item) {
            tvItemName.setText(item.getName());

            String toppingsString = item.getToppings().stream()
                    .map(Topping::getName)
                    .collect(Collectors.joining(", "));

            String meta = "Size: " + item.getSize();
            if (!toppingsString.isEmpty()) {
                meta += " · Topping: " + toppingsString;
            }
            tvItemMeta.setText(meta);

            String qtyPrice = String.format("x%d · %,.0f VND", item.getQuantity(), item.getPrice());
            tvItemQtyPrice.setText(qtyPrice);
        }
    }
}
