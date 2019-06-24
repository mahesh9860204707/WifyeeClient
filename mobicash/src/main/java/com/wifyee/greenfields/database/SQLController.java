package com.wifyee.greenfields.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class SQLController {
    private DBManager dbHelper;
    private Context ourcontext;
    private SQLiteDatabase database;

    public SQLController(Context c) {
        ourcontext = c;
    }

    public SQLController open() throws SQLException {
        dbHelper = new DBManager(ourcontext,"DatabaseDB",null,2);
        database = dbHelper.getWritableDatabase();
        database = dbHelper.getReadableDatabase();
        return this;

    }

    public String fireQuery(String query)
    {
        String return_value="Done";
        try
        {
            database.execSQL(query);
        }
        catch(Exception ex)
        {
            return_value=ex.getMessage();
        }
        return return_value;
    }

    public String fireSafeQuery(String query, String[] values)
    {
        String return_value="";

        try
        {
            //SQLiteStatement stmt = db.compileStatement("SELECT * FROM Country WHERE code = ?");
            //stmt=database.prepareStatement(query);
            SQLiteStatement stmt = database.compileStatement(query);

            int i=1;
            for(String data:values)
            {
                stmt.bindString(i, data);
                i++;
            }
            stmt.execute();
            return_value="Done";

        }
        catch(Exception ex)
        {
            return_value=ex.getMessage();
        }
        return return_value;
    }


    public Cursor safeRetrieve(String query, String[] values)
    {
        return database.rawQuery(query,values);
    }

    public Cursor retrieve(String query)
    {
        return database.rawQuery(query,null);
    }

    public void close() {
        database.close();
        dbHelper.close();
    }
}
