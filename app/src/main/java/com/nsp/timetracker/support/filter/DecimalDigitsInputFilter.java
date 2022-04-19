package com.nsp.timetracker.support.filter;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalDigitsInputFilter implements InputFilter {
    private static final int DEFAULT_DIGITS_AFTER = 2;

    private int decimalDigits;

    public DecimalDigitsInputFilter() {
        init(DEFAULT_DIGITS_AFTER);
    }

    public DecimalDigitsInputFilter(boolean fractionalEnabled) {
        init(fractionalEnabled ? DEFAULT_DIGITS_AFTER : 0);
    }

    public DecimalDigitsInputFilter(int digitsAfterZero) {
        init(digitsAfterZero);
    }

    private void init(int digitsAfterZero) {
        decimalDigits = digitsAfterZero;
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int start,
                               int end,
                               Spanned dest,
                               int dstart,
                               int dend) {
        int dotPos = -1;
        int len = dest.length();

        for (int i = 0; i < len; i++) {
            char c = dest.charAt(i);
            if (c == '.' || c == ',') {
                dotPos = i;
                break;
            }
        }

        if (dotPos >= 0) {
            // protects against many dots
            if (source.equals(".") || source.equals(",")) {
                return "";
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }

        return null;
    }
}