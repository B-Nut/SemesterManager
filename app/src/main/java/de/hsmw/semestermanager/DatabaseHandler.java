package de.hsmw.semestermanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Benjamin on 26.11.2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "semesterPlans.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_MODULES = "modules";
    private static final String TABLE_PLANS = "plans";
    private static final String TABLE_ENTRIES = "entries";

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("database", "create Database");
        db.execSQL("CREATE TABLE " + TABLE_MODULES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ANZEIGENAME TEXT,SEMESTERID INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_PLANS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ANZEIGENAME TEXT,STARTTIME DATETIME, ENDTIME DATETIME)");

        db.execSQL("CREATE TABLE " + TABLE_ENTRIES + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ANZEGENAME TEXT," +
                "STARTTIME DATETIME," +
                "ENDTIME DATETIME," +
                "ORT TEXT," +
                "TERMINTYP TEXT," +
                "PRIORITAET INTEGER," +
                "SEMESTERID INTEGER," +
                "MODULID INTEGER," +
                "ISTGANZTAGSTERMIN INTEGER," +
                "DOZENT TEXT" +
                ")");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DREOP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
