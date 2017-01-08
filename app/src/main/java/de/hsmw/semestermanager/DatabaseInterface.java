package de.hsmw.semestermanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Adrian on 26.11.2016.
 */


public class DatabaseInterface {
    private static DatabaseInterface di;
    private SQLiteDatabase db;

    private DatabaseInterface(SQLiteDatabase db) {
        this.db = db;
    }

    public static DatabaseInterface getInstance(Context c) {
        if (di == null) {
            di = new DatabaseInterface(new DatabaseHandler(c.getApplicationContext()).getWritableDatabase());
        }
        return di;
    }

    //---------------------------Add Data----------------------------------------------
    public long insertDataPlans(String ANZEIGENAME, String STARTTIME, String ENDTIME) {
        if ((Date.valueOf(STARTTIME).before(Date.valueOf(ENDTIME)) || (Date.valueOf(STARTTIME).equals(Date.valueOf(ENDTIME))))) {
            ContentValues values = new ContentValues();
            values.put("ANZEIGENAME", ANZEIGENAME);
            values.put("STARTTIME", STARTTIME);
            values.put("ENDTIME", ENDTIME);
            return db.insert("plans", null, values);
        } else {
            Log.d("DatabaseInterface", "insertDataPlans = return -1");
            return -1;
        }
    }

