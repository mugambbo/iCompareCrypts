package com.hellotractor.icomparecrypts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hellotractor.icomparecrypts.database.DBOPS;
import com.hellotractor.icomparecrypts.enums.CryptoCurrencySymbols;
import com.hellotractor.icomparecrypts.enums.WorldCurrencySymbols;
import com.hellotractor.icomparecrypts.model.ExchangeRateItem;
import com.hellotractor.icomparecrypts.network.VolleyClient;
import com.hellotractor.icomparecrypts.util.DateTimeUtil;
import com.hellotractor.icomparecrypts.util.Fonts;
import com.hellotractor.icomparecrypts.util.WorldCurrencyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ExchangeRateItem> currencyExchangeList;
    private CurrencyListAdapter currencyListAdapter;
    ArrayList<String> fromCurrencies = new ArrayList<>();
    ArrayList<String> toCurrencies = new ArrayList<>();
    boolean toWorldCurrency = true;

    private ProgressBar progressBar;
    FloatingActionButton fab;
    private TextView mNoExchangeRatesTV;
    public static final String CRYPTOCOMPARE_BASE_URL = "https://www.cryptocompare.com";
    public static final String CRYPTOCOMPARE_EXCHANGE_RATE_ROUTE = "https://min-api.cryptocompare.com/data/price?";
    public static final String CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_FROM = "fsym=";
    public static final String AND = "&";
    public static final String CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_TO = "tsyms=";
    public static final String CRYPTOCOMPARE_COINLIST_ROUTE = "https://www.cryptocompare.com/api/data/coinlist/";
    private ExchangeRateItem exchangeRateItem;
    private DBOPS dbops;
    Handler refreshHandler = new Handler();
    Runnable refreshRunnable;

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

        dbops = new DBOPS(getApplicationContext());
        retrieveOldRates();
    }

    private void showCurrencyConversionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = View.inflate(MainActivity.this, R.layout.currency_conversion_dialog, null);
        final Spinner mToSpinner = (Spinner) mView.findViewById(R.id.to_spinner);
        final Spinner mFromSpinner = (Spinner) mView.findViewById(R.id.from_spinner);

        if (fromCurrencies.isEmpty()) {
            for (CryptoCurrencySymbols cryptoCurrencySymbols : CryptoCurrencySymbols.values()) {
                fromCurrencies.add(cryptoCurrencySymbols.fullName());
            }
        }

        if (toCurrencies.isEmpty()) {
            for (WorldCurrencySymbols worldCurrencySymbols : WorldCurrencySymbols.values()) {
                toCurrencies.add(worldCurrencySymbols.fullName());
            }
        }
        mFromSpinner.setTag("crypto");
        mToSpinner.setTag("world");
        mToSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toCurrencies));
        mFromSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fromCurrencies));


        ImageView mSwitchConversion = (ImageView) mView.findViewById(R.id.switch_conversion);

        mSwitchConversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toWorldCurrency){
                    mFromSpinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, toCurrencies));
                    mToSpinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, fromCurrencies));
                    toWorldCurrency = false;
                    mFromSpinner.setTag("world");
                    mToSpinner.setTag("crypto");
                } else {
                    mToSpinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, toCurrencies));
                    mFromSpinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, fromCurrencies));
                    toWorldCurrency = true;
                    mFromSpinner.setTag("crypto");
                    mToSpinner.setTag("world");
                }
            }
        });


        builder.setView(mView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Pull latest exchange rates
                        toggleProgressBar(true);
                        fab.setEnabled(false);
                        if (mFromSpinner.getTag().equals("crypto") && mToSpinner.getTag().equals("world")){
                            fetchExchangeRate(CryptoCurrencySymbols.values()[mFromSpinner.getSelectedItemPosition()].name(), WorldCurrencySymbols.values()[mToSpinner.getSelectedItemPosition()].name());
                        } else {
                            fetchExchangeRate(WorldCurrencySymbols.values()[mFromSpinner.getSelectedItemPosition()].name(), CryptoCurrencySymbols.values()[mToSpinner.getSelectedItemPosition()].name());
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private boolean isCoin(String symbol){
        for (int i = 0; i < CryptoCurrencySymbols.values().length; i++){
            if (symbol.equalsIgnoreCase(CryptoCurrencySymbols.values()[i].name())){
                return true;
            }
        }
        return false;
    }


    private void fetchExchangeRate(final String fromSymbol, final String toSymbol){

        exchangeRateItem = new ExchangeRateItem();
        final JsonObjectRequest imageRequest = new JsonObjectRequest(Request.Method.GET, CRYPTOCOMPARE_COINLIST_ROUTE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        String baseURL = response.optString("BaseImageUrl", CRYPTOCOMPARE_BASE_URL);
                        JSONObject coinlistData = response.getJSONObject("Data");
                        Log.e("iCompare", coinlistData.toString());
                        Log.e("iCompare", "From: "+fromSymbol);
                        Log.e("iCompare", "To: "+toSymbol);

                        if (isCoin(fromSymbol)) {
                            exchangeRateItem.setFromImageURL(baseURL + coinlistData.optJSONObject(fromSymbol).optString("ImageUrl"));
                            exchangeRateItem.setToImageURL(WorldCurrencyUtil.getWorldCurrencyImage((WorldCurrencySymbols.valueOf(toSymbol))));
                        } else {
                            exchangeRateItem.setFromImageURL(WorldCurrencyUtil.getWorldCurrencyImage((WorldCurrencySymbols.valueOf(fromSymbol))));
                            exchangeRateItem.setToImageURL(baseURL + coinlistData.optJSONObject(toSymbol).optString("ImageUrl"));
                        }

                        currencyExchangeList.add(exchangeRateItem);
                        currencyListAdapter.notifyDataSetChanged();

                    } catch (JSONException err){
                        err.printStackTrace();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Unable to fetch exchange rates. Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
                toggleProgressBar(false);
                if (!currencyExchangeList.isEmpty()){
                    mNoExchangeRatesTV.setVisibility(View.GONE);
                } else {
                    mNoExchangeRatesTV.setVisibility(View.VISIBLE);
                }
                fab.setEnabled(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Unable to fetch exchange rates. Check your internet connection.", Toast.LENGTH_SHORT).show();
                fab.setEnabled(true);
            }
        });

        String completeRoute = CRYPTOCOMPARE_EXCHANGE_RATE_ROUTE + CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_FROM +
                fromSymbol + AND + CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_TO + toSymbol;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, completeRoute, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    exchangeRateItem = new ExchangeRateItem();
                    exchangeRateItem.setFromAmount(1);

                    if (isCoin(fromSymbol)) {
                        exchangeRateItem.setFromCurrencyFullname(CryptoCurrencySymbols.valueOf(fromSymbol).fullName());
                        exchangeRateItem.setToCurrencyFullname(WorldCurrencySymbols.valueOf(toSymbol).fullName());
                    } else {
                        exchangeRateItem.setFromCurrencyFullname(WorldCurrencySymbols.valueOf(fromSymbol).fullName());
                        exchangeRateItem.setToCurrencyFullname(CryptoCurrencySymbols.valueOf(toSymbol).fullName());

                    }

                    exchangeRateItem.setToAmount(response.optDouble(toSymbol, 0.0));
                    exchangeRateItem.setFromCurrencySymbol(fromSymbol);
                    exchangeRateItem.setToCurrencySymbol(toSymbol);
                    exchangeRateItem.setUpdated(DateTimeUtil.todaysDateTime());

                    VolleyClient.getInstance(MainActivity.this).addToRequestQueue(imageRequest);

                } else {
                    Toast.makeText(MainActivity.this, "Unable to fetch exchange rates. Check your internet connection.", Toast.LENGTH_SHORT).show();
                }
                toggleProgressBar(false);
                if (!currencyExchangeList.isEmpty()){
                    mNoExchangeRatesTV.setVisibility(View.GONE);
                } else {
                    mNoExchangeRatesTV.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.setSequence(0);

        VolleyClient.getInstance(this).addToRequestQueue(request);
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
        } else if(id == R.id.action_clear) {
            dbops.clearExchangeRates();
            retrieveOldRates();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void toggleProgressBar(boolean show){
        if (show){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void retrieveOldRates(){
        currencyExchangeList.clear();
        if (!dbops.getExchangeRateItems().isEmpty()){
            currencyExchangeList.addAll(dbops.getExchangeRateItems());
        }

        currencyListAdapter.notifyDataSetChanged();
        if (!currencyExchangeList.isEmpty()){
            mNoExchangeRatesTV.setVisibility(View.GONE);
        } else {
            mNoExchangeRatesTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbops.clearExchangeRates();
        dbops.insertExchangeRate(currencyExchangeList);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                currencyListAdapter.notifyDataSetChanged();
                refreshHandler.postDelayed(refreshRunnable, 1000);
            }
        };
        refreshHandler.postDelayed(refreshRunnable, 1000);
    }
}
