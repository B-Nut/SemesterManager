package de.hsmw.semestermanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Created by Benjamin on 26.11.2016.
 */

public class DatabaseInterface {
    private SQLiteDatabase db;

    DatabaseInterface(SQLiteDatabase db){
        this.db = db;
    }

    public long writeSomething(){
        String[] colums = {"id"};
        String query = SQLiteQueryBuilder.buildQueryString(false,"plans",colums,"","","","","");
        Log.d("database", query);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Anzeigename", "Wintersemester16");
        contentValues.put("zeitraum", "IRGENDEINZEITRAUM");
        return db.insert("plans",null,contentValues);

    }
    public long insertData(String name, String timeframe){
        ContentValues values = new ContentValues();
        values.put("Anzeigename", name);
        values.put("zeitraum", timeframe);
        return db.insert("plans", null, values);
    }

    public Cursor getAllData(){
        return db.rawQuery("select * from plans",null);
    }
    public Cursor getDataByID(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false,"plans",columns,"ID = " + id,"","","","");
        Log.d("database", query);
        return db.rawQuery(query,null);
    }
}