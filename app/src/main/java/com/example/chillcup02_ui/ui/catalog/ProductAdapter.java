package com.example.chillcup02_ui.ui.catalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Product;
import com.example.chillcup02_ui.util.PriceUtil;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products = new ArrayList<>();
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        android.util.Log.d("ProductAdapter", "onBindViewHolder called for position: " + position);
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(List<Product> products) {
        this.products = products != null ? products : new ArrayList<>();
        android.util.Log.d("ProductAdapter", "Setting products: " + this.products.size());
        notifyDataSetChanged();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProductImage;
        private TextView tvProductName;
        private TextView tvProductPrice;
        private TextView tvProductRating;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductRating = itemView.findViewById(R.id.tvProductRating);
        }

        void bind(Product product) {
            android.util.Log.d("ProductAdapter", "Binding product: " + product.getName() + ", image: " + product.getImage());

            tvProductName.setText(product.getName());
            tvProductPrice.setText(PriceUtil.formatPrice(product.getBasePrice()));
            tvProductRating.setText("⭐ " + product.getRating());

            // Load product image
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                android.util.Log.d("ProductAdapter", "Loading image: " + product.getImage());
                Glide.with(ivProductImage.getContext())
                        .load(product.getImage())
                        .placeholder(R.drawable.ic_categories)
                        .error(R.drawable.ic_categories)
                        .centerCrop()
                        .into(ivProductImage);
            } else {
                android.util.Log.d("ProductAdapter", "No image, using placeholder");
                ivProductImage.setImageResource(R.drawable.ic_categories);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}
