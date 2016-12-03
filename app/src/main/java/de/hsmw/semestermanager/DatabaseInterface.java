package de.hsmw.semestermanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.sql.Date;

/**
 * Created by Benni on 26.11.2016.
 */


public class DatabaseInterface {
    private SQLiteDatabase db;

    DatabaseInterface(SQLiteDatabase db) {
        this.db = db;
    }


    public long insertData(String name, String timeframe) {
        ContentValues values = new ContentValues();
        Date startDate = Date.valueOf(timeframe);
        Date endDate = Date.valueOf(timeframe);
        values.put("Anzeigename", name);
        values.put("STARTTIME", startDate.toString());
        values.put("ENDTIME", endDate.toString());
        return db.insert("plans", null, values);
    }

    //---------------------------Add Data----------------------------------------------
    public long insertDataPlans(String ANZEIGENAME, String ZEITRAUM) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("ZEITRAUM", ZEITRAUM);
        return db.insert("plans", null, values);
    }

    public long insertDataModules(String ANZEIGENAME, int SEMESTERID) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("SEMESTERID", SEMESTERID);
        return db.insert("modules", null, values);
    }

    public long insertDataEntries(String ANZEIGENAME, String ZEITRAUM, String ORT, String TERMINTYP, int PRIORITAET, int SEMESTERID, int MODULID, int ISTGANZTAGSTERMIN, String DOZENT) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("ZEITRAUM", ZEITRAUM);
        values.put("ORT", ORT);
        values.put("TERMINTYP", TERMINTYP);
        values.put("PRIORITAET", PRIORITAET);
        values.put("SEMESTERID", SEMESTERID);
        values.put("MODULID", MODULID);
        values.put("ISTGANZTAGSTERMIN", ISTGANZTAGSTERMIN);
        values.put("DOZENT", DOZENT);
        return db.insert("entries", null, values);
    }
    //------------------------------Get All Data-----------------------------------------

    public Cursor getAllData() {
        return db.rawQuery("select * from plans", null);
    }

    public Cursor getAllDataPlans() {
        return db.rawQuery("select * from plans ORDER BY ZEITRAUM", null);
    }

    public Cursor getAllDataModules() {
        return db.rawQuery("select * from modules ORDER BY ANZEIGENAME", null);
    }

    public Cursor getAllDataEntries() {
        return db.rawQuery("select * from entries ORDER BY ZEITRAUM", null);
    }

    //----------------------------Get Data By ID---------------------------
    public Cursor getDataByID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "plans", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        return db.rawQuery(query, null);
    }

    public Plan getDataByIDPlans(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "plans", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        return new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
    }

    public Cursor getDataByIDModules(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        return db.rawQuery(query, null);
    }

    public Cursor getDataByIDEntries(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "entries", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        return db.rawQuery(query, null);

    }

    //-------------------------searching for---------------------------------------------

    public Plan[] getListByStringPlans(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM plans WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Plan[] returnPlan = new Plan[c.getCount()];
        while( c.moveToNext()){
            returnPlan[c.getPosition()] = new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        }
        return returnPlan;
    }

    public Cursor getListByStringModules(String searchword) {
        return db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ANZEIGENAME", null);
    }

    public Cursor getListByStringEntries(String searchword) {
        return db.rawQuery("SELECT * FROM entries WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
    }
    //----------------------Update------------------------------------------------------

    //UPDATE Customers SET CustomerName='Hamburg', ContactName='Hamburg',Address='Hamburg' WHERE CustomerID=1;

    public long updateDataPlans(int ID, String ANZEIGENAME, String ZEITRAUM) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("ZEITRAUM", ZEITRAUM);
        return db.update("plans", values, "WHERE ID =" + String.valueOf(ID), null);

    }

    public long updateDataModules(int ID, String ANZEIGENAME, int SEMESTERID) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("SEMESTERID", SEMESTERID);
        return db.update("Modules", values, "WHERE ID =" + String.valueOf(ID), null);
    }

    public long updateDataEntries(int ID, String ANZEIGENAME, String ZEITRAUM, String ORT, String TERMINTYP, int PRIORITAET, int SEMESTERID, int MODULID, int ISTGANZTAGSTERMIN, String DOZENT) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("ZEITRAUM", ZEITRAUM);
        values.put("ORT", ORT);
        values.put("TERMINTYP", TERMINTYP);
        values.put("PRIORITAET", PRIORITAET);
        values.put("SEMESTERID", SEMESTERID);
        values.put("MODULID", MODULID);
        values.put("ISTGANZTAGSTERMIN", ISTGANZTAGSTERMIN);
        values.put("DOZENT", DOZENT);
        return db.update("Entries", values, "WHERE ID =" + String.valueOf(ID), null);
    }
}