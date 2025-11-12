package com.example.chillcup02_ui.ui.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.auth.LoginActivity;
import com.example.chillcup02_ui.databinding.FragmentOrdersBinding;
import com.example.chillcup02_ui.ui.auth.AuthViewModel;

import com.google.firebase.auth.FirebaseUser;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.view.View;
import com.example.chillcup02_ui.util.MockOrders;
import com.example.chillcup02_ui.domain.model.Order;
import java.util.List;
import com.example.chillcup02_ui.ui.orders.OrderAdapter;


public class OrdersFragment extends Fragment {
    
    private FragmentOrdersBinding binding;
    private AuthViewModel authViewModel;
    private RecyclerView rvOrders;
    private OrderAdapter adapter;
    private LinearLayout tabsRow;
    private java.util.List<Order> orders = new java.util.ArrayList<>();
    private String currentTab = "";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        
        // Observe auth state
        authViewModel.isLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            if (isLoggedIn) {
                showOrdersContent();
            } else {
                showLoginPrompt();
            }
        });
        
        // Check initial state
        if (authViewModel.isUserLoggedIn()) {
            showOrdersContent();
        } else {
            showLoginPrompt();
        }
    }
    
    private void showLoginPrompt() {
        binding.containerLoginPrompt.setVisibility(View.VISIBLE);
        binding.containerOrdersContent.setVisibility(View.GONE);
        
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
    
    private void showOrdersContent() {
        binding.containerLoginPrompt.setVisibility(View.GONE);
        binding.containerOrdersContent.setVisibility(View.VISIBLE);
        
            // try loading orders from mock server; fallback to local MockOrders
            com.example.chillcup02_ui.network.ApiService api = com.example.chillcup02_ui.network.ApiClient.service();
            api.listOrders().enqueue(new retrofit2.Callback<java.util.List<com.example.chillcup02_ui.network.model.OrderResponse.Order>>() {
                @Override
                public void onResponse(retrofit2.Call<java.util.List<com.example.chillcup02_ui.network.model.OrderResponse.Order>> call, retrofit2.Response<java.util.List<com.example.chillcup02_ui.network.model.OrderResponse.Order>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        java.util.List<Order> converted = new java.util.ArrayList<>();
                        for (com.example.chillcup02_ui.network.model.OrderResponse.Order o : response.body()) {
                            Order ord = new Order();
                            ord._id = o._id;
                            ord.orderNumber = o.orderNumber;
                            // server doesn't provide createdAt in OrderResponse currently
                            ord.createdAt = "";
                            ord.status = o.status;
                            ord.paymentMethod = o.paymentMethod;
                            ord.total = (int) Math.round(o.total);
                            ord.deliveryAddress = o.deliveryAddress;
                            ord.phone = o.phone;
                            ord.items = new java.util.ArrayList<>();
                            if (o.items != null) {
                                for (com.example.chillcup02_ui.network.model.OrderRequest.Item it : o.items) {
                                    Order.OrderItem oi = new Order.OrderItem();
                                    oi.name = it.name;
                                    oi.quantity = it.quantity;
                                    oi.price = (int) Math.round(it.price);
                                    ord.items.add(oi);
                                }
                            }
                            converted.add(ord);
                        }
                        adapter.setOrders(filterByStatus(converted, currentTab));
                    } else {
                        java.util.List<Order> all = MockOrders.getOrders();
                        adapter.setOrders(filterByStatus(all, currentTab));
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<java.util.List<com.example.chillcup02_ui.network.model.OrderResponse.Order>> call, Throwable t) {
                    java.util.List<Order> all = MockOrders.getOrders();
                    adapter.setOrders(filterByStatus(all, currentTab));
                }
            });
            // start with local mock orders so UI is responsive
            orders = MockOrders.getOrders();

        // setup tabs with icons and badges
        tabsRow = binding.getRoot().findViewById(R.id.tabsRow);
        tabsRow.removeAllViews();
        String[] tabs = new String[] {"preparing","delivering","completed","cancelled","refunded"};
        for (String t : tabs) {
            View tab = LayoutInflater.from(requireContext()).inflate(R.layout.item_order_tab, tabsRow, false);
            android.widget.ImageView iv = tab.findViewById(R.id.ivTabIcon);
            android.widget.TextView tv = tab.findViewById(R.id.tvTabLabel);
            tv.setText(t.equals("preparing") ? "Đang chuẩn bị" : t.equals("delivering") ? "Đang giao" : t.equals("completed") ? "Hoàn tất" : t.equals("cancelled") ? "Đã hủy" : "Đã trả"
            );
            // choose icons
            int icon = android.R.drawable.ic_menu_compass;
            switch (t) {
                case "preparing": icon = android.R.drawable.ic_menu_edit; break;
                case "delivering": icon = android.R.drawable.ic_menu_directions; break;
                case "completed": icon = android.R.drawable.ic_menu_agenda; break;
                case "cancelled": icon = android.R.drawable.ic_menu_close_clear_cancel; break;
                case "refunded": icon = android.R.drawable.ic_menu_revert; break;
            }
            iv.setImageResource(icon);

            // no badge counts (user requested no small red numbers)

            final String tabKey = t;
            tab.setOnClickListener(v -> {
                currentTab = tabKey;
                filterOrders(currentTab, orders);
                // highlight selected tab: reset all, then set selected background and tint
                for (int i = 0; i < tabsRow.getChildCount(); i++) {
                    View tvw = tabsRow.getChildAt(i);
                    tvw.setBackgroundResource(android.R.color.transparent);
                    android.widget.ImageView ivw = tvw.findViewById(R.id.ivTabIcon);
                    android.widget.TextView tvwLabel = tvw.findViewById(R.id.tvTabLabel);
                    if (ivw != null) ivw.setColorFilter(android.graphics.Color.parseColor("#9E9E9E"));
                    if (tvwLabel != null) tvwLabel.setTextColor(android.graphics.Color.parseColor("#9E9E9E"));
                }
                tab.setBackgroundResource(R.drawable.bg_tab_selected);
                // selected tint
                iv.setColorFilter(android.graphics.Color.parseColor("#FF7043"));
                tv.setTextColor(android.graphics.Color.parseColor("#FF7043"));
            });

            tabsRow.addView(tab);
        }

        rvOrders = binding.getRoot().findViewById(R.id.rvOrders);
        adapter = new OrderAdapter(orders, new OrderAdapter.OrderListener() {
            @Override
            public void onOrderClick(Order order) {
                // open detail fragment
                OrderDetailFragment frag = OrderDetailFragment.newInstance(order);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, frag)
                        .addToBackStack(null)
                        .commit();
            }
        });
        rvOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvOrders.setAdapter(adapter);
    }

    private void filterOrders(String tabKey, List<Order> allOrders) {
        if (tabKey == null || tabKey.isEmpty()) return;
        java.util.List<Order> filtered = filterByStatus(allOrders, tabKey);
        if (adapter != null) {
            adapter.setOrders(filtered);
        } else {
            adapter = new OrderAdapter(filtered, new OrderAdapter.OrderListener() {
                @Override
                public void onOrderClick(Order order) {
                    OrderDetailFragment frag = OrderDetailFragment.newInstance(order);
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, frag)
                            .addToBackStack(null)
                            .commit();
                }
            });
            rvOrders.setAdapter(adapter);
        }
    }

    private java.util.List<Order> filterByStatus(List<Order> allOrders, String tabKey) {
        java.util.List<Order> filtered = new java.util.ArrayList<>();
        if (allOrders == null) return filtered;
        switch (tabKey) {
            case "preparing":
                for (Order o : allOrders) if (o.status != null && (o.status.equals("processing") || o.status.equals("preparing") || o.status.equals("ready") || o.status.equals("pending"))) filtered.add(o);
                break;
            case "delivering":
                for (Order o : allOrders) if (o.status != null && o.status.equals("delivering")) filtered.add(o);
                break;
            case "completed":
                for (Order o : allOrders) if (o.status != null && o.status.equals("completed")) filtered.add(o);
                break;
            case "cancelled":
                for (Order o : allOrders) if (o.status != null && o.status.equals("cancelled")) filtered.add(o);
                break;
            case "refunded":
                for (Order o : allOrders) if (o.refundRequests != null && !o.refundRequests.isEmpty()) filtered.add(o);
                break;
            default:
                filtered.addAll(allOrders);
        }
        return filtered;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
