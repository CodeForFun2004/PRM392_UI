package com.example.chillcup02_ui.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.util.CurrencyUtils;
import com.example.chillcup02_ui.domain.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

	public interface CartItemListener { void onQuantityChanged(); }

	private final List<CartItem> items = new ArrayList<>();
	private final CartItemListener listener;

	public CartAdapter(List<CartItem> list, CartItemListener listener) {
		if (list != null) items.addAll(list);
		this.listener = listener;
	}

	public List<CartItem> getItems() { return items; }

	@NonNull
	@Override
	public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
		return new VH(v);
	}

	@Override
	public void onBindViewHolder(@NonNull VH holder, int position) {
		CartItem it = items.get(position);
		holder.tvName.setText(it.product.name);
		// product model does not have category in this mock; show placeholder
		holder.tvCategory.setText("Món Nóng");
		holder.tvPrice.setText(CurrencyUtils.formatVnd((long) it.getUnitPrice()));
		holder.tvQty.setText(String.valueOf(it.getQty()));

		// image: keep placeholder (no image loader configured)
		holder.ivImage.setImageResource(R.mipmap.ic_launcher);

		holder.btnPlus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				it.qty++;
				holder.tvQty.setText(String.valueOf(it.qty));
				if (listener != null) listener.onQuantityChanged();
			}
		});

		holder.btnMinus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (it.qty > 1) {
					it.qty--;
					holder.tvQty.setText(String.valueOf(it.qty));
					if (listener != null) listener.onQuantityChanged();
				}
			}
		});

		holder.btnTrash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// remove from shared CartManager and adapter list
				com.example.chillcup02_ui.util.CartManager.getInstance().getItems().remove(it);
				int pos = holder.getAdapterPosition();
				if (pos >= 0 && pos < items.size()) {
					items.remove(pos);
					notifyItemRemoved(pos);
				}
				if (listener != null) listener.onQuantityChanged();
			}
		});
	}

	@Override
	public int getItemCount() { return items.size(); }

	static class VH extends RecyclerView.ViewHolder {
		ImageView ivImage;
		TextView tvName, tvCategory, tvPrice, tvQty;
		Button btnPlus, btnMinus;
		ImageButton btnTrash;

		VH(@NonNull View v) {
			super(v);
			ivImage = v.findViewById(R.id.ivProductImage);
			tvName = v.findViewById(R.id.tvProductName);
			tvCategory = v.findViewById(R.id.tvProductCategory);
			tvPrice = v.findViewById(R.id.tvProductPrice);
			tvQty = v.findViewById(R.id.tvQty);
			btnPlus = v.findViewById(R.id.btnPlus);
			btnMinus = v.findViewById(R.id.btnMinus);
			btnTrash = v.findViewById(R.id.btnTrash);
		}
	}
}
