package de.hsmw.semestermanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
    public long insertDataTermine( String name, String startDate, String wiederholungsStart, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode, int isExeption,int exceptionContextID,String exceptionTargetDay,int delete) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", name);
        values.put("STARTDATE", startDate);
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
        values.put("ISEXEPTION", isExeption);
        values.put("EXCEPTIONCONTEXTID", exceptionContextID);
        values.put("EXCEPTIONTARGETDAY", exceptionTargetDay);
        values.put("ISDELETED", delete);
        return db.insert("termine", null, values);
    }
//------------------------------Get All Data-----------------------------------------
    public Plan[] getAllPlans(){
        //Cursor c = db.rawQuery("select * from plans ORDER BY STARTTIME, ENDTIME", null);
        Cursor c = db.rawQuery("select * from plans", null);
        Plan[] returnPlans = new Plan[c.getCount()];
        while (c.moveToNext()) {
            returnPlans[c.getPosition()] = cursor2Plan(c);
        }
        return returnPlans;
    }
    public Module[] getAllDataModules(){
        //Cursor c = db.rawQuery("select * from modules ORDER BY ANZEIGENAME", null);
        Cursor c = db.rawQuery("select * from modules", null);
        Module[] returnModule = new Module[c.getCount()];
        while (c.moveToNext()) {
            returnModule[c.getPosition()] = cursor2Module(c);
        }
        return returnModule;
    }
    public Termin[] getAllDataTermine(){
        //Cursor c = db.rawQuery("select * from termine ORDER BY STARTTIME, STARTTIME, ENDDATA, ENDTIME ", null);
        Cursor c = db.rawQuery("select * from termine", null);
        Termin[] returnTermin = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnTermin[c.getPosition()] =  cursor2Termin(c);
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
        Plan p = cursor2Plan(c);
        c.close();
        return  p;
    }
    public Module getDataByIDModules(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Module p = cursor2Module(c);
        c.close();
        return  p;
    }
    public Termin getDataByIDTermine(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "Termine", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Termin p =   cursor2Termin(c);
        c.close();
        return  p;
    }

    public Termin[] getTermineByPlanID(int id){
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "SEMESTERID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query,null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()){
            returnArray[c.getPosition()] =  cursor2Termin(c);
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
            returnArray[c.getPosition()] = cursor2Module(c);
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
            returnArray[c.getPosition()] =  cursor2Termin(c);
            }
        c.close();
        return returnArray;
    }
    public Termin[] getTermineByPlanIDUndNichtModul(int id){
        String[] columns = {"*"};
        //String query = "select * from termine WHERE SEMESTERID =  \""+ id +  "\" AND ModulID = \"0\" ORDER BY STARTTIME, ENDTIME";
        String query = "select * from termine WHERE SEMESTERID =  \""+ id +  "\" AND ModulID = \"0\"";
        Cursor c = db.rawQuery(query,null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()){
            returnArray[c.getPosition()] =  cursor2Termin(c);
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
            returnPlan[c.getPosition()] = cursor2Plan(c);
        }
        c.close();
        return returnPlan;
    }
    public Module[] getListByStringModules(String searchword) {
        //Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Module[] returnModules = new Module[c.getCount()];
        while( c.moveToNext()){
            returnModules[c.getPosition()] = cursor2Module(c);
        }
        c.close();
        return returnModules;
    }
    public Termin[] getListByStringTermine(String searchword) {
        //Cursor c = db.rawQuery("SELECT * FROM termine WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Cursor c = db.rawQuery("SELECT * FROM termine WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Termin[] returnTermine = new Termin[c.getCount()];
        while( c.moveToNext()){
            returnTermine[c.getPosition()] =  cursor2Termin(c);
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
    public long updateDataTermine(int id, String name, String startDate, String wiederholungsStart, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode,int isExeption,int exceptionContextID,String exceptionTargetDay,int delete) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", name);
        values.put("STARTDATE", startDate);
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
        values.put("ISEXEPTION", isExeption);
        values.put("EXCEPTIONCONTEXTID", exceptionContextID);
        values.put("EXCEPTIONTARGETDAY", exceptionTargetDay);
        values.put("DELETE",delete);
        return db.update("Termine", values, "WHERE ID =" + String.valueOf(id), null);
    }
    //------------------------------------------------------------------
    public Termin[] getTermineByDate(String date){
        Log.d("database", "getTermineByDate aufgerufen!");
        ArrayList<Termin> returnArray = new ArrayList<>();
        ArrayList<Integer> TerminwiederholungsIgnorierArray = new ArrayList<>();
        ArrayList<Integer> EXCEPTIONCONTEXTIDArray = new ArrayList<>();

        //Query werfen, der mir roh alle Termine eines Tages zurück gibt. -> Diese in das Returnarray schreiben.
        String query = "select * from termine WHERE STARTDATE =  \""+ date + "\" ";

        Cursor c = db.rawQuery(query,null);
        while (c.moveToNext()){
            int terminId = c.getInt(0);
            int isException = c.getInt(15);
            int isDeleted = c.getInt(18);
            int periode = c.getInt(14);
            int exceptionContextID = c.getInt(16);
            String exceptionDay = c.getString(17);

            //Terminwiederholungen und Ausnahmen ignorieren.
            if(periode == 0 && isException == 0) {
                returnArray.add(cursor2Termin(c));
            }else if (isException != 0){
                //TerminwiederholungsID merken, wenn die ExceptionTargetDay dem date entspricht.
                if (exceptionDay.equals(date)) {
                    TerminwiederholungsIgnorierArray.add(exceptionContextID);
                }
                if(isDeleted == 0) {
                    returnArray.add(cursor2Termin(c));
                }
            }
        }
        c.close();

        //Query werfen, der mir alle Terminwiederholungen gibt.
        query = "select * from termine  WHERE PERIODE<>'0' AND ISEXEPTION='0'";
        c = db.rawQuery(query,null);
        while (c.moveToNext()){
            //Für Terminwiederholungen - dessen ID's nicht gemerkt wurden -  getTerminAtDate(date) aufrufen -> Termin anfügen, wenn nicht null.
            int terminId = c.getInt(0);
            //TESTEN :)
            if (TerminwiederholungsIgnorierArray.contains(terminId)){
                continue;
            }

            Termin t = cursor2Termin(c).getTerminAtDate(Date.valueOf(date));
            if(t != null){
                returnArray.add(t);
            }
        }

        //returnArray Sortieren
        Collections.sort(returnArray);

        return returnArray.toArray(new Termin[returnArray.size()]);
    }
    private Termin cursor2Termin(Cursor c){
        return new Termin(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getString(8), c.getInt(9), c.getInt(10), c.getInt(11), c.getInt(12), c.getString(13), c.getInt(14), c.getInt(15), c.getInt(16), c.getString(17), c.getInt(18));
    }
    private Plan cursor2Plan(Cursor c){
       return new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));

    }
    private Module cursor2Module(Cursor c){
        return new Module(c.getInt(0), c.getString(1), c.getInt(2));
    }
}