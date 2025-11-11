package com.example.chillcup02_ui.ui.checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.chillcup02_ui.util.CurrencyUtils;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;
import android.widget.FrameLayout;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chillcup02_ui.MainActivity;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.domain.model.CartItem;
import com.example.chillcup02_ui.domain.model.Order;
import com.example.chillcup02_ui.util.CartManager;
import com.example.chillcup02_ui.util.MockOrders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity implements InformationDialogFragment.InfoDialogListener {

	private TextView tvAddress, tvPhone;
	private TextView tvSubtotal, tvDelivery, tvTax, tvTotal, tvEta;
	private Button btnPlaceOrder;
	private ImageButton btnBack;
	private ImageButton btnEditAddress;
	private ImageButton btnEditPhone;
	private CardView cardVietQr;
	private CardView cardCOD;
	private android.widget.ImageView ivVietQrCheck;
	private android.widget.ImageView ivCodCheck;
	private LinearLayout llItems;
	private FrameLayout loadingOverlay;
	private TextView tvLoadingText;
	private String selectedPayment = "cod"; // default

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);

	tvAddress = findViewById(R.id.tvAddress);
	tvPhone = findViewById(R.id.tvPhone);
	btnBack = findViewById(R.id.btnBack);
	btnEditAddress = findViewById(R.id.btnEditAddress);
	btnEditPhone = findViewById(R.id.btnEditPhone);
    	cardVietQr = findViewById(R.id.cardVietQr);
    	cardCOD = findViewById(R.id.cardCOD);
    	ivVietQrCheck = findViewById(R.id.ivVietQrCheck);
    	ivCodCheck = findViewById(R.id.ivCodCheck);
	llItems = findViewById(R.id.llItems);
	loadingOverlay = findViewById(R.id.loadingOverlay);
	tvLoadingText = findViewById(R.id.tvLoadingText);
	tvSubtotal = findViewById(R.id.tvSubtotal);
	tvDelivery = findViewById(R.id.tvDelivery);
	tvTax = findViewById(R.id.tvTax);
	tvTotal = findViewById(R.id.tvTotal);
	tvEta = findViewById(R.id.tvEta);
		btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

		btnBack.setOnClickListener(v -> finish());
		// open edit modal for address/phone
		btnEditAddress.setOnClickListener(v -> {
			InformationDialogFragment f = InformationDialogFragment.newInstance("location", tvAddress.getText().toString());
			f.show(getSupportFragmentManager(), "info");
		});
		btnEditPhone.setOnClickListener(v -> {
			InformationDialogFragment f = InformationDialogFragment.newInstance("phone", tvPhone.getText().toString());
			f.show(getSupportFragmentManager(), "info");
		});

	refreshSummary();
	populateItems();
	updatePaymentSelection();

		// payment card clicks
		cardVietQr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedPayment = "vietqr";
				updatePaymentSelection();
			}
		});
		cardCOD.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedPayment = "cod";
				updatePaymentSelection();
			}
		});

		btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptPlaceOrder();
			}
		});

	}
	@Override
	public void onInfoSaved(String field, String value) {
		if ("phone".equals(field)) {
			tvPhone.setText(value);
		} else {
			tvAddress.setText(value);
		}
	}

	private void updatePaymentSelection() {
		// default backgrounds
		cardVietQr.setBackgroundResource(R.drawable.bg_payment_card);
		cardCOD.setBackgroundResource(R.drawable.bg_payment_card);
		ivVietQrCheck.setVisibility(View.GONE);
		ivCodCheck.setVisibility(View.GONE);

		if ("vietqr".equals(selectedPayment)) {
			cardVietQr.setBackgroundResource(R.drawable.bg_payment_card_selected);
			ivVietQrCheck.setVisibility(View.VISIBLE);
		} else {
			cardCOD.setBackgroundResource(R.drawable.bg_payment_card_selected);
			ivCodCheck.setVisibility(View.VISIBLE);
		}
	}


	private void refreshSummary() {
		CartManager cm = CartManager.getInstance();
		tvSubtotal.setText(CurrencyUtils.formatVnd(cm.getSubtotal()));
	tvDelivery.setText(CurrencyUtils.formatVnd(cm.getDeliveryFee()));
	tvTax.setText(CurrencyUtils.formatVnd(cm.getDiscountAmount()));
	tvTotal.setText(CurrencyUtils.formatVnd(cm.getTotal()));
		tvEta.setText("⏱ Thời gian giao hàng dự kiến: 25-35 phút");
	}

	private void populateItems() {
		llItems.removeAllViews();
		CartManager cm = CartManager.getInstance();
		for (CartItem ci : cm.getItems()) {
			LinearLayout row = new LinearLayout(this);
			row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			row.setOrientation(LinearLayout.HORIZONTAL);
			int pad = (int) (4 * getResources().getDisplayMetrics().density);
			row.setPadding(0, pad, 0, pad);

			TextView tvName = new TextView(this);
			tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
			tvName.setText(ci.product != null ? ci.product.name : "Item");

			TextView tvQtyPrice = new TextView(this);
			tvQtyPrice.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			tvQtyPrice.setText(ci.qty + " x $" + ci.unitPrice);

			row.addView(tvName);
			row.addView(tvQtyPrice);
			llItems.addView(row);
		}
	}

	private void attemptPlaceOrder() {
	String address = tvAddress.getText().toString().trim();
	String phone = tvPhone.getText().toString().trim();
		if (address.isEmpty() || phone.isEmpty()) {
			Toast.makeText(this, "Nhập địa chỉ và số điện thoại", Toast.LENGTH_SHORT).show();
			return;
		}

		CartManager cm = CartManager.getInstance();
		if (cm.getItems().isEmpty()) {
			Toast.makeText(this, "Giỏ hàng rỗng", Toast.LENGTH_SHORT).show();
			return;
		}

		// If user selected VietQR, open the QR payment screen (simulate)
		if ("vietqr".equals(selectedPayment)) {
			android.content.Intent intent = new android.content.Intent(this, com.example.chillcup02_ui.ui.payment.VietQrPaymentActivity.class);
			// pass total so payment screen can display amount
			intent.putExtra("order_total_raw", cm.getTotal());
			startActivityForResult(intent, 1234);
			return;
		}

		// show loading overlay and simulate network delay for COD
		loadingOverlay.setVisibility(View.VISIBLE);
		btnPlaceOrder.setEnabled(false);

		// Prepare network payload
		com.example.chillcup02_ui.network.model.OrderRequest req = new com.example.chillcup02_ui.network.model.OrderRequest();
		req.deliveryAddress = address;
		req.phone = phone;
		req.paymentMethod = selectedPayment;
		java.util.ArrayList<com.example.chillcup02_ui.network.model.OrderRequest.Item> list = new java.util.ArrayList<>();
		for (CartItem ci : cm.getItems()) {
			com.example.chillcup02_ui.network.model.OrderRequest.Item it = new com.example.chillcup02_ui.network.model.OrderRequest.Item();
			it.name = ci.product != null ? ci.product.name : "Item";
			it.quantity = ci.qty;
			it.price = ci.unitPrice;
			list.add(it);
		}
		req.items = list;
		req.total = cm.getTotal();

		com.example.chillcup02_ui.network.ApiService api = com.example.chillcup02_ui.network.ApiClient.service();
		api.createOrder(req).enqueue(new retrofit2.Callback<com.example.chillcup02_ui.network.model.OrderResponse>() {
			@Override
			public void onResponse(retrofit2.Call<com.example.chillcup02_ui.network.model.OrderResponse> call, retrofit2.Response<com.example.chillcup02_ui.network.model.OrderResponse> response) {
				if (response.isSuccessful() && response.body() != null && response.body().order != null) {
					// map network order to domain order and open detail
					com.example.chillcup02_ui.network.model.OrderResponse.Order net = response.body().order;
					com.example.chillcup02_ui.domain.model.Order ord = new com.example.chillcup02_ui.domain.model.Order();
					ord._id = net._id;
					ord.orderNumber = net.orderNumber;
					ord.createdAt = java.time.Instant.now().toString();
					ord.status = net.status != null ? net.status : "pending";
					ord.paymentMethod = net.paymentMethod;
					ord.total = (int) net.total;
					ord.deliveryAddress = net.deliveryAddress;
					ord.phone = net.phone;
					// convert items
					if (net.items != null) {
						java.util.ArrayList<com.example.chillcup02_ui.domain.model.Order.OrderItem> list = new java.util.ArrayList<>();
						for (com.example.chillcup02_ui.network.model.OrderRequest.Item it : net.items) {
							com.example.chillcup02_ui.domain.model.Order.OrderItem oi = new com.example.chillcup02_ui.domain.model.Order.OrderItem();
							oi.name = it.name;
							oi.quantity = it.quantity;
							oi.price = (int) Math.round(it.price);
							list.add(oi);
						}
						ord.items = list;
					}
					// navigate to OrderDetailActivity
					cm.clear();
					Intent intent = new Intent(CheckoutActivity.this, com.example.chillcup02_ui.ui.orders.OrderDetailActivity.class);
					intent.putExtra("order", ord);
					startActivity(intent);
					finish();
				} else {
					// fallback to local mock creation
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							createAndSubmitOrder();
						}
					}, 300);
				}
			}

			@Override
			public void onFailure(retrofit2.Call<com.example.chillcup02_ui.network.model.OrderResponse> call, Throwable t) {
				// fallback local
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						createAndSubmitOrder();
					}
				}, 300);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1234) {
			if (resultCode == RESULT_OK) {
				// Payment succeeded at gateway; create the order
				createAndSubmitOrder();
			} else {
				// Payment cancelled
				Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void createAndSubmitOrder() {
	String address = tvAddress.getText().toString().trim();
	String phone = tvPhone.getText().toString().trim();

		CartManager cm = CartManager.getInstance();

		Order order = new Order();
		order._id = null; // generated by MockOrders
		order.orderNumber = "ORD-" + System.currentTimeMillis();
		order.createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(new Date());
		order.status = "processing";
		order.total = cm.getTotal();
		order.deliveryTime = "TBD";
		order.deliveryAddress = address;
		order.phone = phone;
		order.paymentMethod = "vietqr".equals(selectedPayment) ? "vietqr" : "cod";

		List<Order.OrderItem> items = new ArrayList<>();
		for (CartItem ci : cm.getItems()) {
			Order.OrderItem oi = new Order.OrderItem();
			oi.name = ci.product != null ? ci.product.name : "Item";
			oi.quantity = ci.qty;
			oi.price = ci.unitPrice;
			items.add(oi);
		}
		order.items = items;


		// Try to submit to mock server first
		com.example.chillcup02_ui.network.model.OrderRequest req = new com.example.chillcup02_ui.network.model.OrderRequest();
		req.deliveryAddress = address;
		req.phone = phone;
		req.paymentMethod = order.paymentMethod;
		java.util.ArrayList<com.example.chillcup02_ui.network.model.OrderRequest.Item> list = new java.util.ArrayList<>();
		for (Order.OrderItem oi : order.items) {
			com.example.chillcup02_ui.network.model.OrderRequest.Item it = new com.example.chillcup02_ui.network.model.OrderRequest.Item();
			it.name = oi.name;
			it.quantity = oi.quantity;
			it.price = oi.price;
			list.add(it);
		}
		req.items = list;
		req.total = order.total;

		com.example.chillcup02_ui.network.ApiService api = com.example.chillcup02_ui.network.ApiClient.service();
		api.createOrder(req).enqueue(new retrofit2.Callback<com.example.chillcup02_ui.network.model.OrderResponse>() {
			@Override
			public void onResponse(retrofit2.Call<com.example.chillcup02_ui.network.model.OrderResponse> call, retrofit2.Response<com.example.chillcup02_ui.network.model.OrderResponse> response) {
				if (response.isSuccessful() && response.body() != null && response.body().order != null) {
					// success from server - map to domain Order and open detail
					com.example.chillcup02_ui.network.model.OrderResponse.Order o = response.body().order;
					com.example.chillcup02_ui.domain.model.Order ord = new com.example.chillcup02_ui.domain.model.Order();
					ord._id = o._id;
					ord.orderNumber = o.orderNumber != null ? o.orderNumber : ("ORD-" + System.currentTimeMillis());
					ord.createdAt = java.time.Instant.now().toString();
					ord.status = o.status != null ? o.status : "pending";
					ord.paymentMethod = o.paymentMethod;
					ord.total = (int) Math.round(o.total);
					ord.deliveryAddress = o.deliveryAddress;
					ord.phone = o.phone;
					if (o.items != null) {
						java.util.ArrayList<com.example.chillcup02_ui.domain.model.Order.OrderItem> list = new java.util.ArrayList<>();
						for (com.example.chillcup02_ui.network.model.OrderRequest.Item it : o.items) {
							com.example.chillcup02_ui.domain.model.Order.OrderItem oi = new com.example.chillcup02_ui.domain.model.Order.OrderItem();
							oi.name = it.name;
							oi.quantity = it.quantity;
							oi.price = (int) Math.round(it.price);
							list.add(oi);
						}
						ord.items = list;
					}
					// navigate to OrderDetailActivity
					cm.clear();
					Intent intent = new Intent(CheckoutActivity.this, com.example.chillcup02_ui.ui.orders.OrderDetailActivity.class);
					intent.putExtra("order", ord);
					startActivity(intent);
					finish();
				} else {
					// fallback to local mock
					MockOrders.addOrder(order);
					cm.clear();
					Intent intent = new Intent(CheckoutActivity.this, com.example.chillcup02_ui.ui.orders.OrderDetailActivity.class);
					intent.putExtra("order", order);
					startActivity(intent);
					finish();
				}
			}

			@Override
			public void onFailure(retrofit2.Call<com.example.chillcup02_ui.network.model.OrderResponse> call, Throwable t) {
				// fallback local - open detail screen
				MockOrders.addOrder(order);
				cm.clear();
				Intent intent = new Intent(CheckoutActivity.this, com.example.chillcup02_ui.ui.orders.OrderDetailActivity.class);
				intent.putExtra("order", order);
				startActivity(intent);
				finish();
			}
		});
	}

}
