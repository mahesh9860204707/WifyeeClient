package com.wifyee.greenfields.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBManager extends SQLiteOpenHelper {


    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory,
                     int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    // Database Information
    static final String DB_NAME = "DatabaseDB.DB";

    // database version
    static final int DB_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            //.db.execSQL("DELETE FROM " + TABLE_NAME1);
            //db.execSQL(CREATE_TABLE1);
            Log.d("Problem !!!!!","Comes here  ");
        }
        catch(Exception ex)
        {
            Log.d("Problem !!!!!","Exception for creation "+ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        onCreate(db);
    }

}
