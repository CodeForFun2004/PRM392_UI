package com.example.chillcup02_ui.util;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtils {
    private static final Locale VI = new Locale("vi", "VN");

    public static String formatVnd(long amount) {
        try {
            // The app stores amounts in 'thousands' in many places; user asked to show 3 extra zeros.
            long real = amount * 1000L; // add three zeros
            NumberFormat nf = NumberFormat.getInstance(VI);
            return nf.format(real) + " đ";
        } catch (Exception ex) {
            return String.valueOf(amount) + "000 đ";
        }
    }

    public static String formatVnd(int amount) { return formatVnd((long) amount); }
}
