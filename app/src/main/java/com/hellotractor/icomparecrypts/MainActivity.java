package com.hellotractor.icomparecrypts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hellotractor.icomparecrypts.enums.CurrencySymbols;
import com.hellotractor.icomparecrypts.model.ExchangeRateItem;
import com.hellotractor.icomparecrypts.network.REST;
import com.hellotractor.icomparecrypts.util.Fonts;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnExchangeRateDownloaded {

    private ArrayList<ExchangeRateItem> currencyExchangeList;
    private CurrencyListAdapter currencyListAdapter;
    private REST rest = new REST(MainActivity.this);
    private ProgressBar progressBar;
    FloatingActionButton fab;
    private TextView mNoExchangeRatesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView mCurrencyListView = (ListView)findViewById(R.id.currency_exchange_rate_list);
        currencyExchangeList = new ArrayList<>();
        currencyListAdapter = new CurrencyListAdapter(MainActivity.this, currencyExchangeList);
        mCurrencyListView.setAdapter(currencyListAdapter);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        mNoExchangeRatesTV = (TextView) findViewById(R.id.no_exchange_rates);
        mNoExchangeRatesTV.setTypeface(Fonts.subtitleFont(this));

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCurrencyConversionDialog();
            }
        });
    }

    private void showCurrencyConversionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = View.inflate(MainActivity.this, R.layout.currency_conversion_dialog, null);
        final Spinner mToSpinner = (Spinner) mView.findViewById(R.id.to_spinner);
        final Spinner mFromSpinner = (Spinner) mView.findViewById(R.id.from_spinner);
        ArrayList<String> fromCurrencies = new ArrayList<>();
        ArrayList<String> toCurrencies = new ArrayList<>();

        for (CurrencySymbols currencySymbols : CurrencySymbols.values()){
            toCurrencies.add(currencySymbols.fullName());
            fromCurrencies.add(currencySymbols.fullName());
        }

        mToSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toCurrencies));
        mFromSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fromCurrencies));

        mToSpinner.setSelection(2); //Select Nigerian naira

        builder.setTitle("Select currencies to compare")
                .setIcon(R.mipmap.ic_launcher_round)
                .setView(mView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Pull latest exchange rates
                        toggleProgressBar(true);
                        rest.fetchExchangeRate(CurrencySymbols.values()[mFromSpinner.getSelectedItemPosition()].name(),
                                CurrencySymbols.values()[mToSpinner.getSelectedItemPosition()].name(), MainActivity.this);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOnExchangeRateDownloaded(ExchangeRateItem exchangeRateItem) {
        if (exchangeRateItem != null){
            currencyExchangeList.add(exchangeRateItem);
            currencyListAdapter.notifyDataSetChanged();
//            Snackbar.make(fab, "Exchange rate from "+exchangeRateItem.getFromCurrencySymbol() + " to "+exchangeRateItem.getToCurrencySymbol()+" retrieved successfully.", Snackbar.LENGTH_LONG)
//                    .show();
        } else {
            Toast.makeText(this, "Unable to fetch exchange rates. Check your internet connection.", Toast.LENGTH_SHORT).show();
        }
        toggleProgressBar(false);
        if (!currencyExchangeList.isEmpty()){
            mNoExchangeRatesTV.setVisibility(View.GONE);
        } else {
            mNoExchangeRatesTV.setVisibility(View.VISIBLE);
        }
    }

    private void toggleProgressBar(boolean show){
        if (show){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
