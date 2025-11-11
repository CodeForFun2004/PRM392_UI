package com.example.chillcup02_ui.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;

import java.util.ArrayList;

public class CatalogFragment extends Fragment {

    private CatalogViewModel viewModel;
    private RecyclerView recyclerView;
    private CatalogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Assuming you have a fragment_catalog.xml layout with a RecyclerView
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

        recyclerView = view.findViewById(R.id.rvProducts); // Make sure this ID exists in your fragment_catalog.xml
        setupRecyclerView();
        observeViewModel();

        viewModel.loadProducts();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CatalogAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) {
                adapter.updateProducts(products);
            }
        });
    }
}
