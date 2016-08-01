package com.example.nguyen.project2.Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by Nguyen on 30/03/2016.
 */
public class MoneyWatcher implements TextWatcher {
    private final DecimalFormat df;
    private final DecimalFormat dfnd;
    private final EditText text;
    private boolean hasFractionalPart;
    private int trailingZeroCount;

    public MoneyWatcher(EditText editText, String pattern) {
        df = new DecimalFormat(pattern);
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###.00");
        this.text = editText;
        hasFractionalPart = false;
    }

    @Override
    public void afterTextChanged(Editable s) {
        text.removeTextChangedListener(this);

        if (s != null && !s.toString().isEmpty()) {
            try {
                int inilen, endlen;
                inilen = text.getText().length();
                String v =
                        s.toString().replace(String.valueOf(df.getDecimalFormatSymbols()
                                .getGroupingSeparator()), "");
                Number n = df.parse(v);
                int cp = text.getSelectionStart();
                if (hasFractionalPart) {
                    StringBuilder trailingZeros = new StringBuilder();
                    while (trailingZeroCount-- > 0)
                        trailingZeros.append('0');
                    text.setText(df.format(n) + trailingZeros.toString());
                } else {
                    text.setText(dfnd.format(n));
                }
//                text.setText("$".concat(text.getText().toString()));
                text.setText(text.getText().toString());
//                text.setText(text.getText().toString().concat("đ"));
                endlen = text.getText().length();
                int sel = (cp + (endlen - inilen));
                if (sel > 0 && sel < text.getText().length()) {
                    text.setSelection(sel);
                } else if (trailingZeroCount > -1) {
                    text.setSelection(text.getText().length() - 3);
//                    et.setSelection(et.getText().length() - 1);
                } else {
                    text.setSelection(text.getText().length());
                }
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
        }

        text.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int index = s.toString().indexOf(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()));
        trailingZeroCount = 0;
        if (index > -1) {
            for (index++; index < s.length(); index++) {
                if (s.charAt(index) == '0')
                    trailingZeroCount++;
                else {
                    trailingZeroCount = 0;
                }
            }
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }
}
