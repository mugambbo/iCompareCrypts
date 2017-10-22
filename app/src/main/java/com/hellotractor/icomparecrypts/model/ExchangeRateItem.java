package com.hellotractor.icomparecrypts.model;

/**
 * Created by abdulmajid on 10/21/17.
 */

public class ExchangeRateItem {
    int id;
    String fromCurrencySymbol;
    String toCurrencySymbol;
    String fromCurrencyFullname;
    String toCurrencyFullname;
    String fromImageURL;
    String toImageURL;
    double fromAmount;
    double toAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromCurrencySymbol() {
        return fromCurrencySymbol;
    }

    public void setFromCurrencySymbol(String fromCurrencySymbol) {
        this.fromCurrencySymbol = fromCurrencySymbol;
    }

    public String getToCurrencySymbol() {
        return toCurrencySymbol;
    }

    public void setToCurrencySymbol(String toCurrencySymbol) {
        this.toCurrencySymbol = toCurrencySymbol;
    }

    public String getFromCurrencyFullname() {
        return fromCurrencyFullname;
    }

    public void setFromCurrencyFullname(String fromCurrencyFullname) {
        this.fromCurrencyFullname = fromCurrencyFullname;
    }

    public String getToCurrencyFullname() {
        return toCurrencyFullname;
    }

    public void setToCurrencyFullname(String toCurrencyFullname) {
        this.toCurrencyFullname = toCurrencyFullname;
    }

    public String getFromImageURL() {
        return fromImageURL;
    }

    public void setFromImageURL(String fromImageURL) {
        this.fromImageURL = fromImageURL;
    }

    public String getToImageURL() {
        return toImageURL;
    }

    public void setToImageURL(String toImageURL) {
        this.toImageURL = toImageURL;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }
}
