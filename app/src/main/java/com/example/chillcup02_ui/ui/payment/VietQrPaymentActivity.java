package com.example.chillcup02_ui.ui.payment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chillcup02_ui.R;
import com.example.chillcup02_ui.util.CurrencyUtils;

public class VietQrPaymentActivity extends AppCompatActivity {

    private TextView tvOrderNumber, tvOrderTotal, tvCountdown, tvQrSmallInfo;
    private ImageView ivQrCode;
    private CountDownTimer timer;
    private int seconds = 6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vietqr_payment);

        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        tvCountdown = findViewById(R.id.tvCountdown);
        ivQrCode = findViewById(R.id.ivQrCode);
        tvQrSmallInfo = findViewById(R.id.tvQrSmallInfo);

        Intent i = getIntent();
        String orderNumber = i.getStringExtra("order_number");
        long total = i.getLongExtra("order_total_raw", 0);

        if (orderNumber == null || orderNumber.isEmpty()) {
            orderNumber = "ORD-" + System.currentTimeMillis();
        }

        tvOrderNumber.setText(orderNumber);
        tvOrderTotal.setText(CurrencyUtils.formatVnd(total));
        tvQrSmallInfo.setText("Số tiền: " + CurrencyUtils.formatVnd(total));

        // start a simple countdown and then return RESULT_OK to caller
        timer = new CountDownTimer(seconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long s = millisUntilFinished / 1000;
                tvCountdown.setText(s + "s");
            }

            public void onFinish() {
                // simulate redirect: report success
                Intent out = new Intent();
                setResult(Activity.RESULT_OK, out);
                finish();
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) timer.cancel();
    }
}
