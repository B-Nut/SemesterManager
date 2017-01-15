package de.hsmw.semestermanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Adrian on 26.11.2016.
 */


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "semesterPlans.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_MODULES = "modules";
    private static final String TABLE_PLANS = "plans";
    private static final String TABLE_TERMINE = "termine";

    /**
     * Konstruktor
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("database", "create Database");
        db.execSQL("CREATE TABLE " + TABLE_MODULES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ANZEIGENAME TEXT,SEMESTERID INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_PLANS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ANZEIGENAME TEXT,STARTTIME TEXT, ENDTIME TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_TERMINE + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ANZEIGENAME TEXT," +
                "STARTDATE TEXT," +
                "wiederholungsEnde TEXT," +
                "STARTTIME TEXT," +
                "ENDTIME TEXT," +
                "ORT TEXT," +
                "TERMINTYP TEXT," +
                "PRIORITAET INTEGER," +
                "SEMESTERID INTEGER," +
                "MODULID INTEGER," +
                "ISTGANZTAGSTERMIN INTEGER," +
                "DOZENT TEXT," +
                "PERIODE INTEGER," +
                "ISEXEPTION INTEGER," +
                "EXCEPTIONCONTEXTID INTEGER," +
                "EXCEPTIONTARGETDAY TEXT," +
                "ISDELETED INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DREOP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
