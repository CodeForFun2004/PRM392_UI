package com.example.chillcup02_ui.ui.staff.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

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

import java.util.ArrayList;

// Implement the new listener interface
public class StaffOrderFragment extends Fragment implements StaffOrderAdapter.OnDetailsClickListener {

    private StaffViewModel viewModel;
    private RecyclerView rvOrders;
    private StaffOrderAdapter adapter;
    private Spinner spFilterStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the new layout for this fragment
        return inflater.inflate(R.layout.fragment_staff_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(StaffViewModel.class);

        // Initialize views from the new, more detailed layout
        rvOrders = view.findViewById(R.id.rvOrders);
        spFilterStatus = view.findViewById(R.id.spFilterStatus);
        // We can add listeners to the spinner and refresh button later

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        // The fragment now passes itself as the OnDetailsClickListener
        adapter = new StaffOrderAdapter(new ArrayList<>(), this);
        rvOrders.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getOrders().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null) {
                adapter.updateOrders(orders);
            }
        });
    }

    /**
     * This method is now called when the 'Details' button on an order row is clicked.
     * It will launch the new OrderDetailActivity.
     */
    @Override
    public void onDetailsClick(Order order) {
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        // Pass the Order object itself to the detail activity.
        // The Order class and its children must implement Serializable for this to work.
        intent.putExtra("ORDER_EXTRA", order);
        startActivity(intent);
    }
}
