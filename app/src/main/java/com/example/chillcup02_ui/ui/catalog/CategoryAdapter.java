package com.example.chillcup02_ui.ui.catalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Category;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private OnCategoryClickListener listener;
    private int selectedPosition = 0;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Chip chip = (Chip) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_chip, parent, false);
        return new CategoryViewHolder(chip);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        int previousPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(previousPosition);
        notifyItemChanged(selectedPosition);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private Chip chip;

        CategoryViewHolder(@NonNull Chip chip) {
            super(chip);
            this.chip = chip;
        }

        void bind(Category category, int position) {
            chip.setText(category.getName());
            chip.setChecked(position == selectedPosition);

            // Load category icon using Glide
            if (category.getIcon() != null && !category.getIcon().isEmpty()) {
                Glide.with(chip.getContext())
                        .load(category.getIcon())
                        .circleCrop()
                        .placeholder(R.drawable.ic_categories) // fallback icon
                        .into(new com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull android.graphics.drawable.Drawable resource,
                                                      com.bumptech.glide.request.transition.Transition<? super android.graphics.drawable.Drawable> transition) {
                                chip.setChipIcon(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable android.graphics.drawable.Drawable placeholder) {
                                chip.setChipIcon(placeholder);
                            }
                        });
            } else {
                // Set default icon if no icon URL
                chip.setChipIconResource(R.drawable.ic_categories);
            }

            chip.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCategoryClick(category, position);
                }
            });
        }
    }
}
