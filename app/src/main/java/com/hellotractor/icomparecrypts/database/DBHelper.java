package com.hellotractor.icomparecrypts.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abdulmajid on 7/30/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "iCompareCrypts.db";
    private static final int DATABASE_VERSION = 1;

    //TABLE Names
    public static final String EXCHANGE_RATES_TABLE = "EXCHANGE_RATES";

    //NEWS TABLE FIELDS
    public static final String ID = "_id" ;
    public static final String FROM_CURRENCY_SYMBOL = "FromCurrencySymb" ;
    public static final String TO_CURRENCY_SYMBOL = "ToCurrencySymb" ;
    public static final String FROM_CURRENCY_FULLNAME = "FromCurrencyFullname" ;
    public static final String TO_CURRENCY_FULLNAME = "ToCurrencyFullname" ;
    public static final String FROM_IMAGE_URL = "FromImageURL" ;
    public static final String TO_IMAGE_URL = "ToImageURL" ;
    public static final String FROM_AMOUNT = "FromAmount" ;
    public static final String TO_AMOUNT = "ToAmount" ;
    public static final String UPDATED = "Updated" ;


    // Database creation sql statement
    private static final String EXCHANGE_RATE_TABLE_CREATE = "create table "
            + EXCHANGE_RATES_TABLE + "("
            + ID +" integer primary key autoincrement,"
            + FROM_CURRENCY_SYMBOL + " varchar, "
            + TO_CURRENCY_SYMBOL + " varchar,"
            + FROM_CURRENCY_FULLNAME + " varchar,"
            + TO_CURRENCY_FULLNAME + " varchar,"
            + FROM_IMAGE_URL + " varchar,"
            + TO_IMAGE_URL + " varchar,"
            + FROM_AMOUNT + " varchar,"
            + TO_AMOUNT + " varchar,"
            + UPDATED + " varchar" + ")";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EXCHANGE_RATE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + EXCHANGE_RATES_TABLE);
            onCreate(db);
        } catch (SQLException err){
            err.printStackTrace();
        }
    }

    public void clearAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXCHANGE_RATES_TABLE, null, null);
        db.close();
    }

}
