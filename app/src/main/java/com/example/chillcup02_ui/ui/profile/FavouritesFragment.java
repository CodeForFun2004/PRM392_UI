package com.example.chillcup02_ui.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.chillcup02_ui.databinding.FragmentFavouritesBinding;
import com.example.chillcup02_ui.domain.model.Favourite;
import com.example.chillcup02_ui.ui.catalog.ProductAdapter;
import com.example.chillcup02_ui.ui.productdetail.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private FragmentFavouritesBinding binding;
    private FavouritesViewModel viewModel;
    private ProductAdapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(FavouritesViewModel.class);

        setupRecyclerView();
        setupObservers();

        // Load favourites
        viewModel.loadFavourites();

        // Setup back button
        binding.btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 3);
        binding.rvFavourites.setLayoutManager(gridLayoutManager);
        binding.rvFavourites.setAdapter(productAdapter);

        productAdapter.setOnProductClickListener(product -> {
            // Navigate to product detail
            Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });
    }

    private void setupObservers() {
        // Observe favourites
        viewModel.getFavourites().observe(getViewLifecycleOwner(), favourites -> {
            if (favourites != null && !favourites.isEmpty()) {
                // Extract products from favourites
                List<com.example.chillcup02_ui.domain.model.Product> products = new ArrayList<>();
                for (Favourite favourite : favourites) {
                    products.add(favourite.getProduct());
                }
                productAdapter.setProducts(products);
                binding.tvFavouritesCount.setText("Sản phẩm yêu thích (" + favourites.size() + ")");
                binding.rvFavourites.setVisibility(View.VISIBLE);
                binding.tvEmptyState.setVisibility(View.GONE);
            } else {
                productAdapter.setProducts(new ArrayList<>());
                binding.tvFavouritesCount.setText("Sản phẩm yêu thích (0)");
                binding.rvFavourites.setVisibility(View.GONE);
                binding.tvEmptyState.setVisibility(View.VISIBLE);
            }
        });

        // Observe loading state
        viewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Observe errors
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
