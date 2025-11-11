package com.example.chillcup02_ui.ui.staff.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;
import com.example.chillcup02_ui.ui.staff.StaffOrderAdapter;
import com.example.chillcup02_ui.ui.staff.StaffViewModel;
import com.example.chillcup02_ui.ui.staff.order.OrderDetailActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class StaffDashboardFragment extends Fragment implements StaffOrderAdapter.OnDetailsClickListener {

    private StaffViewModel viewModel;
    private TextView tvTotalRevenue;
    private TextView tvPendingOrders;
    private TextView tvTotalOrders;
    private RecyclerView rvRecentOrders;
    private StaffOrderAdapter recentOrdersAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(StaffViewModel.class);

        // Bind views
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        tvPendingOrders = view.findViewById(R.id.tvPendingOrders);
        tvTotalOrders = view.findViewById(R.id.tvTotalOrders); // This ID now exists in the layout
        rvRecentOrders = view.findViewById(R.id.rvRecentOrders);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        rvRecentOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recentOrdersAdapter = new StaffOrderAdapter(new ArrayList<>(), this);
        rvRecentOrders.setAdapter(recentOrdersAdapter);
    }

    private void observeViewModel() {
        viewModel.getTotalRevenue().observe(getViewLifecycleOwner(), revenue -> {
            if (revenue != null) {
                NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                tvTotalRevenue.setText(format.format(revenue));
            }
        });

        viewModel.getPendingOrdersCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvPendingOrders.setText(String.valueOf(count));
            }
        });

        viewModel.getTotalOrdersCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvTotalOrders.setText(String.valueOf(count));
            }
        });

        viewModel.getRecentOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                recentOrdersAdapter.updateOrders(orders);
            }
        });
    }

    @Override
    public void onDetailsClick(Order order) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        intent.putExtra("ORDER_EXTRA", order);
        startActivity(intent);
    }
}
