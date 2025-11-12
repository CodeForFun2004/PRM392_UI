package com.example.chillcup02_ui.ui.voucher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.data.dto.DiscountDto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {
    
    private List<DiscountDto> vouchers = new ArrayList<>();
    
    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        DiscountDto voucher = vouchers.get(position);
        holder.bind(voucher);
    }
    
    @Override
    public int getItemCount() {
        return vouchers.size();
    }
    
    public void setVouchers(List<DiscountDto> vouchers) {
        this.vouchers = vouchers != null ? vouchers : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    static class VoucherViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvDescription;
        private final TextView tvMinOrder;
        private final TextView tvExpiryDate;
        private final TextView tvPromoCode;
        private final TextView tvStatus;
        private final View cardView;
        
        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvVoucherTitle);
            tvDescription = itemView.findViewById(R.id.tvVoucherDescription);
            tvMinOrder = itemView.findViewById(R.id.tvMinOrder);
            tvExpiryDate = itemView.findViewById(R.id.tvExpiryDate);
            tvPromoCode = itemView.findViewById(R.id.tvPromoCode);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            cardView = itemView.findViewById(R.id.cardVoucher);
        }
        
        public void bind(DiscountDto voucher) {
            tvTitle.setText(voucher.getTitle());
            tvDescription.setText(voucher.getDescription());
            
            // Min order
            if (voucher.getMinOrder() != null && voucher.getMinOrder() > 0) {
                String minOrderText = String.format(Locale.getDefault(), "Đơn tối thiểu: %,d₫", voucher.getMinOrder());
                tvMinOrder.setText(minOrderText);
            } else {
                tvMinOrder.setText("Đơn tối thiểu: 0₫");
            }
            
            // Expiry date
            if (voucher.getExpiryDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvExpiryDate.setText("HSD: " + sdf.format(voucher.getExpiryDate()));
            } else {
                tvExpiryDate.setText("HSD: -");
            }
            
            // Promo code
            if (voucher.getPromotionCode() != null) {
                tvPromoCode.setText("Mã: " + voucher.getPromotionCode());
            } else {
                tvPromoCode.setText("Mã: -");
            }
            
            // Status (for used vouchers)
            if (Boolean.TRUE.equals(voucher.getIsUsed())) {
                tvStatus.setVisibility(View.VISIBLE);
                tvStatus.setText("Đã sử dụng");
                // Gray out the card
                cardView.setAlpha(0.6f);
            } else {
                tvStatus.setVisibility(View.GONE);
                cardView.setAlpha(1.0f);
            }
        }
    }
}

