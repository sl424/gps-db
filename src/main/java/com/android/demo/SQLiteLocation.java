package com.android.demo;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class SQLiteLocation extends SQLiteOpenHelper {
    public SQLiteLocation(Context context) {
        super(context, DBContract.LocTable.DB_NAME, null, DBContract.LocTable.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.LocTable.SQL_CREATE_TABLE);
        ContentValues testValues = new ContentValues();
        testValues.put(DBContract.LocTable.COL_LON_STRING, "Longitude");
        testValues.put(DBContract.LocTable.COL_LAT_STRING, "Latitude");
        testValues.put(DBContract.LocTable.COL_INPUT_STRING, "input string");
        db.insert(DBContract.LocTable.TABLE_NAME,null,testValues);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBContract.LocTable.SQL_DROP_TABLE);
        onCreate(db);
    }
}

