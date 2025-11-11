package com.example.chillcup02_ui.ui.productdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Topping;
import com.example.chillcup02_ui.util.PriceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {

    private List<Topping> toppings = new ArrayList<>();
    private Set<Integer> selectedPositions = new HashSet<>();
    private OnToppingSelectionChangedListener listener;

    public interface OnToppingSelectionChangedListener {
        void onToppingSelectionChanged(List<Topping> selectedToppings);
    }

    public void setOnToppingSelectionChangedListener(OnToppingSelectionChangedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topping, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        Topping topping = toppings.get(position);
        holder.bind(topping, selectedPositions.contains(position));
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings != null ? toppings : new ArrayList<>();
        selectedPositions.clear(); // Clear selections when data changes
        notifyDataSetChanged();
    }

    public List<Topping> getSelectedToppings() {
        List<Topping> selected = new ArrayList<>();
        for (Integer position : selectedPositions) {
            if (position >= 0 && position < toppings.size()) {
                selected.add(toppings.get(position));
            }
        }
        return selected;
    }

    public void toggleToppingSelection(int position) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position);
        } else {
            selectedPositions.add(position);
        }

        notifyItemChanged(position);

        // Notify listener
        if (listener != null) {
            listener.onToppingSelectionChanged(getSelectedToppings());
        }
    }

    class ToppingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvToppingName;
        private TextView tvToppingPrice;
        private com.google.android.material.card.MaterialCardView cardView;

        ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (com.google.android.material.card.MaterialCardView) itemView;
            tvToppingName = itemView.findViewById(R.id.tvToppingName);
            tvToppingPrice = itemView.findViewById(R.id.tvToppingPrice);
        }

        void bind(Topping topping, boolean isSelected) {
            tvToppingName.setText(topping.getName());
            tvToppingPrice.setText("+" + PriceUtil.formatPrice(topping.getPrice()));

            // Update visual appearance based on selection
            if (isSelected) {
                cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#FFF3E0")); // Light orange background
                cardView.setStrokeColor(android.graphics.Color.parseColor("#FF9800")); // Orange border
            } else {
                cardView.setCardBackgroundColor(android.graphics.Color.WHITE);
                cardView.setStrokeColor(android.graphics.Color.parseColor("#4CAF50")); // Keep green border
            }

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    toggleToppingSelection(position);
                }
            });
        }
    }
}