    public long insertDataModules(String ANZEIGENAME, int SEMESTERID) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("SEMESTERID", SEMESTERID);
        return db.insert("modules", null, values);
    }

    public long insertDataTermine(String name, String startDate, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode, int isExeption, int exceptionContextID, String exceptionTargetDay, int delete) {
        if (Date.valueOf(startDate).equals(Date.valueOf(wiederholungsEnde)) || Date.valueOf(startDate).before(Date.valueOf(wiederholungsEnde))) {
            if (Time.valueOf(startTime).equals(Time.valueOf(endTime)) || Time.valueOf(startTime).before(Time.valueOf(endTime))) {
                ContentValues values = new ContentValues();
                values.put("ANZEIGENAME", name);
                values.put("STARTDATE", startDate);
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
            } else {
                Log.d("DatabaseInterface", "insertDataTermine = return -1");
                return -1;
            }
        } else {
            Log.d("DatabaseInterface", "insertDataTermine = return -1");
            return -1;
        }
    }

    //------------------------------Get All Data-----------------------------------------
    public Plan[] getAllPlans() {
        //Cursor c = db.rawQuery("select * from plans ORDER BY STARTTIME, ENDTIME", null);
        Cursor c = db.rawQuery("select * from plans", null);
        Plan[] returnPlans = new Plan[c.getCount()];
        while (c.moveToNext()) {
            returnPlans[c.getPosition()] = cursor2Plan(c);
        }
        return returnPlans;
    }

    public Module[] getAllDataModules() {
        //Cursor c = db.rawQuery("select * from modules ORDER BY ANZEIGENAME", null);
        Cursor c = db.rawQuery("select * from modules", null);
        Module[] returnModule = new Module[c.getCount()];
        while (c.moveToNext()) {
            returnModule[c.getPosition()] = cursor2Module(c);
        }
        return returnModule;
    }

    public Termin[] getAllDataTermine() {
        Cursor c = db.rawQuery("select * from termine ORDER BY STARTTIME, STARTTIME, ENDDATA, ENDTIME ", null);
        //Cursor c = db.rawQuery("select * from termine", null);
        Termin[] returnTermin = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnTermin[c.getPosition()] = cursor2Termin(c);
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
        return p;
    }

    public Module getDataByIDModules(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Module p = cursor2Module(c);
        c.close();
        return p;
    }

    public Termin getDataByIDTermine(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "Termine", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        c.moveToNext();
        Termin p = cursor2Termin(c);
        c.close();
        return p;
    }

    public Termin[] getTermineByPlanID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "SEMESTERID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query, null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnArray[c.getPosition()] = cursor2Termin(c);
        }
        c.close();
        return returnArray;
    }

    public Module[] getModulesByPlanID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "SEMESTERID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query, null);
        Module[] returnArray = new Module[c.getCount()];
        while (c.moveToNext()) {
            returnArray[c.getPosition()] = cursor2Module(c);
        }
        c.close();
        return returnArray;
    }

    public Termin[] getTermineByModulID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "ModulID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query, null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnArray[c.getPosition()] = cursor2Termin(c);
        }
        c.close();
        return returnArray;
    }

    public Termin[] getTermineByPlanIDUndNichtModul(int id) {
        String[] columns = {"*"};
        String query = "select * from termine WHERE SEMESTERID =  \"" + id + "\" AND ModulID = \"0\" ORDER BY STARTTIME, ENDTIME";
        //String query = "select * from termine WHERE SEMESTERID =  \""+ id +  "\" AND ModulID = \"0\"";
        Cursor c = db.rawQuery(query, null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnArray[c.getPosition()] = cursor2Termin(c);
        }
        c.close();
        return returnArray;
    }

    //-------------------------searching for---------------------------------------------
    public Plan[] getListByStringPlans(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM plans WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        //Cursor c = db.rawQuery("SELECT * FROM plans WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Plan[] returnPlan = new Plan[c.getCount()];
        while (c.moveToNext()) {
            returnPlan[c.getPosition()] = cursor2Plan(c);
        }
        c.close();
        return returnPlan;
    }

    public Module[] getListByStringModules(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        //Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Module[] returnModules = new Module[c.getCount()];
        while (c.moveToNext()) {
            returnModules[c.getPosition()] = cursor2Module(c);
        }
        c.close();
        return returnModules;
    }

    public Termin[] getListByStringTermine(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM termine WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        //Cursor c = db.rawQuery("SELECT * FROM termine WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"", null);
        Termin[] returnTermine = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnTermine[c.getPosition()] = cursor2Termin(c);
        }
        c.close();
        return returnTermine;
    }

    //----------------------Update------------------------------------------------------
    public long updateDataPlans(int ID, String ANZEIGENAME, String STARTTIME, String ENDTIME) {
        if ((Date.valueOf(STARTTIME).before(Date.valueOf(ENDTIME)) || (Date.valueOf(STARTTIME).equals(Date.valueOf(ENDTIME))))) {
            ContentValues values = new ContentValues();
            values.put("ANZEIGENAME", ANZEIGENAME);
            values.put("STARTTIME", STARTTIME);
            values.put("ENDTIME", ENDTIME);
            return db.update("plans", values, "ID =" + String.valueOf(ID), null);
        } else {
            Log.d("DatabaseInterface", "updateDataPlans = return -1");
            return -1;
        }
    }

    public long updateDataModules(int ID, String ANZEIGENAME, int SEMESTERID) {
        ContentValues values = new ContentValues();
        values.put("ANZEIGENAME", ANZEIGENAME);
        values.put("SEMESTERID", SEMESTERID);
        return db.update("Modules", values, "ID =" + String.valueOf(ID), null);
    }

    public long updateDataTermine(int id, String name, String startDate, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode, int isExeption, int exceptionContextID, String exceptionTargetDay, int delete) {
        if (Date.valueOf(startDate).equals(Date.valueOf(wiederholungsEnde)) || Date.valueOf(startDate).before(Date.valueOf(wiederholungsEnde))) {
            if (Time.valueOf(startTime).equals(Time.valueOf(endTime)) || Time.valueOf(startTime).before(Time.valueOf(endTime))) {
                ContentValues values = new ContentValues();
                values.put("ANZEIGENAME", name);
                values.put("STARTDATE", startDate);
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

                return db.update("Termine", values, "ID =" + String.valueOf(id), null);
            } else {
                Log.d("DatabaseInterface", "updateDataTermine = return -1");
                return -1;
            }
        } else {
            Log.d("DatabaseInterface", "updateDataTermine = return -1");
            return -1;
        }
    }

    //------------------------------------------------------------------
    public Termin[] getTermineByDate(String date, int planOrModuleID, boolean isPlan) {
        ArrayList<Termin> returnArray = new ArrayList<>();
        ArrayList<Integer> TerminwiederholungsIgnorierArray = new ArrayList<>();

        String restrictionExtension;
        if (isPlan) {
            restrictionExtension = "SEMESTERID = \"" + planOrModuleID + "\"";
        } else {
            restrictionExtension = "MODULID = \"" + planOrModuleID + "\"";
        }

        //Query werfen, der mir roh alle Termine eines Tages zurück gibt. -> Diese in das Returnarray schreiben.
        String query = "select * from termine WHERE STARTDATE =  \"" + date + "\" AND " + restrictionExtension;

        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            int isException = c.getInt(14);
            int isDeleted = c.getInt(17);
            int periode = c.getInt(13);

            //Terminwiederholungen und Ausnahmen ignorieren.
            if (periode == 0 && isException == 0) {
                returnArray.add(cursor2Termin(c));
            } else if (isException != 0) {
                if (isDeleted == 0) {
                    returnArray.add(cursor2Termin(c));
                }
            }
        }
        c.close();

        //Alle Terminwiederholungsausnahme holen, die für der aktuellen Tag gelten
        query = "select * from termine WHERE EXCEPTIONTARGETDAY =  \"" + date + "\" AND " + restrictionExtension;
        c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            int exceptionContextID = c.getInt(15);
            TerminwiederholungsIgnorierArray.add(exceptionContextID);
        }
        c.close();

        //Query werfen, der mir alle Terminwiederholungen gibt.
        query = "select * from termine  WHERE PERIODE<>'0' AND ISEXEPTION='0' AND " + restrictionExtension;
        c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            //Für Terminwiederholungen - dessen ID's nicht gemerkt wurden -  getTerminAtDate(date) aufrufen -> Termin anfügen, wenn nicht null.
            int terminId = c.getInt(0);
            //TODO: TESTEN :)
            if (TerminwiederholungsIgnorierArray.contains(terminId)) {
                continue;
            }

            Termin t = cursor2Termin(c).getTerminAtDate(Date.valueOf(date));
            if (t != null) {
                returnArray.add(t);
            }
        }

        //returnArray Sortieren
        Collections.sort(returnArray);

        return returnArray.toArray(new Termin[returnArray.size()]);
    }

    //_________________Delete_____________________
    public void deletePlanByID(int ID) {
        try {
            db.delete("PLANS", "ID =  \"" + ID + "\" ", null);
            Log.d("DatabaseInterface", "Plan_geloescht: " + ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteMudulByID(int ID) {
        try {
            db.delete("MODULES", "ID =  \"" + ID + "\" ", null);
            Log.d("DatabaseInterface", "Modul_geloescht: " + ID);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void deleteTerminByID(int ID) {
        try {
            db.delete("TERMINE", "ID =  \"" + ID + "\" ", null);
            Log.d("DatabaseInterface", "Termin_geloescht: " + ID);
            int deletedItems = db.delete("TERMINE", "EXCEPTIONCONTEXTID =  \"" + ID + "\" ", null);
            Log.d("DatabaseInterface", deletedItems + " Wiederholungsausnahmen im Kontext der ID " + ID + " gelöscht.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //___________________SonderFunktionen_________________
    public int getCountExceptionsByID(int ID) {
        Cursor c = db.rawQuery("SELECT * from TERMINE WHERE EXCEPTIONCONTEXTID = \"" + ID + "\"", null);
        int returnValue = c.getCount();
        c.close();
        return returnValue;
    }

    public Termin[] getExceptionsByWiederholungsID(int wiederholungsID) {
        Cursor c = db.rawQuery("SELECT * from TERMINE WHERE EXCEPTIONCONTEXTID = \"" + wiederholungsID + "\"", null);
        Termin[] returnArray = new Termin[c.getCount()];
        while (c.moveToNext()) {
            returnArray[c.getPosition()] = cursor2Termin(c);
        }
        return returnArray;
    }
    //____________________________________________________

    private Termin cursor2Termin(Cursor c) {
        return new Termin(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getInt(8), c.getInt(9), c.getInt(10), c.getInt(11), c.getString(12), c.getInt(13), c.getInt(14), c.getInt(15), c.getString(16), c.getInt(17));
    }

    private Plan cursor2Plan(Cursor c) {
        return new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));

    }

    private Module cursor2Module(Cursor c) {
        return new Module(c.getInt(0), c.getString(1), c.getInt(2));
    }
}