    package com.example.chillcup02_ui.ui.cart;

    import android.os.Bundle;
    import android.os.Handler;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;

    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.chillcup02_ui.R;
    import com.example.chillcup02_ui.domain.model.CartItem;
    import com.example.chillcup02_ui.util.MockData;

    import java.util.List;

    public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemListener {

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

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvCheckout = findViewById(R.id.tvCheckout);
        loadingView = findViewById(R.id.loadingView);
        errorView = findViewById(R.id.errorView);
        emptyView = findViewById(R.id.emptyView);
        tvErrorText = findViewById(R.id.tvErrorText);
        tvRetry = findViewById(R.id.tvRetry);
        tvClearCart = findViewById(R.id.tvClearCart);
        etPromo = findViewById(R.id.etPromo);
        btnApplyPromo = findViewById(R.id.btnApplyPromo);
        btnRemovePromo = findViewById(R.id.btnRemovePromo);
        tvDelivery = findViewById(R.id.tvDelivery);
        tvDiscount = findViewById(R.id.tvDiscount);

            // use shared CartManager
            List<CartItem> items = com.example.chillcup02_ui.util.CartManager.getInstance().getItems();
            if (items.isEmpty()) {
                items.addAll(MockData.getMockCartItems());
            }

            adapter = new CartAdapter(items, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                    Toast.makeText(CartActivity.this, "Đã xóa giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            });

            tvRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // simulate reload
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
                        Toast.makeText(CartActivity.this, "Nhập mã khuyến mãi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // simple mock promo: DISCOUNT10 => 10% off
                    if (code.equalsIgnoreCase("DISCOUNT10")) {
                        com.example.chillcup02_ui.util.CartManager.getInstance().setAppliedPromo(code);
                        btnRemovePromo.setVisibility(View.VISIBLE);
                        btnApplyPromo.setVisibility(View.GONE);
                        etPromo.setEnabled(false);
                        Toast.makeText(CartActivity.this, "Áp dụng mã thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CartActivity.this, "Mã không hợp lệ (dùng DISCOUNT10 thử)", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CartActivity.this, "Giỏ hàng rỗng", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        startActivity(new android.content.Intent(CartActivity.this, com.example.chillcup02_ui.ui.checkout.CheckoutActivity.class));
                    } catch (Exception ex) {
                        android.util.Log.e("CartActivity", "Failed to start CheckoutActivity", ex);
                        Toast.makeText(CartActivity.this, "Không thể mở màn thanh toán: " + ex.getClass().getSimpleName() + ": " + ex.getMessage(), Toast.LENGTH_LONG).show();
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
            tvCheckout.setText("Thanh toán • " + com.example.chillcup02_ui.util.CurrencyUtils.formatVnd(total));
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
