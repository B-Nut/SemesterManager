package de.hsmw.semestermanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

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
    public long insertDataTermine( String name, String startDate, String endDate, String wiederholungsStart, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode) {
        ContentValues values = new ContentValues();
        values.put("ANZEGENAME", name);
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
        return db.insert("termine", null, values);
    }
//------------------------------Get All Data-----------------------------------------
    public Plan[] getAllPlans(){
        //Cursor c = db.rawQuery("select * from plans ORDER BY STARTTIME, ENDTIME", null);
        Cursor c = db.rawQuery("select * from plans", null);
        Plan[] returnPlans = new Plan[c.getCount()];
        while (c.moveToNext()) {
            returnPlans[c.getPosition()] = new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        }
        return returnPlans;
    }
    public Module[] getAllDataModules(){
        //Cursor c = db.rawQuery("select * from modules ORDER BY ANZEIGENAME", null);
        Cursor c = db.rawQuery("select * from modules", null);
        Module[] returnModule = new Module[c.getCount()];
        while (c.moveToNext()) {
            returnModule[c.getPosition()] = new Module(c.getInt(0), c.getString(1), c.getInt(2));
        }
        return returnModule;
    }
    public Termin[] getAllDataTermine(){
        //Cursor c = db.rawQuery("select * from termine ORDER BY STARTTIME, STARTTIME, ENDDATA, ENDTIME ", null);
        Cursor c = db.rawQuery("select * from termine", null);
        Termin[] returnTermin = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnTermin[c.getPosition()] = new Termin(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        return returnTermin;
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
    public Termin getDataByIDTermine(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "Termine", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Termin p = new Termin(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        c.close();
        return  p;
    }

    public Termin[] getTermineByPlanID(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "SEMESTERID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query,null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()){
            returnArray[c.getPosition()] = new Termin(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        c.close();
        return returnArray;
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

    public Termin[] getTermineByModulID(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "ModulID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query,null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()){
            returnArray[c.getPosition()] = new Termin(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        c.close();
        return returnArray;
    }
    public Termin[] getTermineByPlanIDUndNichtModul(int id){
        String[] columns = {"*"};
        //String query = "select * from termine WHERE SEMESTERID =  \""+ id +  "\" AND ModulID = \"0\" ORDER BY STARTTIME, ENDTIME";
        String query = "select * from termine WHERE SEMESTERID =  \""+ id +  "\" AND ModulID = \"0\"";
        //String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "SEMESTERID = " + id AND "ModulID = " + 0, "", "", "", "");
        Cursor c = db.rawQuery(query,null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()){
            returnArray[c.getPosition()] = new Termin(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        c.close();
        return returnArray;
    }
//-------------------------searching for---------------------------------------------
    public Plan[] getListByStringPlans(String searchword) {
        //Cursor c = db.rawQuery("SELECT * FROM plans WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Cursor c = db.rawQuery("SELECT * FROM plans WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Plan[] returnPlan = new Plan[c.getCount()];
        while( c.moveToNext()){
            returnPlan[c.getPosition()] = new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
        }
        c.close();
        return returnPlan;
    }
    public Module[] getListByStringModules(String searchword) {
        //Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Module[] returnModules = new Module[c.getCount()];
        while( c.moveToNext()){
            returnModules[c.getPosition()] = new Module(c.getInt(0), c.getString(1), c.getInt(2));
        }
        c.close();
        return returnModules;
    }
    public Termin[] getListByStringTermine(String searchword) {
        //Cursor c = db.rawQuery("SELECT * FROM termine WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Cursor c = db.rawQuery("SELECT * FROM termine WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Termin[] returnTermine = new Termin[c.getCount()];
        while( c.moveToNext()){
            returnTermine[c.getPosition()] = new Termin(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8),c.getString(9),c.getInt(10),c.getInt(11),c.getInt(12),c.getInt(13),c.getString(14),c.getInt(15));
        }
        c.close();
        return returnTermine;
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
    public long updateDataTermine(int id, String name, String startDate, String endDate, String wiederholungsStart, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode) {
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
        return db.update("Termine", values, "WHERE ID =" + String.valueOf(id), null);
    }
    //------------------------------------------------------------------
}