package com.hellotractor.icomparecrypts.enums;

/**
 * Created by Abdulmajid on 10/28/17.
 */

public enum  WorldCurrencySymbols {

    //World Currencies
    NGN("Nigerian naira"),
    GHS("Ghanaian cedi"),
    CZK("Czech koruna"),
    AUD("Australian dollar"),
    BRL("Brazilian real"),
    CAD("Canadian dollar"),
    CLP("Chilean peso"),
    CNY("Chinese Yuan Renminbi"),
    GBP("Pound sterling"),
    EUR("European euro"),
    HKD("Hong Kong dollar"),
    INR("Indian rupee"),
    ILS("Israeli new shekel"),
    MYR("Malaysian ringgit"),
    MXN("Mexican peso"),
    RUB("Russian ruble"),
    CHF("Swiss franc"),
    IDR("Indonesian rupiah"),
    KRW("South Korean won"),
    ZAR("South African rand"),
    USD("United States dollar");

    private String fullName;

    WorldCurrencySymbols (String fullName) {
        this.fullName = fullName;
    }

    public String fullName() {
        return fullName;
    }
}
