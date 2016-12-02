package de.hsmw.semestermanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

/**
 * Created by Adrian on 26.11.2016.
 */

/**
 --------------------- Query-Liste------------------------------------
add plans(old)      = insertDataPlans(String name, String timeframe)
add plans           = insertDataPlans(String ANZEIGENAME, String ZEITRAUM)
add modules         = insertDataModules(String ANZEIGENAME, int SEMESTERID)
add entries         = insertDataEntries(String ANZEIGENAME, String ZEITRAUM,String ORT, String TERMINTYP,int PRIORITAET, int SEMESTERID, int MODULID, int ISTGANZTAGSTERMIN, String DOZENT)

get all from plans(old)                       = getAllData()
get all from plans; ORDER BY ANZEIGENAME      = getAllDataPlans()
get all from modules; ORDER BY ANZEIGENAME    = getAllDataModules()
get all from entries; ORDER BY ANZEIGENAME    = getAllDataEntries()

get row by plans.id(old) = getDataByID(int id)
get row by plans.id      = getDataByIDPlans(int id)
get row by moduls.id     = getDataByIDModules(int id)
get row by Entries.id    = getDataByIDEntries(int id)

search after string; order by zeitraum    = getListByStringPlans(int id)
search after string; order by name        = getListByStringModules(int id)
search after string; order by zeitraum    = getListByStringEntries(int id)



 */

public class DatabaseInterface {
    private SQLiteDatabase db;

    DatabaseInterface(SQLiteDatabase db){
        this.db = db;
    }


    public long insertData(String name, String timeframe){
        ContentValues values = new ContentValues();
        values.put("Anzeigename", name);
        values.put("zeitraum", timeframe);
        return db.insert("plans", null, values);
    }
    //---------------------------Add Data----------------------------------------------
    public long insertDataPlans(String ANZEIGENAME, String ZEITRAUM){
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("ZEITRAUM", ZEITRAUM);
        return db.insert("plans", null, values);
    }
    public long insertDataModules(String ANZEIGENAME, int SEMESTERID){
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("SEMESTERID", SEMESTERID);
        return db.insert("modules", null, values);

    public long insertDataEntries(String ANZEIGENAME, String ZEITRAUM,String ORT, String TERMINTYP,int PRIORITAET, int SEMESTERID, int MODULID, int ISTGANZTAGSTERMIN, String DOZENT){
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

    public Cursor getAllData(){
        return db.rawQuery("select * from plans",null);
    }
    public Cursor getAllDataPlans(){
        return db.rawQuery("select * from plans ORDER BY ANZEIGENAME",null);
    }
    public Cursor getAllDataModules(){
        return db.rawQuery("select * from modules ORDER BY ANZEIGENAME",null);
    }
    public Cursor getAllDataEntries(){
        return db.rawQuery("select * from entries ORDER BY ANZEIGENAME",null);
    }

    //----------------------------Get Data By ID---------------------------
    public Cursor getDataByID(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false,"plans",columns,"ID = " + id,"","","","");
        Log.d("database", query);
        return db.rawQuery(query,null);
    }
    public Cursor getDataByIDPlans(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false,"plans",columns,"ID = " + id,"","","","");
        Log.d("database", query);
        return db.rawQuery(query,null);
    }
    public Cursor getDataByIDModules(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false,"modules",columns,"ID = " + id,"","","","");
        Log.d("database", query);
        return db.rawQuery(query,null);
    }
    public Cursor getDataByIDEntries(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false,"entries",columns,"ID = " + id,"","","","");
        Log.d("database", query);
        return db.rawQuery(query,null);
    }

    //-------------------------suche nach ---------------------------------------------

    public Cursor getListByStringPlans(int id){return db.rawQuery("SELECT * FROM plans ORDER BY ZEITRAUM",null);}
    public Cursor getListByStringModules(int id){return db.rawQuery("SELECT * FROM modules ORDER BY ANZEIGENAME",null);}
    public Cursor getListByStringEntries(int id){return db.rawQuery("SELECT * FROM entries ORDER BY ZEITRAUM",null);}




}