package org.kamusbahasarejang.kamusbahasarejang;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseOpenHelperInggris extends SQLiteAssetHelper {
     private static final String DATABASE_NAME = "databaseinggris.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelperInggris(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
