package com.example.chillcup02_ui.util;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceUtil {

    public static String formatPrice(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    public static String formatPrice(int price) {
        return formatPrice((double) price);
    }
}
