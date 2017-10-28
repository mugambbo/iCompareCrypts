package com.hellotractor.icomparecrypts.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.hellotractor.icomparecrypts.model.ExchangeRateItem;
import java.util.ArrayList;

/**
 * Created by Abdulmajid on 7/30/17.
 */

public class DBOPS {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBOPS(Context ctx) {
        this.dbHelper = new DBHelper(ctx);
    }

    public void insertExchangeRate(ArrayList<ExchangeRateItem> exchangeRateItems){
        db = dbHelper.getWritableDatabase();
        // and insert it into the database.
        ContentValues exchangeRateValues = new ContentValues();
        // Assign values for each row.
        try {
            for (ExchangeRateItem exchangeRateItem : exchangeRateItems) {
                exchangeRateValues.put(DBHelper.FROM_CURRENCY_SYMBOL, exchangeRateItem.getFromCurrencySymbol());
                exchangeRateValues.put(DBHelper.TO_CURRENCY_SYMBOL, exchangeRateItem.getToCurrencySymbol());
                exchangeRateValues.put(DBHelper.FROM_CURRENCY_FULLNAME, exchangeRateItem.getFromCurrencyFullname());
                exchangeRateValues.put(DBHelper.TO_CURRENCY_FULLNAME, exchangeRateItem.getToCurrencyFullname());
                exchangeRateValues.put(DBHelper.FROM_IMAGE_URL, exchangeRateItem.getFromImageURL());
                exchangeRateValues.put(DBHelper.TO_IMAGE_URL, exchangeRateItem.getToImageURL());
                exchangeRateValues.put(DBHelper.FROM_AMOUNT, exchangeRateItem.getFromAmount());
                exchangeRateValues.put(DBHelper.TO_AMOUNT, exchangeRateItem.getToAmount());
                exchangeRateValues.put(DBHelper.UPDATED, exchangeRateItem.getUpdated());
                // Insert the row into your table
                db.insert(DBHelper.EXCHANGE_RATES_TABLE, null, exchangeRateValues);
            }
        } catch (Exception err){
            Log.e("DB","Unable to insert news");
        }
    }

    public ArrayList<ExchangeRateItem> getExchangeRateItems (){
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.EXCHANGE_RATES_TABLE, null);
        ArrayList <ExchangeRateItem> exchangeRateItems = new ArrayList<>();
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    ExchangeRateItem exchangeRateItem = new ExchangeRateItem();
                    exchangeRateItem.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.ID)));
                    exchangeRateItem.setFromCurrencySymbol(cursor.getString(cursor.getColumnIndex(DBHelper.FROM_CURRENCY_SYMBOL)));
                    exchangeRateItem.setToCurrencySymbol(cursor.getString(cursor.getColumnIndex(DBHelper.TO_CURRENCY_SYMBOL)));
                    exchangeRateItem.setFromCurrencyFullname(cursor.getString(cursor.getColumnIndex(DBHelper.FROM_CURRENCY_FULLNAME)));
                    exchangeRateItem.setToCurrencyFullname(cursor.getString(cursor.getColumnIndex(DBHelper.TO_CURRENCY_FULLNAME)));
                    exchangeRateItem.setFromImageURL(cursor.getString(cursor.getColumnIndex(DBHelper.FROM_IMAGE_URL)));
                    exchangeRateItem.setToImageURL(cursor.getString(cursor.getColumnIndex(DBHelper.TO_IMAGE_URL)));
                    exchangeRateItem.setFromAmount(cursor.getDouble(cursor.getColumnIndex(DBHelper.FROM_AMOUNT)));
                    exchangeRateItem.setToAmount(cursor.getDouble(cursor.getColumnIndex(DBHelper.TO_AMOUNT)));
                    exchangeRateItem.setUpdated(cursor.getString(cursor.getColumnIndex(DBHelper.UPDATED)));
                    exchangeRateItems.add(exchangeRateItem);
                    cursor.moveToNext();
                }

                cursor.close();
            }
        } catch (Exception err){Log.e("", "Unable to get Exchange Rates");}
        return exchangeRateItems;
    }

    public void clearExchangeRates() {
        db = dbHelper.getWritableDatabase();
        try {
            db.delete(DBHelper.EXCHANGE_RATES_TABLE, null, null);
        } catch (Exception err){err.printStackTrace();}
    }

}
