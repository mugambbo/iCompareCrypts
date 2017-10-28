package com.hellotractor.icomparecrypts.util;

import com.hellotractor.icomparecrypts.enums.CryptoCurrencySymbols;
import com.hellotractor.icomparecrypts.enums.WorldCurrencySymbols;

/**
 * Created by abdulmajid on 10/28/17.
 */

public class WorldCurrencyUtil {

    public static String getWorldCurrencyImage(WorldCurrencySymbols currencySymbol){
        String assetFolder = "file:///android_asset/currencies/";
        if (currencySymbol == WorldCurrencySymbols.NGN){
            return assetFolder + "beautiful-country-currency-nigeria.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.BRL){
            return assetFolder + "beautiful-country-currency-brazil.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.AUD){
            return assetFolder + "beautiful-country-currency-australia.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.GBP){
            return assetFolder + "beautiful-country-currency-england_pound.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.CAD){
            return assetFolder + "beautiful-country-currency-canada.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.CLP){
            return assetFolder + "beautiful-country-currency-chile.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.CNY){
            return assetFolder + "beautiful-country-currency-china_yuan.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.CZK){
            return assetFolder + "2000-czech-koruna-banknote-series-1996.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.EUR){
            return assetFolder + "beautiful-country-currency-euro.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.GHS){
            return assetFolder + "50GHS-front.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.HKD){
            return assetFolder + "beautiful-country-currency-hong_kong_dollar.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.INR){
            return assetFolder + "beautiful-country-currency-india.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.IDR){
            return assetFolder + "idr-100000-indonesian-rupiahs-2.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.ILS){
            return assetFolder + "beautiful-country-currency-israel.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.KRW){
            return assetFolder + "south-korea-50000-won.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.RUB){
            return assetFolder + "beautiful-country-currency-russia.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.MYR){
            return assetFolder + "beautiful-country-currency-malaysia.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.MXN){
            return assetFolder + "beautiful-country-currency-mexico.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.CHF){
            return assetFolder + "beautiful-country-currency-switzerland.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.ZAR){
            return assetFolder + "zar-10-south-african-rands-2.jpg";
        } else if (currencySymbol == WorldCurrencySymbols.USD){
            return assetFolder + "usd-5-us-dollars-2.jpg";
        }
        return null;
    }
}
