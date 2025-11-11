package com.example.chillcup02_ui.ui.productdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.domain.model.Size;
import com.example.chillcup02_ui.domain.model.Topping;
import com.example.chillcup02_ui.util.PriceUtil;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ProductDetailViewModel viewModel;
    private String productId;

    // Views
    private Toolbar toolbar;
    private ImageView ivProductImage;
    private TextView tvProductName;
    private TextView tvProductRating;
    private TextView tvProductPrice;
    private TextView tvProductDescription;
    private TextView tvSizeTitle;
    private RecyclerView rvSizes;
    private TextView tvToppingTitle;
    private RecyclerView rvToppings;
    private TextView tvError;
    private View progressBar;
    private TextView btnAddToCart;

    // Adapters
    private SizeAdapter sizeAdapter;
    private ToppingAdapter toppingAdapter;

    // Current selections
    private Product currentProduct;
    private Size selectedSize;
    private List<Topping> selectedToppings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get product ID from intent
        productId = getIntent().getStringExtra("product_id");
        if (productId == null) {
            Toast.makeText(this, "Product ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);

        initializeViews();
        setupAdapters();
        setupToolbar();
        setupObservers();
        setupClickListeners();

        // Load data
        viewModel.loadProduct(productId);
        viewModel.loadSizes();
        viewModel.loadToppings();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        ivProductImage = findViewById(R.id.ivProductImage);
        tvProductName = findViewById(R.id.tvProductName);
        tvProductRating = findViewById(R.id.tvProductRating);
        tvProductPrice = findViewById(R.id.tvProductPrice);
        tvProductDescription = findViewById(R.id.tvProductDescription);
        tvSizeTitle = findViewById(R.id.tvSizeTitle);
        rvSizes = findViewById(R.id.rvSizes);
        tvToppingTitle = findViewById(R.id.tvToppingTitle);
        rvToppings = findViewById(R.id.rvToppings);
        tvError = findViewById(R.id.tvError);
        progressBar = findViewById(R.id.progressBar);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Setup RecyclerViews
        rvSizes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvToppings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupAdapters() {
        // Size adapter
        sizeAdapter = new SizeAdapter();
        sizeAdapter.setOnSizeClickListener((size, position) -> {
            selectedSize = size;
            updateTotalPrice();
        });
        rvSizes.setAdapter(sizeAdapter);

        // Topping adapter
        toppingAdapter = new ToppingAdapter();
        toppingAdapter.setOnToppingSelectionChangedListener(toppings -> {
            selectedToppings = toppings;
            updateTotalPrice();
        });
        rvToppings.setAdapter(toppingAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupObservers() {
        viewModel.getProduct().observe(this, product -> {
            currentProduct = product;
            displayProduct(product);
            updateTotalPrice();
        });

        viewModel.getSizes().observe(this, sizes -> {
            sizeAdapter.setSizes(sizes);
            // Set default size (first one)
            if (sizes != null && !sizes.isEmpty() && selectedSize == null) {
                selectedSize = sizes.get(0);
                sizeAdapter.setSelectedPosition(0);
            }
        });

        viewModel.getToppings().observe(this, toppings -> {
            toppingAdapter.setToppings(toppings);
        });

        viewModel.getLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnAddToCart.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        viewModel.getError().observe(this, error -> {
            if (error != null) {
                tvError.setText(error);
                tvError.setVisibility(View.VISIBLE);
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            } else {
                tvError.setVisibility(View.GONE);
            }
        });
    }

    private void setupClickListeners() {
        btnAddToCart.setOnClickListener(v -> {
            // TODO: Implement add to cart functionality with selected options
            String message = "Đã thêm vào giỏ hàng!\n";
            if (selectedSize != null) {
                message += "Size: " + selectedSize.getName() + "\n";
            }
            if (selectedToppings != null && !selectedToppings.isEmpty()) {
                message += "Toppings: " + selectedToppings.size() + " loại";
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void displayProduct(Product product) {
        if (product == null) return;

        // Set product name
        tvProductName.setText(product.getName());

        // Set product rating
        tvProductRating.setText("⭐ " + product.getRating());

        // Set product description
        tvProductDescription.setText(product.getDescription() != null ? product.getDescription() : "Không có mô tả");

        // Load product image
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Glide.with(this)
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_categories)
                    .error(R.drawable.ic_categories)
                    .centerCrop()
                    .into(ivProductImage);
        } else {
            ivProductImage.setImageResource(R.drawable.ic_categories);
        }

        // Size and topping sections are always shown now since we load them separately
        tvSizeTitle.setVisibility(View.VISIBLE);
        rvSizes.setVisibility(View.VISIBLE);
        tvToppingTitle.setVisibility(View.VISIBLE);
        rvToppings.setVisibility(View.VISIBLE);
    }

    private void updateTotalPrice() {
        if (currentProduct == null) return;

        double basePrice = currentProduct.getBasePrice();
        double sizeMultiplier = selectedSize != null ? selectedSize.getMultiplier() : 1.0;
        double toppingsPrice = 0;

        if (selectedToppings != null) {
            for (Topping topping : selectedToppings) {
                toppingsPrice += topping.getPrice();
            }
        }

        double totalPrice = (basePrice * sizeMultiplier) + toppingsPrice;
        tvProductPrice.setText(PriceUtil.formatPrice(totalPrice));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
