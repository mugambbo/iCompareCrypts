package com.hellotractor.icomparecrypts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hellotractor.icomparecrypts.util.Fonts;

import java.text.NumberFormat;

public class ConversionActivity extends AppCompatActivity {

    public static final String FROM_CURRENCY_SYMBOL = "fromCurrencySymbol";
    public static final String TO_CURRENCY_SYMBOL = "toCurrencySymbol";
    public static final String FROM_CURRENCY_FULLNAME = "fromCurrencyFullname";
    public static final String TO_CURRENCY_FULLNAME = "toCurrencyFullname";
    public static final String FROM_CURRENCY_AMOUNT = "fromCurrencyAmount";
    public static final String TO_CURRENCY_AMOUNT = "toCurrencyAmount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String fromCurrencySymbol = getIntent().getStringExtra(FROM_CURRENCY_SYMBOL);
        final String toCurrencySymbol = getIntent().getStringExtra(TO_CURRENCY_SYMBOL);

        double fromCurrencyAmount = getIntent().getDoubleExtra(FROM_CURRENCY_AMOUNT, 0.0);
        final double toCurrencyAmount = getIntent().getDoubleExtra(TO_CURRENCY_AMOUNT, 0.0);

        String fromCurrencyFullname = getIntent().getStringExtra(FROM_CURRENCY_FULLNAME);
        String toCurrencyFullname = getIntent().getStringExtra(TO_CURRENCY_FULLNAME);

        TextInputLayout mTextInputLayout = (TextInputLayout)findViewById(R.id.textInputLayout);
        mTextInputLayout.setHint("Enter amount to convert to "+toCurrencySymbol);
        final TextInputEditText mAmountEditText = (TextInputEditText)findViewById(R.id.amount_edit_text);
        mAmountEditText.setTypeface(Fonts.subtitleFont(this));
        final TextView mResultTV = (TextView)findViewById(R.id.result_tv);
        mResultTV.setTypeface(Fonts.subtitleFont(this));
        TextView mConversionTitle = (TextView) findViewById(R.id.conversion_title);
        mConversionTitle.setText(fromCurrencyFullname+ " to "+toCurrencyFullname);
        mConversionTitle.setTypeface(Fonts.titleFont(this));
        final NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);

        mAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(mAmountEditText.getText().toString())){
                    mResultTV.setText("");
                } else if (!TextUtils.isDigitsOnly(mAmountEditText.getText().toString())){
                    mAmountEditText.setError("Amount to convert must be numbers only");
                    mResultTV.setText("");
                } else {
                    mResultTV.setText(String.valueOf(numberFormat.format(Double.parseDouble(mAmountEditText.getText().toString()) * toCurrencyAmount)) + " "+ toCurrencySymbol);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
