package de.hsmw.semestermanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.sql.Date;

/**
 * Created by Adrian on 26.11.2016.
 */


public class DatabaseInterface {
    private SQLiteDatabase db;

    DatabaseInterface(SQLiteDatabase db) {
        this.db = db;
    }




    //---------------------------Add Data----------------------------------------------
    public long insertDataPlans(String ANZEIGENAME, String STARTTIME, String ENDTIME) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("STARTTIME", STARTTIME);
        values.put("ENDTIME", ENDTIME);
        return db.insert("plans", null, values);
    }

    public long insertDataModules(String ANZEIGENAME, int SEMESTERID) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("SEMESTERID", SEMESTERID);
        return db.insert("modules", null, values);
    }
  /*  public long insertDataRevision(String STARTDATE,String ENDDATA, int PRIORITAET) {
        ContentValues values = new ContentValues();
        values.put("STARTDATE", STARTDATE);
        values.put("ENDDATA", ENDDATA);
        values.put("PRIORITAET", PRIORITAET);
        return db.insert("modules", null, values);
    }*/
    public long insertDataRevisionException(int PRIORITAET,int ISDELETED,String STARTDATE,String ENDDATA,String STARTTIME,String ENDTIME) {
        ContentValues values = new ContentValues();
        values.put("PRIORITAET", PRIORITAET);
        values.put("ISDELETED", ISDELETED);
        values.put("STARTDATE", STARTDATE);
        values.put("ENDDATA", ENDDATA);
        values.put("STARTTIME", STARTTIME);
        values.put("ENDTIME", ENDTIME);
        return db.insert("modules", null, values);
    }
    public long insertDataEntries( String name, String startDate, String endDate, String wiederholungsStart, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", name);
        values.put("STARTDATE", startDate);
        values.put("ENDDATA", endDate);
        values.put("wiederholungsStart", wiederholungsStart);
        values.put("wiederholungsEnde", wiederholungsEnde);
        values.put("STARTTIME", startTime);
        values.put("ENDTIME", endTime);
        values.put("ORT", ort);
        values.put("TERMINTYP", typ);
        values.put("PRIORITAET", prioritaet);
        values.put("SEMESTERID", planID);
        values.put("MODULID", modulID);
        values.put("ISTGANZTAGSTERMIN", istGanztagsTermin);
        values.put("DOZENT", dozent);
        values.put("PERIODE", periode);
        return db.insert("entries", null, values);
    }
//------------------------------Get All Data-----------------------------------------
    public Plan[] getAllPlans(){
        Cursor c = db.rawQuery("select * from plans ORDER BY STARTTIME, ENDTIME", null);
        Plan[] returnPlans = new Plan[c.getCount()];
        while (c.moveToNext()) {
            returnPlans[c.getPosition()] = new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        }
        return returnPlans;
    }
    public Module[] getAllDataModules(){
        Cursor c = db.rawQuery("select * from modules ORDER BY ANZEIGENAME", null);
        Module[] returnModule = new Module[c.getCount()];
        while (c.moveToNext()) {
            returnModule[c.getPosition()] = new Module(c.getInt(0), c.getString(1), c.getInt(2));
        }
        return returnModule;
    }



    public Plan[] getAllRevisionException(){
        Cursor c = db.rawQuery("select * from revisionException ORDER BY STARTTIME, STARTTIME, ENDDATA, ENDTIME", null);
        Plan[] returnPlans = new Plan[c.getCount()];
        while (c.moveToNext()) {
            returnPlans[c.getPosition()] = new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        }
        return returnPlans;
    }
    public Entry[] getAllDataEntries(){
        Cursor c = db.rawQuery("select * from entries ORDER BY STARTTIME, STARTTIME, ENDDATA, ENDTIME ", null);
        Entry[] returnEntry = new Entry[c.getCount()];
        while (c.moveToNext()) {
            returnEntry[c.getPosition()] = new Entry(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        return returnEntry;
    }
//----------------------------Get Data By ID---------------------------
    public Plan getDataByIDPlans(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "plans", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Plan p = new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        c.close();
        return  p;
    }
    public Module getDataByIDModules(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Module p = new Module(c.getInt(0), c.getString(1), c.getInt(2));
        c.close();
        return  p;

    }
    public Entry getDataByIDEntries(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Entry p = new Entry(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        c.close();
        return  p;
    }

    public Module[] getModulesByPlanID(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "SEMESTERID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query,null);
        Module[] returnArray = new Module[c.getCount()];
        while (c.moveToNext()){
            returnArray[c.getPosition()] = new Module(c.getInt(0), c.getString(1), c.getInt(2));
        }
        c.close();
        return returnArray;
    }

    public Entry[] getEntriesByPlanID(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "entries", columns, "SEMESTERID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query,null);
        Entry[] returnArray = new Entry[c.getCount()];
        while (c.moveToNext()){
            returnArray[c.getPosition()] = new Entry(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        c.close();
        return returnArray;
    }


//-------------------------searching for---------------------------------------------
    public Plan[] getListByStringPlans(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM plans WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Plan[] returnPlan = new Plan[c.getCount()];
        while( c.moveToNext()){
            returnPlan[c.getPosition()] = new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        }
        c.close();
        return returnPlan;
    }

    public Module[] getListByStringModules(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Module[] returnModules = new Module[c.getCount()];
        while( c.moveToNext()){
            returnModules[c.getPosition()] = new Module(c.getInt(0), c.getString(1), c.getInt(2));
        }
        c.close();
        return returnModules;
    }
    public Entry[] getListByStringEntries(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM entries WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Entry[] returnEntries = new Entry[c.getCount()];
        while( c.moveToNext()){
            returnEntries[c.getPosition()] = new Entry(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        c.close();
        return returnEntries;
    }

//----------------------Update------------------------------------------------------
    public long updateDataPlans(int ID, String ANZEIGENAME, String STARTTIME, String ENDTIME) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("STARTTIME", STARTTIME);
        values.put("ENDTIME", ENDTIME);
        return db.update("plans", values, "WHERE ID =" + String.valueOf(ID), null);

    }

    public long updateDataModules(int ID, String ANZEIGENAME, int SEMESTERID) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("SEMESTERID", SEMESTERID);
        return db.update("Modules", values, "WHERE ID =" + String.valueOf(ID), null);
    }

    public long updateDataEntries(int id, String name, String startDate, String endDate, String wiederholungsStart, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", name);
        values.put("STARTDATE", startDate);
        values.put("ENDDATA", endDate);
        values.put("wiederholungsStart", wiederholungsStart);
        values.put("wiederholungsEnde", wiederholungsEnde);
        values.put("STARTTIME", startTime);
        values.put("ENDTIME", endTime);
        values.put("ORT", ort);
        values.put("TERMINTYP", typ);
        values.put("PRIORITAET", prioritaet);
        values.put("SEMESTERID", planID);
        values.put("MODULID", modulID);
        values.put("ISTGANZTAGSTERMIN", istGanztagsTermin);
        values.put("DOZENT", dozent);
        values.put("PERIODE", periode);
        return db.update("Entries", values, "WHERE ID =" + String.valueOf(id), null);
    }
    //------------------------------------------------------------------
   /* semid = list aller module
    modul id = liste aller entis
    semid = alle entis die nicht zu einem modul gehoren*/
}