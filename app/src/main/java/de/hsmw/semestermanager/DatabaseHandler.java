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
    private static final int DATABASE_VERSION = 7;
    private static final String TABLE_MODULES = "modules";
    private static final String TABLE_PLANS = "plans";
   // private static final String TABLE_REVISION = "revision";
    private static final String TABLE_REVISIONEXCEPTION = "revisionException";
    private static final String TABLE_TERMINE = "termine";

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("database", "create Database");
        db.execSQL("CREATE TABLE " + TABLE_MODULES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ANZEIGENAME TEXT,SEMESTERID INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_PLANS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,ANZEIGENAME TEXT,STARTTIME TEXT, ENDTIME TEXT)");
     //   db.execSQL("CREATE TABLE " + TABLE_REVISION + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,STARTDATE TEXT, ENDDATA TEXT,PRIORITAET INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_REVISIONEXCEPTION + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TerminID INTEGER, PRIORITAET INTEGER, ISDELETED INTEGER, STARTDATE TEXT, ENDDATA TEXT,STARTTIME TEXT, ENDTIME TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_TERMINE + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ANZEGENAME TEXT," +
                "STARTDATE TEXT," +
                "ENDDATA TEXT,"+
                "wiederholungsStart TEXT,"+
                "wiederholungsEnde TEXT,"+
                "STARTTIME TEXT,"+
                "ENDTIME TEXT,"+
                "ORT TEXT,"+
                "TERMINTYP TEXT," +
                "PRIORITAET INTEGER," +
                "SEMESTERID INTEGER," +
                "MODULID INTEGER," +
                "ISTGANZTAGSTERMIN INTEGER," +
                "DOZENT TEXT," +
                "PERIODE INTEGER"+
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
