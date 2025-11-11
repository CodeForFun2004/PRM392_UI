package com.example.chillcup02_ui.ui.staff.order;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Order;
import com.example.chillcup02_ui.ui.staff.StaffOrderAdapter;
import com.example.chillcup02_ui.ui.staff.StaffViewModel;

import java.util.ArrayList;

public class StaffOrderFragment extends Fragment implements StaffOrderAdapter.OnDetailsClickListener {

    private StaffViewModel viewModel;
    private RecyclerView rvOrders;
    private StaffOrderAdapter adapter;
    private Spinner spFilterStatus;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_staff_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(StaffViewModel.class);

        rvOrders = view.findViewById(R.id.rvOrders);
        spFilterStatus = view.findViewById(R.id.spFilterStatus);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        setupRecyclerView();
        setupSpinnerListener();
        setupSwipeToRefresh();
        observeViewModel();
    }

    private void setupRecyclerView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new StaffOrderAdapter(new ArrayList<>(), this);
        rvOrders.setAdapter(adapter);
    }

    private void setupSpinnerListener() {
        spFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
                viewModel.setFilter(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { /* Do nothing */ }
        });
    }

    private void setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            new Handler().postDelayed(() -> {
                // Correctly call the new public method on the ViewModel
                viewModel.forceRefresh();
                swipeRefreshLayout.setRefreshing(false); // Stop the animation
            }, 1000);
        });
    }

    private void observeViewModel() {
        viewModel.getFilteredOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                adapter.updateOrders(orders);
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
