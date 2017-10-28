package com.hellotractor.icomparecrypts.enums;

/**
 * Created by Abdulmajid on 10/22/17.
 */

public enum CryptoCurrencySymbols {

    //Cryptocurrencies
    BTC("Bitcoin (BTC)"),
    ETH("Ethereum  (ETH)");

    private String fullName;

    CryptoCurrencySymbols(String fullName) {
        this.fullName = fullName;
    }

    public String fullName() {
        return fullName;
    }
}
