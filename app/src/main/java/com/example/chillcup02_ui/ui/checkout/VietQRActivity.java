package com.example.chillcup02_ui.ui.checkout;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chillcup02_ui.R;

public class VietQRActivity extends AppCompatActivity {

    private ImageView ivQrImage;
    private TextView tvOrderNumber, tvOrderTotal, tvBank, tvAccount, tvAccountName, tvCountdown;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vietqr);

        ivQrImage = findViewById(R.id.ivQrImage);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        tvBank = findViewById(R.id.tvBank);
        tvAccount = findViewById(R.id.tvAccount);
        tvAccountName = findViewById(R.id.tvAccountName);
        tvCountdown = findViewById(R.id.tvCountdown);

        // Populate with basic mock data. If intent extras provided, fill them.
        String orderNum = "ORD-" + System.currentTimeMillis();
        long total = getIntent().getLongExtra("total", 62800L);

        tvOrderNumber.setText(orderNum);
        // format total as VND (simple)
        tvOrderTotal.setText(String.format("%,d VND", total));

        tvBank.setText("Vietcombank");
        tvAccount.setText("19036735544018");
        tvAccountName.setText("Dinh Quoc Huy");

        // Countdown text static for now
        tvCountdown.setText("6s");
    }

}
