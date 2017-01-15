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

    /**
     * Initialisiert das DatabaseInterface mit der gegebenen Datenbank.
     *
     * @param db Muss eine beschreibbare Datenbank sein.
     */
    private DatabaseInterface(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Gibt die Instanz des DatabaseInterface zurueck. Initialisiert diese gegebenenfalls.
     *
     * @param c wird zur Initialisierung benoetigt.
     * @return DatabaseInterface
     */
    public static DatabaseInterface getInstance(Context c) {
        if (di == null) {
            di = new DatabaseInterface(new DatabaseHandler(c.getApplicationContext()).getWritableDatabase());
        }
        return di;
    }
    //---------------------------Add Data----------------------------------------------

    /**
     * Diese Funktion erstellt einen neuen Plan.
     *
     * @param ANZEIGENAME Der Name des Plans, welcher neu angelegt werden soll.
     * @param STARTDATE   Das Anfangsdatum, von wann der Plan gelten soll. Das STARTDATE muss gleich, oder vor dem ENDDATE liegen. Das Datum muss im Format jjjj-mm-dd sein.
     * @param ENDDATE     Das Enddatum, bis wann der Plan gelten soll. Das ENDDATE muss gleich, oder nach dem STARTDATE liegen. Das Datum muss im Format jjjj-mm-dd sein.
     * @return Die ID des neuen Plans wird zurueckgegeben. Falls diese -1 ist, dann wurde aufgrund eines Fehlers kein neuer Plan erstellt.
     */
    public long insertDataPlans(String ANZEIGENAME, String STARTDATE, String ENDDATE) {
        try {
            if ((Date.valueOf(STARTDATE).before(Date.valueOf(ENDDATE)) || (Date.valueOf(STARTDATE).equals(Date.valueOf(ENDDATE))))) {
                ContentValues values = new ContentValues();
                values.put("ANZEIGENAME", ANZEIGENAME);
                values.put("STARTTIME", STARTDATE);
                values.put("ENDTIME", ENDDATE);
                return db.insert("plans", null, values);
            } else {
                Log.d("DatabaseInterface", "insertDataPlans_STARTDATE_ENDDATE = fehlerhafte Eingabe");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Diese Funktion erstellt ein neues Modul.
     *
     * @param ANZEIGENAME Der Name des Moduls, welches neu angelegt werden soll.
     * @param SEMESTERID  Die ID des Semesterplans, welchem das neue Modul zugeordnet werden soll.
     * @return Die ID des neuen Modul wird zurueckgegeben. Falls diese -1 ist, dann wurde aufgrund eines Fehlers kein neues Modul erstellt.
     */
    public long insertDataModules(String ANZEIGENAME, int SEMESTERID) {
        try {
            ContentValues values = new ContentValues();

            values.put("ANZEIGENAME", ANZEIGENAME);
            values.put("SEMESTERID", SEMESTERID);
            return db.insert("modules", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Diese Funktion erstell einen neuen Termin.
     *
     * @param name               Der Name des Termins/Terminwiederholung/Termienwiederholungsausnahme welche neu angelegt werden soll.
     * @param startDate          Das Datum, an welchem der Termin/Terminwiederholung/Termienwiederholungsausnahme beginnen soll. Das Datum muss im Format jjjj-mm-dd sein.
     * @param wiederholungsEnde  Fuer die Terminwiederholung wird hier das Ende der Wiederholung festgelegt. Fuer die Termine/Termienwiederholungsausnahmen solle hier das selbe Datum wie in startDate eingetragen werden, da diese Spalte nicht benoetigt wird. Das Datum muss im Format jjjj-mm-dd sein.
     * @param startTime          Die Uhrzeit bei der der Termin/Terminwiederholung/Termienwiederholungsausnahme starten soll. Die Uhrzeit muss im Format hh:mm:ss sein.
     * @param endTime            Die Uhrzeit bei der der Termin/Terminwiederholung/Termienwiederholungsausnahme enden soll. Der Termin muss am selben Tag enden an dem er beginnt. Die endTime muss spaeter oder gleich als die startTime sein. Die Uhrzeit muss im Format hh:mm:ss sein.
     * @param ort                Der Ort an welchem der Termin stattfindet.
     * @param typ                Der Typ des Termins. Z.B.: Vorlesung, Seminar oder Praktikum
     * @param prioritaet         Die Zahl 0 bis einschliesslich 2. 0 = wichtig, 1 = normal, 2 = unwichtig
     * @param planID             Die PlanID des Plans, welchem der Termin zugeordnet werden soll.
     * @param modulID            Die ModulID des Moduls, dem dar Termin zugeordnet werden soll. Das Modul muss dem selben PLan zugeordnet sein, wie der Termin.
     * @param istGanztagsTermin  Ein Integer (0 bis 1) welcher beschreibt, ob ein Termin ein Ganztagstermin ist. 0 = normaler Termin, 1 = Ganztagstermin
     * @param dozent             Der Name des Dozenten.
     * @param periode            Ein Integer, welcher beschreibt in welchen Abstaenden ein Termin wiederholt wird. Ist die Periode !=0, dann ist der Termin eine Terminwiederholung oder eine Termienwiederholungsausnahme. Zulaessige Werte sind: 0 fuer keine Wiederholung, 7 fuer Woechentlich, 14 fuer alle 14 Tage und 28 fuer alle 4 Wochen.
     * @param isExeption         Ein Integer (0 bis 1) welcher beschreibt, ob ein Eintrag eine Terminwiederholungsausnahme (isExeption = 1) oder ein Termin, bzw. Terminwiederholung (isExeption = 0) ist.
     * @param exceptionContextID Die ID der Terminwiederholung, wenn eine Termienwiederholungsausnahme erstellt werden soll, oder 0, wenn ein Termin oder eine Terminwiederholung erstellt werden soll.
     * @param exceptionTargetDay Der Tag auf den sich die Terminwiederholungsausnahme bezieht. Das Datum muss im Format jjjj-mm-dd sein.
     * @param isdelete           Ein Integer (0 bis 1) welcher beschreibt, ob ein Termin einer Terminwiederholung geloescht wurde. 1 = geloescht, 0 = nicht geloescht
     * @return Die ID des neuen Termins/Terminwiederholung/Terminwiederholungsausnahme wird zurueckgegeben, oder -1, wenn ein Fehler aufgetreten ist.
     */
    public long insertDataTermine(String name, String startDate, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode, int isExeption, int exceptionContextID, String exceptionTargetDay, int isdelete) {
        try {
            if (Date.valueOf(startDate).equals(Date.valueOf(wiederholungsEnde)) || Date.valueOf(startDate).before(Date.valueOf(wiederholungsEnde))) {
                if (Time.valueOf(startTime).equals(Time.valueOf(endTime)) || Time.valueOf(startTime).before(Time.valueOf(endTime))) {
                    if (prioritaet == 0 || prioritaet == 1 || prioritaet == 2) {
                        Cursor c = db.rawQuery("SELECT SEMESTERID from modules WHERE ID = \"" + modulID + "\"", null);
                        c.moveToNext();
                        if (c.getInt(0) == modulID) {
                            if (istGanztagsTermin == 0 || istGanztagsTermin == 1) {
                                if (periode == 0 || periode == 7 || periode == 14 || periode == 28) {
                                    if (isExeption == 0 || isExeption == 1) {
                                        if (Date.valueOf(exceptionTargetDay) == Date.valueOf("01-01-0001") || Date.valueOf(exceptionTargetDay) != Date.valueOf("01-01-0001")) { //Pruefung ob der String exceptionTargetDay auch ein Date-String ist.
                                            if (isdelete == 0 || isdelete == 1) {
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
                                                values.put("ISDELETED", isdelete);
                                                return db.insert("termine", null, values);
                                            } else {
                                                Log.d("DatabaseInterface", "insertDataTermine_isdelete = fehlerhafte Eingabe: " + isdelete);
                                                return -1;
                                            }
                                        }
                                    } else {
                                        Log.d("DatabaseInterface", "insertDataTermine_isExeption = fehlerhafte Eingabe: " + isExeption);
                                        return -1;
                                    }
                                } else {
                                    Log.d("DatabaseInterface", "insertDataTermine_periode = fehlerhafte Eingabe: "+ periode);
                                    return -1;
                                }
                            } else {
                                Log.d("DatabaseInterface", "insertDataTermine_istGanztagsTermin = fehlerhafte Eingabe: " + istGanztagsTermin);
                                return -1;
                            }
                        } else {
                            Log.d("DatabaseInterface", "insertDataTermine_modulID = fehlerhafte Eingabe: " + modulID);
                            return -1;
                        }
                    } else {
                        Log.d("DatabaseInterface", "insertDataTermine_prioritaet = fehlerhafte Eingabe: " + prioritaet);
                        return -1;
                    }
                } else {
                    Log.d("DatabaseInterface", "insertDataTermine_startTime_endTime = fehlerhafte Eingabe: " + startTime + ";" + endTime);
                    return -1;
                }
            } else {
                Log.d("DatabaseInterface", "insertDataTermine_startDate_wiederholungsEnde = fehlerhafte Eingabe: " + startDate + ";" + wiederholungsEnde);
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    //------------------------------Get All Data-----------------------------------------

    /**
     * Eine Funktion, welche alle Plaene zurueckgibt.
     *
     * @return Plan[], oder null bei keinem vorhandenen Plan.
     */
    public Plan[] getAllPlans() {

        //Cursor c = db.rawQuery("select * from plans ORDER BY STARTTIME, ENDTIME", null);
        Cursor c = db.rawQuery("select * from plans", null);
        Plan[] returnPlans = new Plan[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnPlans[c.getPosition()] = cursor2Plan(c);
            b = c.moveToNext();
        }
        return returnPlans;
    }

    /**
     * Eine Funktion, welche alle Module zurueckgibt.
     *
     * @return Module[], oder null bei keinem vorhandenen Modul.
     */
    public Module[] getAllModules() {
        //Cursor c = db.rawQuery("select * from modules ORDER BY ANZEIGENAME", null);
        Cursor c = db.rawQuery("select * from modules", null);
        Module[] returnModule = new Module[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnModule[c.getPosition()] = cursor2Module(c);
            b = c.moveToNext();
        }
        return returnModule;
    }

    /**
     * Eine Funktion, welche alle Termine zurueckgibt.
     *
     * @return Termin[], oder null bei keinem vorhandenen Termin.
     */
    public Termin[] getAllTermine() {
        Cursor c = db.rawQuery("select * from termine ORDER BY STARTTIME, STARTTIME, ENDDATA, ENDTIME ", null);
        //Cursor c = db.rawQuery("select * from termine", null);
        Termin[] returnTermin = new Termin[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnTermin[c.getPosition()] = cursor2Termin(c);
            b = c.moveToNext();
        }

        return returnTermin;
    }
    //----------------------------Get Data By ID---------------------------

    /**
     * Diese Funktion gibt einen Plan mit einer bestimmten ID zurueck.
     *
     * @param id ID des Plans welcher zurueckgegeben werden soll.
     * @return Plan, wenn kein Plan gefunden wurde, wird null zurueckegegeben.
     */
    public Plan getPlanByID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "plans", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        boolean b = c.moveToNext();
        if (b == true) {
            Plan p = cursor2Plan(c);
            c.close();
            return p;
        } else {
            return null;
        }
    }

    /**
     * Diese Funktion gibt ein Modul mit einer bestimmten ID zurueck.
     *
     * @param id ID des Moduls welches zurueckgegeben werden soll.
     * @return Modul, wenn kein Modul gefunden wurde, wird null zurueckegegeben.
     */
    public Module getModuleByID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        boolean b = c.moveToNext();
        if (b == true) {
            Module p = cursor2Module(c);
            c.close();
            return p;
        } else {
            return null;
        }
    }

    /**
     * Diese Funktion gibt einen Termin mit einer bestimmten ID zurueck.
     *
     * @param id ID des Termins welcher zurueckgegeben werden soll.
     * @return Termin, wenn kein Termin gefunden wurde, wird null zurueckegegeben.
     */
    public Termin getTerminByID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "Termine", columns, "ID = " + id, "", "", "", "");
        Log.d("database", query);
        Cursor c = db.rawQuery(query, null);
        boolean b = c.moveToNext();
        if (b == true) {
            Termin p = cursor2Termin(c);
            c.close();
            return p;
        } else {
            return null;
        }
    }

    /**
     * Diese Funktion gibt alle Termine zurueck, welche zu einem bestimmten Plan gehoeren.
     *
     * @param PlanID ID des Planes, zu dem alle Termine gesucht werden sollen.
     * @return Termin[], wenn kein Termin gefunden wurde, wird null zurueckegegeben.
     */
    public Termin[] getTermineByPlanID(int PlanID) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "SEMESTERID = " + PlanID, "", "", "", "");
        Cursor c = db.rawQuery(query, null);
        Termin[] returnArray = new Termin[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnArray[c.getPosition()] = cursor2Termin(c);
            b = c.moveToNext();
        }
        c.close();
        return returnArray;
    }

    /**
     * Diese Funktion gibt alle Module zurueck, welche zu einem bestimmten Plan gehoeren.
     *
     * @param PlanID ID des Plans, zu dem alle Module gesucht werden sollen.
     * @return Modul[], wenn kein Modul gefunden wurde, wird null zurueckegegeben.
     */
    public Module[] getModulesByPlanID(int PlanID) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "modules", columns, "SEMESTERID = " + PlanID, "", "", "", "");
        Cursor c = db.rawQuery(query, null);
        Module[] returnArray = new Module[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnArray[c.getPosition()] = cursor2Module(c);
            b = c.moveToNext();
        }
        c.close();
        return returnArray;
    }

    /**
     * Diese Funktion gibt alle Termine zurueck, welche zu einem bestimmten Modul gehoeren.
     *
     * @param id ID des Moduls, zu dem alle Termine gesucht werden sollen.
     * @return Termin[], wenn kein Termin gefunden wurde, wird null zurueckegegeben.
     */
    public Termin[] getTermineByModulID(int id) {
        String[] columns = {"*"};
        String query = SQLiteQueryBuilder.buildQueryString(false, "termine", columns, "ModulID = " + id, "", "", "", "");
        Cursor c = db.rawQuery(query, null);
        Termin[] returnArray = new Termin[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnArray[c.getPosition()] = cursor2Termin(c);
            b = c.moveToNext();
        }
        c.close();
        return returnArray;
    }

    /**
     * Gibt alle Termine zurueck, welche zu einem bestimmten Plan gehoeren, aber keinem Modul zugeordnet sind.
     *
     * @param id ID des Plans, zu dem alle Termine gesucht werden, die keinem Modul zugeordnet sind.
     * @return Termin[], wenn kein Termin gefunden wurde, wird null zurueckegegeben.
     */
    public Termin[] getTermineByPlanIDButNotModulID(int id) {
        String[] columns = {"*"};
        String query = "select * from termine WHERE SEMESTERID =  \"" + id + "\" AND ModulID = \"0\" ORDER BY STARTTIME, ENDTIME";
        Cursor c = db.rawQuery(query, null);
        Termin[] returnArray = new Termin[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnArray[c.getPosition()] = cursor2Termin(c);
            b = c.moveToNext();
        }
        c.close();
        return returnArray;
    }

    /**
     * Diese Funktion gibt alle Termine zurueck, welche an einem bestimmten Datum stattfinden.
     *
     * @param date           Das gewuenschte Suchdatum. Das Datum muss im Format jjjj-mm-dd sein.
     * @param planOrModuleID Die ID des Plans oder Moduls der Termin des gewuenschten Suchdatums.
     * @param isPlan         Ob die gegebene ID eine Plan ID ist. True = PlanID , False = ModulID
     * @return Termin[], wenn kein Termin gefunden wurde, wird null zurueckegegeben.
     */
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
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
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
            b = c.moveToNext();
        }
        c.close();
        //Alle Terminwiederholungsausnahme holen, die für der aktuellen Tag gelten
        query = "select * from termine WHERE EXCEPTIONTARGETDAY =  \"" + date + "\" AND " + restrictionExtension;
        c = db.rawQuery(query, null);
        b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            int exceptionContextID = c.getInt(15);
            TerminwiederholungsIgnorierArray.add(exceptionContextID);
            b = c.moveToNext();
        }
        c.close();
        //Query werfen, der mir alle Terminwiederholungen gibt.
        query = "select * from termine  WHERE PERIODE<>'0' AND ISEXEPTION='0' AND " + restrictionExtension;
        c = db.rawQuery(query, null);
        b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
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
            b = c.moveToNext();
        }
        //returnArray Sortieren
        Collections.sort(returnArray);
        return returnArray.toArray(new Termin[returnArray.size()]);
    }
    //-------------------------searching for---------------------------------------------

    /**
     * Es werden alle Plaene zurueckgegeben, welche den Suchstring beinhalten.
     *
     * @param searchword Eingegebener String zur Plansuche.
     * @return Plan[], wenn kein Plan gefunden wurde, wird null zurueckegegeben.
     */
    public Plan[] getPlansByString(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM plans WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Plan[] returnPlan = new Plan[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnPlan[c.getPosition()] = cursor2Plan(c);
            b = c.moveToNext();
        }
        c.close();
        return returnPlan;
    }

    /**
     * Es werden alle Module zurueckgegeben, welche den Suchstring beinhalten.
     *
     * @param searchword Eingegebener String zur Modulsuche.
     * @return Module[], wenn kein Modul gefunden wurde, wird null zurueckegegeben.
     */
    public Module[] getModulesByString(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM modules WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Module[] returnModules = new Module[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnModules[c.getPosition()] = cursor2Module(c);
            b = c.moveToNext();
        }
        c.close();
        return returnModules;
    }

    /**
     * Es werden alle Termine zurueckgegeben, welche den Suchstring beinhalten.
     *
     * @param searchword Eingegebener String zur Terminsuche.
     * @return Termin[], wenn kein Termin gefunden wurde, wird null zurueckegegeben.
     */
    public Termin[] getTermineByString(String searchword) {
        Cursor c = db.rawQuery("SELECT * FROM termine WHERE ANZEIGENAME LIKE \"%" + searchword + "%\"ORDER BY ZEITRAUM", null);
        Termin[] returnTermine = new Termin[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnTermine[c.getPosition()] = cursor2Termin(c);
            b = c.moveToNext();
        }
        c.close();
        return returnTermine;
    }
    //----------------------Update------------------------------------------------------

    /**
     * An einem Plan werden die gewuenscheten Aederungen vorgenommen.
     *
     * @param ID          ID des Plans, welcher geaendert werden soll.
     * @param ANZEIGENAME Der geltende ANZEIGENAME kann hier zum gewuenschten String geaendert werden.
     * @param STARTDATE   Das geltende Startdatum kann hier zum gewuenschten Startdatum geaendert werden. Das STARTDATE muss gleich oder vor dem ENDDATE liegen. Das Datum muss im Format jjjj-mm-dd sein.
     * @param ENDDATE     Das geltende Enddatum kann hier zum gewuenschten Enddatum geaendert werden. Das ENDDATE muss gleich oder nach dem STARTDATE liegen. Das Datum muss im Format jjjj-mm-dd sein.
     * @return Es wird die Nummer der geaenderten Felder zurueckgegeben, oder -1, wenn das Enddatum vor dem Startdatum liegt.
     */
    public long updateDataPlans(int ID, String ANZEIGENAME, String STARTDATE, String ENDDATE) {
        try {
            if ((Date.valueOf(STARTDATE).before(Date.valueOf(ENDDATE)) || (Date.valueOf(STARTDATE).equals(Date.valueOf(ENDDATE))))) {
                ContentValues values = new ContentValues();
                values.put("ANZEIGENAME", ANZEIGENAME);
                values.put("STARTTIME", STARTDATE);
                values.put("ENDTIME", ENDDATE);
                return db.update("plans", values, "ID =" + String.valueOf(ID), null);
            } else {
                Log.d("DatabaseInterface", "updateDataPlans_STARTDATE_ENDDATE = fehlerhafte Eingabe");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * An einem Modul werden die gewuenscheten Aederungen vorgenommen.
     *
     * @param ID          ID des Moduls welches geaendert werden soll.
     * @param ANZEIGENAME Der geltende ANZEIGENAME kann hier zum gewuenschten String geaendert werden.
     * @param SEMESTERID  ID des Plans, zu dem das Modul gehoeren soll.
     * @return Es wird die Nummer der geaenderten Felder zurueckgegeben, oder -1, wenn das Enddatum vor dem Startdatum liegt.
     */
    public long updateDataModules(int ID, String ANZEIGENAME, int SEMESTERID) {
        try {
            ContentValues values = new ContentValues();

            values.put("ANZEIGENAME", ANZEIGENAME);
            values.put("SEMESTERID", SEMESTERID);
            return db.update("Modules", values, "ID =" + String.valueOf(ID), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * An einem Termin werden die gewuenscheten Aenderungen vorgenommen. Saemtliche Parameter entsprechen den Parametern der Funktion "insertDataTermine". Die ID muss zusaetzlich angegeben werden.
     *
     * @param id ID des Termins welcher geaendert werden soll.
     * @return Es wird die Nummer der geaenderten Felder zurueckgegeben, oder -1, wenn das Enddatum vor dem Startdatum, bzw. die endTime vor der startTime liegt.
     */
    public long updateDataTermine(int id, String name, String startDate, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode, int isExeption, int exceptionContextID, String exceptionTargetDay, int isdelete) {
        try {
            if (Date.valueOf(startDate).equals(Date.valueOf(wiederholungsEnde)) || Date.valueOf(startDate).before(Date.valueOf(wiederholungsEnde))) {
                if (Time.valueOf(startTime).equals(Time.valueOf(endTime)) || Time.valueOf(startTime).before(Time.valueOf(endTime))) {
                    if (prioritaet == 0 || prioritaet == 1 || prioritaet == 2) {
                        Cursor c = db.rawQuery("SELECT SEMESTERID from modules WHERE ID = \"" + modulID + "\"", null);
                        c.moveToNext();
                        if (c.getInt(0) == modulID) {
                            if (istGanztagsTermin == 0 || istGanztagsTermin == 1) {
                                if (periode == 0 || periode == 7 || periode == 14 || periode == 28) {
                                    if (isExeption == 0 || isExeption == 1) {
                                        if (Date.valueOf(exceptionTargetDay) == Date.valueOf("0001-01-01") || Date.valueOf(exceptionTargetDay) != Date.valueOf("0001-01-01")) { //Pruefung ob der String exceptionTargetDay auch ein Date-String ist.
                                            if (isdelete == 0 || isdelete == 1) {
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
                                                values.put("ISDELETED", isdelete);
                                                return db.update("Termine", values, "ID =" + String.valueOf(id), null);
                                            } else {
                                                Log.d("DatabaseInterface", "updateDataTermine_isdelete = fehlerhafte Eingabe"+ isdelete);
                                                return -1;
                                            }
                                        }
                                    } else {
                                        Log.d("DatabaseInterface", "updateDataTermine_isExeption = fehlerhafte Eingabe"+ isExeption);
                                        return -1;
                                    }
                                } else {
                                    Log.d("DatabaseInterface", "updateDataTermine_periode = fehlerhafte Eingabe"+ periode);
                                    return -1;
                                }
                            } else {
                                Log.d("DatabaseInterface", "updateDataTermine_istGanztagsTermin = fehlerhafte Eingabe"+ istGanztagsTermin);
                                return -1;
                            }
                        } else {
                            Log.d("DatabaseInterface", "updateDataTermine_modulID = fehlerhafte Eingabe"+ modulID);
                            return -1;
                        }
                    } else {
                        Log.d("DatabaseInterface", "updateDataTermine_prioritaet = fehlerhafte Eingabe"+ prioritaet);
                        return -1;
                    }
                } else {
                    Log.d("DatabaseInterface", "updateDataTermine_startTime_endTime = fehlerhafte Eingabe" + endTime);
                    return -1;
                }
            } else {
                Log.d("DatabaseInterface", "updateDataTermine_startDate_wiederholungsEnde = fehlerhafte Eingabe"+ wiederholungsEnde);
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    //_________________Delete_____________________

    /**
     * Loescht eine Zeile in der Tabelle Plans
     *
     * @param ID ID der zuloeschende Zeile.
     */
    public void deletePlanByID(int ID) {
        try {
            db.delete("PLANS", "ID =  \"" + ID + "\" ", null);
            Log.d("DatabaseInterface", "Plan_geloescht: " + ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loescht eine Zeile in der Tabelle Moduls
     *
     * @param ID ID der zuloeschende Zeile.
     */
    public void deleteMudulByID(int ID) {
        try {
            db.delete("MODULES", "ID =  \"" + ID + "\" ", null);
            Log.d("DatabaseInterface", "Modul_geloescht: " + ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loescht eine Zeile in der Tabelle Termine
     *
     * @param ID ID der zuloeschende Zeile.
     */
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

    /**
     * Diese Funktion gibt die Anzahl der Ausnahmen zu einer Terminwiederholung zurueck.
     *
     * @param ID Die ID der Terminwiederholung, zu welcher die Ausnahmen gesucht werden sollen.
     * @return Long, oder -1 bei einem unvorhergesehenen Fehler.
     */
    public long getCountExceptionsByID(int ID) {
        try {
            Cursor c = db.rawQuery("SELECT * from TERMINE WHERE EXCEPTIONCONTEXTID = \"" + ID + "\"", null);

            long returnValue = c.getCount();
            c.close();
            return returnValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gibt alle Termienwiederholungsausnahmen zurueck, welche zu einer bestimmten Terminwiederholung gehoeren.
     *
     * @param wiederholungsID Die ID der Terminwiederholung, zu welcher die Ausnahmen gesucht werden sollen.
     * @return Termin[], null wenn es keine Treffer gibt.
     */
    public Termin[] getExceptionsByWiederholungsID(int wiederholungsID) {
        Cursor c = db.rawQuery("SELECT * from TERMINE WHERE EXCEPTIONCONTEXTID = \"" + wiederholungsID + "\"", null);
        Termin[] returnArray = new Termin[c.getCount()];
        boolean b = c.moveToNext();
        if (b == false) {
            return null;
        }
        while (b == true) {
            returnArray[c.getPosition()] = cursor2Termin(c);
            b = c.moveToNext();
        }
        return returnArray;
    }
    //____________________________________________________

    /**
     * Wandelt einen Cursor in ein Terminobjekt um.
     *
     * @param c Der Cursor welcher umgewandelt werden soll.
     * @return Terminobjekt (Cursor)
     */
    private Termin cursor2Termin(Cursor c) {
        return new Termin(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getString(6), c.getString(7), c.getInt(8), c.getInt(9), c.getInt(10), c.getInt(11), c.getString(12), c.getInt(13), c.getInt(14), c.getInt(15), c.getString(16), c.getInt(17));
    }

    /**
     * Wandelt einen Cursor in ein Planobjekt um.
     *
     * @param c Der Cursor welcher umgewandelt werden soll.
     * @return Planobjekt (Planobjekt)
     */
    private Plan cursor2Plan(Cursor c) {
        return new Plan(c.getInt(0), c.getString(1), c.getString(2), c.getString(3));
    }

    /**
     * Wandelt einen Cursor in ein Modulobjekt um.
     *
     * @param c Der Cursor welcher umgewandelt werden soll.
     * @return Modulobjekt (Module)
     */
    private Module cursor2Module(Cursor c) {
        return new Module(c.getInt(0), c.getString(1), c.getInt(2));
    }
}