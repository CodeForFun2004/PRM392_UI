package com.example.chillcup02_ui.ui.productdetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.Size;

import java.util.ArrayList;
import java.util.List;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {

    private List<Size> sizes = new ArrayList<>();
    private int selectedPosition = 0; // Default to first size
    private OnSizeClickListener listener;

    public interface OnSizeClickListener {
        void onSizeClick(Size size, int position);
    }

    public void setOnSizeClickListener(OnSizeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_size, parent, false);
        return new SizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeViewHolder holder, int position) {
        Size size = sizes.get(position);
        holder.bind(size, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes != null ? sizes : new ArrayList<>();
        notifyDataSetChanged();
    }

    public Size getSelectedSize() {
        if (selectedPosition >= 0 && selectedPosition < sizes.size()) {
            return sizes.get(selectedPosition);
        }
        return null;
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPosition);
        notifyItemChanged(selectedPosition);
    }

    class SizeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSizeName;
        private TextView tvSizeVolume;
        private com.google.android.material.card.MaterialCardView cardView;

        SizeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (com.google.android.material.card.MaterialCardView) itemView;
            tvSizeName = itemView.findViewById(R.id.tvSizeName);
            tvSizeVolume = itemView.findViewById(R.id.tvSizeVolume);
        }

        void bind(Size size, boolean isSelected) {
            tvSizeName.setText(size.getName());
            tvSizeVolume.setText(size.getVolume());

            // Update visual appearance based on selection
            if (isSelected) {
                cardView.setCardBackgroundColor(android.graphics.Color.parseColor("#E8F5E8")); // Light green background
                cardView.setStrokeColor(android.graphics.Color.parseColor("#4CAF50")); // Darker green border
            } else {
                cardView.setCardBackgroundColor(android.graphics.Color.WHITE);
                cardView.setStrokeColor(android.graphics.Color.parseColor("#4CAF50")); // Keep green border
            }

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedPosition(position);
                    if (listener != null) {
                        listener.onSizeClick(size, position);
                    }
                }
            });
        }
    }
}
