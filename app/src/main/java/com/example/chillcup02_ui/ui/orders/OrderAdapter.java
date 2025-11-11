package com.example.chillcup02_ui.ui.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.VH> {

    public interface OrderListener { void onOrderClick(Order order); }

    private final List<Order> items = new ArrayList<>();
    private final OrderListener listener;

    public OrderAdapter(List<Order> list, OrderListener listener) {
        if (list != null) items.addAll(list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Order o = items.get(position);
    holder.tvNumber.setText(o.orderNumber != null ? o.orderNumber : "-");
    holder.tvDate.setText(o.createdAt != null ? o.createdAt.replace('T',' ').replace("Z","") : "");
    holder.tvTotal.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(o.total));
    holder.tvStatus.setText(o.status != null ? capitalize(o.status) : "");

        // items count
        if (o.items != null) {
            holder.tvItemsCount.setText(o.items.size() + (o.items.size() > 1 ? " items" : " item"));
        } else {
            holder.tvItemsCount.setText("0 items");
        }

        // delivery time
        holder.tvDeliveryTime.setText(o.deliveryTime != null ? ("• " + o.deliveryTime) : "");

        // status color mapping to match RN styles
        int bgColor = getStatusColor(o.status);
        // set background tint of badge drawable
        android.graphics.drawable.Drawable bg = holder.tvStatus.getBackground();
        if (bg != null) {
            bg = androidx.core.graphics.drawable.DrawableCompat.wrap(bg);
            androidx.core.graphics.drawable.DrawableCompat.setTint(bg, bgColor);
            holder.tvStatus.setBackground(bg);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onOrderClick(o);
            }
        });

        // detail / tracking link
        TextView tvLink = holder.itemView.findViewById(R.id.tvDetailLink);
        if (tvLink != null) {
            if (o.status != null && o.status.equalsIgnoreCase("delivering")) {
                tvLink.setText("Tap to track order");
                tvLink.setTextColor(android.graphics.Color.parseColor("#2196F3"));
            } else {
                tvLink.setText("Chi tiết đơn hàng");
                tvLink.setTextColor(android.graphics.Color.parseColor("#2196F3"));
            }
            tvLink.setOnClickListener(v -> {
                if (listener != null) listener.onOrderClick(o);
            });
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    public void setOrders(List<Order> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNumber, tvDate, tvTotal, tvStatus;
        TextView tvItemsCount, tvDeliveryTime;
        VH(@NonNull View v) {
            super(v);
            tvNumber = v.findViewById(R.id.tvOrderNumber);
            tvDate = v.findViewById(R.id.tvOrderDate);
            tvTotal = v.findViewById(R.id.tvOrderTotal);
            tvStatus = v.findViewById(R.id.tvOrderStatus);
            tvItemsCount = v.findViewById(R.id.tvItemsCount);
            tvDeliveryTime = v.findViewById(R.id.tvDeliveryTime);
        }
    }

    private int getStatusColor(String status) {
        if (status == null) return android.graphics.Color.parseColor("#FF9800");
        switch (status.toLowerCase()) {
            case "processing":
            case "preparing":
            case "ready":
                return android.graphics.Color.parseColor("#FF9800"); // orange
            case "delivering":
                return android.graphics.Color.parseColor("#2196F3"); // blue
            case "completed":
                return android.graphics.Color.parseColor("#4CAF50"); // green
            case "cancelled":
                return android.graphics.Color.parseColor("#9E9E9E"); // grey
            case "refunded":
                return android.graphics.Color.parseColor("#8E24AA"); // purple
            default:
                return android.graphics.Color.parseColor("#FF9800");
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) return s;
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
