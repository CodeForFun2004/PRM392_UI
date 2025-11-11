package com.example.chillcup02_ui.ui.cart;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.CartItem;
import com.example.chillcup02_ui.util.MockData;

import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView tvSubtotal;
    private Button tvCheckout;
    private View loadingView;
    private View errorView;
    private View emptyView;
    private TextView tvErrorText;
    private TextView tvRetry;
    private TextView tvClearCart;
    private EditText etPromo;
    private Button btnApplyPromo;
    private Button btnRemovePromo;
    private TextView tvDelivery;
    private TextView tvDiscount;

    private String appliedPromo = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerCart);
        tvSubtotal = view.findViewById(R.id.tvSubtotal);
        tvCheckout = view.findViewById(R.id.tvCheckout);
    final TextView tvTotalSummary = view.findViewById(R.id.tvTotalSummary);
        loadingView = view.findViewById(R.id.loadingView);
        errorView = view.findViewById(R.id.errorView);
        emptyView = view.findViewById(R.id.emptyView);
        tvErrorText = view.findViewById(R.id.tvErrorText);
        tvRetry = view.findViewById(R.id.tvRetry);
        tvClearCart = view.findViewById(R.id.tvClearCart);
        etPromo = view.findViewById(R.id.etPromo);
        btnApplyPromo = view.findViewById(R.id.btnApplyPromo);
        btnRemovePromo = view.findViewById(R.id.btnRemovePromo);
        tvDelivery = view.findViewById(R.id.tvDelivery);
        tvDiscount = view.findViewById(R.id.tvDiscount);

        // use shared CartManager so checkout/order flow can access same cart
        List<CartItem> items = com.example.chillcup02_ui.util.CartManager.getInstance().getItems();
        if (items.isEmpty()) {
            items.addAll(MockData.getMockCartItems());
        }
        adapter = new CartAdapter(items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

    updateSubtotal();
        updateUiState();

        tvClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.chillcup02_ui.util.CartManager.getInstance().clear();
                adapter.getItems().clear();
                adapter.notifyDataSetChanged();
                updateSubtotal();
                updateUiState();
                Toast.makeText(requireContext(), "Đã xóa giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });

        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        com.example.chillcup02_ui.util.CartManager.getInstance().clear();
                        com.example.chillcup02_ui.util.CartManager.getInstance().getItems().addAll(MockData.getMockCartItems());
                        adapter.getItems().clear();
                        adapter.getItems().addAll(com.example.chillcup02_ui.util.CartManager.getInstance().getItems());
                        adapter.notifyDataSetChanged();
                        updateSubtotal();
                        updateUiState();
                    }
                }, 800);
            }
        });

        btnApplyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etPromo.getText().toString().trim();
                if (code.isEmpty()) {
                    Toast.makeText(requireContext(), "Nhập mã khuyến mãi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (code.equalsIgnoreCase("DISCOUNT10")) {
                    com.example.chillcup02_ui.util.CartManager.getInstance().setAppliedPromo(code);
                    btnRemovePromo.setVisibility(View.VISIBLE);
                    btnApplyPromo.setVisibility(View.GONE);
                    etPromo.setEnabled(false);
                    Toast.makeText(requireContext(), "Áp dụng mã thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Mã không hợp lệ (dùng DISCOUNT10 thử)", Toast.LENGTH_SHORT).show();
                }
                updateSubtotal();
            }
        });

        btnRemovePromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.example.chillcup02_ui.util.CartManager.getInstance().setAppliedPromo(null);
                btnRemovePromo.setVisibility(View.GONE);
                btnApplyPromo.setVisibility(View.VISIBLE);
                etPromo.setEnabled(true);
                etPromo.setText("");
                updateSubtotal();
            }
        });

        tvCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItems().isEmpty()) {
                    Toast.makeText(requireContext(), "Giỏ hàng rỗng", Toast.LENGTH_SHORT).show();
                    return;
                }
                // start CheckoutActivity to complete mock checkout
                try {
                    android.content.Intent intent = new android.content.Intent(requireContext(), com.example.chillcup02_ui.ui.checkout.CheckoutActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                    android.util.Log.e("CartFragment", "Failed to start CheckoutActivity", ex);
                    Toast.makeText(requireContext(), "Không thể mở màn thanh toán: " + ex.getClass().getSimpleName() + ": " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updateSubtotal() {
        com.example.chillcup02_ui.util.CartManager cm = com.example.chillcup02_ui.util.CartManager.getInstance();
        int subtotal = cm.getSubtotal();
        int delivery = cm.getDeliveryFee();
        int discount = cm.getDiscountAmount();

        tvSubtotal.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(subtotal));
        tvDelivery.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(delivery));
        tvDiscount.setText(com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(discount));

        int total = subtotal + delivery - discount;
        // update checkout button and summary total
        String totalStr = com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(total);
        tvCheckout.setText("Thanh toán • " + totalStr);
        // update tvTotalSummary if present
        View root = getView();
        if (root != null) {
            TextView tvSum = root.findViewById(R.id.tvTotalSummary);
            if (tvSum != null) tvSum.setText(totalStr);
        }
    }

    @Override
    public void onQuantityChanged() {
        updateSubtotal();
        updateUiState();
    }

    private void updateUiState() {
        boolean empty = adapter.getItems().isEmpty();
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
        etPromo.setVisibility(empty ? View.GONE : View.VISIBLE);
        btnApplyPromo.setVisibility(empty ? View.GONE : View.VISIBLE);
        tvCheckout.setEnabled(!empty);
        tvSubtotal.setVisibility(empty ? View.GONE : View.VISIBLE);
        tvDelivery.setVisibility(empty ? View.GONE : View.VISIBLE);
        tvDiscount.setVisibility(empty ? View.GONE : View.VISIBLE);
    }
}
