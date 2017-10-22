package com.hellotractor.icomparecrypts.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hellotractor.icomparecrypts.OnExchangeRateDownloaded;
import com.hellotractor.icomparecrypts.R;
import com.hellotractor.icomparecrypts.enums.CurrencySymbols;
import com.hellotractor.icomparecrypts.model.ExchangeRateItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Abdulmajid on 10/21/17.
 */

public class REST {

    //e.g. https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=BTC,USD,EUR
    public static final String CRYPTOCOMPARE_BASE_URL = "https://www.cryptocompare.com";
    public static final String CRYPTOCOMPARE_EXCHANGE_RATE_ROUTE = "https://min-api.cryptocompare.com/data/price?";
    public static final String CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_FROM = "fsym=";
    public static final String AND = "&";
    public static final String CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_TO = "tsyms=";
    public static final String CRYPTOCOMPARE_COINLIST_ROUTE = "https://www.cryptocompare.com/api/data/coinlist/";
    private OnExchangeRateDownloaded onExchangeRateDownloaded;
    private ICompareCryptsAsync iCompareCryptsAsync;

    Context mContext;

    public REST(Context mContext) {
        this.mContext = mContext;
    }

    public void fetchExchangeRate(String fromCurrency, String toCurrency, OnExchangeRateDownloaded onExchangeRateDownloaded){
        this.onExchangeRateDownloaded = onExchangeRateDownloaded;
        iCompareCryptsAsync = new ICompareCryptsAsync();
        iCompareCryptsAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fromCurrency, toCurrency);
    }

    public void stopFetchingExchangeRate(){
        if (iCompareCryptsAsync != null){
            if (!iCompareCryptsAsync.isCancelled()){
                iCompareCryptsAsync.cancel(true);
            }
        }
    }

    private class ICompareCryptsAsync extends AsyncTask<String, Integer, String> {
        private String toSymbol;
        private String fromSymbol;
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                fromSymbol = params[0];
                toSymbol = params[1];
                String completeRoute = CRYPTOCOMPARE_EXCHANGE_RATE_ROUTE + CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_FROM +
                        fromSymbol + AND + CRYPTOCOMPARE_EXCHANGE_RATE_PRICE_ENDPOINT_TO + toSymbol;
                Log.e("C", completeRoute);
                URL url = new URL(completeRoute);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (IOException e) {
                Log.e("IOException: ", e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e("IOException: ", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ExchangeRateItem exchangeRateItem = null;
            if (result != null) {
                JSONObject exchangeRate = null;
                try {
                    exchangeRate = new JSONObject(result);
                    Log.e("C2", result);
                    exchangeRateItem = new ExchangeRateItem();
                    exchangeRateItem.setFromAmount(1);
                    exchangeRateItem.setFromCurrencyFullname(CurrencySymbols.valueOf(fromSymbol).fullName());
                    exchangeRateItem.setToCurrencyFullname(CurrencySymbols.valueOf(toSymbol).fullName());
                    exchangeRateItem.setToAmount(exchangeRate.optDouble(toSymbol, 0.0));
                    exchangeRateItem.setFromCurrencySymbol(fromSymbol);
                    exchangeRateItem.setToCurrencySymbol(toSymbol);

                    new ICompareCryptsImageAsync(exchangeRateItem).executeOnExecutor(THREAD_POOL_EXECUTOR);

                } catch (JSONException err){
                    if (onExchangeRateDownloaded != null){
                        onExchangeRateDownloaded.setOnExchangeRateDownloaded(null);
                    }
                }
            } else {
                if (onExchangeRateDownloaded != null){
                    onExchangeRateDownloaded.setOnExchangeRateDownloaded(null);
                }
            }
        }
    }

    private class ICompareCryptsImageAsync extends AsyncTask<String, Integer, String> {

        ExchangeRateItem exchangeRateItem;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        private ICompareCryptsImageAsync(ExchangeRateItem exchangeRateItem){
            this.exchangeRateItem = exchangeRateItem;
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(CRYPTOCOMPARE_COINLIST_ROUTE);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (IOException e) {
                Log.e("IOException: ", e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.e("IOException: ", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                JSONObject coinlistObject = null;
                try {
                    coinlistObject = new JSONObject(result);
                    Log.e("C2", result);

                    String baseURL = coinlistObject.optString("BaseImageUrl", CRYPTOCOMPARE_BASE_URL);

                    JSONObject coinlistData = coinlistObject.getJSONObject("Data");

                    if (isCoin(exchangeRateItem.getFromCurrencySymbol())){
                        exchangeRateItem.setFromImageURL(baseURL + coinlistData.optJSONObject(exchangeRateItem.getFromCurrencySymbol()).optString("ImageUrl"));
                    } else {
                        exchangeRateItem.setFromImageURL(getWorldCurrencyImage((CurrencySymbols.valueOf(exchangeRateItem.getFromCurrencySymbol()))));
                    }

                    if (isCoin(exchangeRateItem.getToCurrencySymbol())){
                        exchangeRateItem.setToImageURL(baseURL + coinlistData.optJSONObject(exchangeRateItem.getToCurrencySymbol()).optString("ImageUrl"));
                    } else {
                        exchangeRateItem.setToImageURL(getWorldCurrencyImage((CurrencySymbols.valueOf(exchangeRateItem.getToCurrencySymbol()))));
                    }

                    if (onExchangeRateDownloaded != null){
                        onExchangeRateDownloaded.setOnExchangeRateDownloaded(exchangeRateItem);
                    }
                } catch (JSONException err){
                    if (onExchangeRateDownloaded != null){
                        onExchangeRateDownloaded.setOnExchangeRateDownloaded(null);
                    }
                }
            } else {
                if (onExchangeRateDownloaded != null){
                    onExchangeRateDownloaded.setOnExchangeRateDownloaded(null);
                }
            }
        }
    }

    private boolean isCoin(String currencySymbol){
        return currencySymbol.equalsIgnoreCase("BTC") || currencySymbol.equalsIgnoreCase("ETH");
    }

    private String getWorldCurrencyImage(CurrencySymbols currencySymbol){
        String assetFolder = "file:///android_asset/currencies/";
        if (currencySymbol == CurrencySymbols.NGN){
            return assetFolder + "beautiful-country-currency-nigeria.jpg";
        } else if (currencySymbol == CurrencySymbols.BRL){
            return assetFolder + "beautiful-country-currency-brazil.jpg";
        } else if (currencySymbol == CurrencySymbols.AUD){
            return assetFolder + "beautiful-country-currency-australia.jpg";
        } else if (currencySymbol == CurrencySymbols.GBP){
            return assetFolder + "beautiful-country-currency-england_pound.jpg";
        } else if (currencySymbol == CurrencySymbols.CAD){
            return assetFolder + "beautiful-country-currency-canada.jpg";
        } else if (currencySymbol == CurrencySymbols.CLP){
            return assetFolder + "beautiful-country-currency-chile.jpg";
        } else if (currencySymbol == CurrencySymbols.CNY){
            return assetFolder + "beautiful-country-currency-china_yuan.jpg";
        } else if (currencySymbol == CurrencySymbols.CZK){
            return assetFolder + "2000-czech-koruna-banknote-series-1996.jpg";
        } else if (currencySymbol == CurrencySymbols.EUR){
            return assetFolder + "beautiful-country-currency-euro.jpg";
        } else if (currencySymbol == CurrencySymbols.GHS){
            return assetFolder + "50GHS-front.jpg";
        } else if (currencySymbol == CurrencySymbols.HKD){
            return assetFolder + "beautiful-country-currency-hong_kong_dollar.jpg";
        } else if (currencySymbol == CurrencySymbols.INR){
            return assetFolder + "beautiful-country-currency-india.jpg";
        } else if (currencySymbol == CurrencySymbols.IDR){
            return assetFolder + "idr-100000-indonesian-rupiahs-2.jpg";
        } else if (currencySymbol == CurrencySymbols.ILS){
            return assetFolder + "beautiful-country-currency-israel.jpg";
        } else if (currencySymbol == CurrencySymbols.KRW){
            return assetFolder + "south-korea-50000-won.jpg";
        } else if (currencySymbol == CurrencySymbols.RUB){
            return assetFolder + "beautiful-country-currency-russia.jpg";
        } else if (currencySymbol == CurrencySymbols.MYR){
            return assetFolder + "beautiful-country-currency-malaysia.jpg";
        } else if (currencySymbol == CurrencySymbols.MXN){
            return assetFolder + "beautiful-country-currency-mexico.jpg";
        } else if (currencySymbol == CurrencySymbols.CHF){
            return assetFolder + "beautiful-country-currency-switzerland.jpg";
        } else if (currencySymbol == CurrencySymbols.ZAR){
            return assetFolder + "zar-10-south-african-rands-2.jpg";
        } else if (currencySymbol == CurrencySymbols.USD){
            return assetFolder + "usd-5-us-dollars-2.jpg";
        }
        return null;
    }
}
