package de.hsmw.semestermanager;

import android.content.Context;
import android.content.Intent;

import java.sql.Date;
import java.sql.Time;
import java.util.GregorianCalendar;

/**
 * Created by Benjamin on 02.12.2016.
 */

public class Termin implements DatabaseObject, Comparable<Termin>, Cloneable {
    private int id;
    private String name;
    private Date startDate;
    private Date wiederholungsEnde;
    private Time startTime;
    private Time endTime;
    private String ort;
    private String typ;
    /**
     * 0 Wichtig - 1 Normal - 2 Unwichtig
     */
    private int prioritaet;
    private int planID;
    private int modulID;
    /**
     * 0 false; 1 true -> SQLite kann keine Booleans.
     */
    private int isGanztagsTermin;
    private String dozent;
    /**
     * 0 - Wenn sich der Termin nicht wiederholen soll. Wiederholungsintervall in Tagen.
     */
    private int periode;
    /**
     * Gibt an, dass dieser Termin eine Terminwiederholungsausnmahme ist. 0 - Keine Ausnahme.
     */
    private int isException;
    /**
     * ID der Terminwiederholung für den diese Ausnahme gilt.
     */
    private int exceptionContextID;
    /**
     * Der Tag den eine Terminwiederholungsausnahme in der Terminwiederholung ersetzen soll.
     */
    private Date exceptionTargetDay;
    /**
     * Gibt an, dass die Ausnahme "nur" zum löschen des Termins am exceptionTargetday dient.
     */
    private int isDeleted;

    public Termin(int id, String name, String startDate, String wiederholungsEnde, String startTime, String endTime, String ort, String typ, int prioritaet, int planID, int modulID, int isGanztagsTermin, String dozent, int periode, int isException, int exceptionContextID, String exceptionTargetDay, int isDeleted) {
        this.id = id;
        this.name = name;
        this.startDate = Date.valueOf(startDate);
        this.wiederholungsEnde = Date.valueOf(wiederholungsEnde);
        this.startTime = Time.valueOf(startTime);
        this.endTime = Time.valueOf(endTime);
        this.ort = ort;
        this.typ = typ;
        this.prioritaet = prioritaet;
        this.planID = planID;
        this.modulID = modulID;
        this.isGanztagsTermin = isGanztagsTermin;
        this.dozent = dozent;
        this.periode = periode;
        this.isException = isException;
        this.exceptionContextID = exceptionContextID;
        this.exceptionTargetDay = Date.valueOf(exceptionTargetDay);
        this.isDeleted = isDeleted;
    }

    public Termin(int id, String name, Date startDate, Date wiederholungsEnde, Time startTime, Time endTime, String ort, String typ, int prioritaet, int planID, int modulID, int isGanztagsTermin, String dozent, int periode, int isException, int exceptionContextID, Date exceptionTargetDay, int isDeleted) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.wiederholungsEnde = wiederholungsEnde;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ort = ort;
        this.typ = typ;
        this.prioritaet = prioritaet;
        this.planID = planID;
        this.modulID = modulID;
        this.isGanztagsTermin = isGanztagsTermin;
        this.dozent = dozent;
        this.periode = periode;
        this.isException = isException;
        this.exceptionContextID = exceptionContextID;
        this.exceptionTargetDay = exceptionTargetDay;
        this.isDeleted = isDeleted;
    }

    public String getDateString() {
        String returnString;
        String[] temp = startDate.toString().split("-");
        returnString = temp[2] + "." + temp[1];
        if (!startDate.equals(wiederholungsEnde) && periode != 0) {
            returnString += " - ";
            temp = wiederholungsEnde.toString().split("-");
            returnString += temp[2] + "." + temp[1];
        }
        return returnString;
    }

    public String getTimeString() {
        String returnString;
        if (isGanztagsTermin != 0) {
            returnString = "Ganztags";
        } else {
            String[] temp = startTime.toString().split(":");
            returnString = temp[0] + ":" + temp[1] + " - ";
            temp = endTime.toString().split(":");
            returnString += temp[0] + ":" + temp[1];
        }
        return returnString;
    }

    public String getWiederholungsString() {
        switch (periode) {
            case 0:
                return "Keine";
            case 7:
                return "Wöchentlich";
            case 14:
                return "Zweiwöchentlich";
            case 28:
                return "Vierwöchentlich";
            default:
                return "PERIODE KORRUPT";
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getWiederholungsEnde() {
        return wiederholungsEnde;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public String getOrt() {
        return ort;
    }

    public String getTyp() {
        return typ;
    }

    public int getPrioritaet() {
        return prioritaet;
    }

    public int getPlanID() {
        return planID;
    }

    public int getModulID() {
        return modulID;
    }

    public int getIsGanztagsTermin() {
        return isGanztagsTermin;
    }

    public String getDozent() {
        return dozent;
    }

    public int getPeriode() {
        return periode;
    }

    public int getIsException() {
        return isException;
    }

    public int getExceptionContextID() {
        return exceptionContextID;
    }

    public Date getExceptionTargetDay() {
        return exceptionTargetDay;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    /**
     * Vergleicht nur die Startzeit der Termine.
     */
    @Override
    public int compareTo(Termin o) {
        int comparedStart = startTime.compareTo(o.startTime);
        if (comparedStart == 0){
            return  endTime.compareTo(o.endTime);
        }else{
            return comparedStart;
        }
    }

    /**
     * Gibt eine neue Termininstanz für eine Terminwiederholung zurück, die am gesuchten Tag stattfindet.
     * @param date Datum für das ein Termin gesucht wird.
     * @return Null, wenn dieser Termin keine Terminwiederholung ist, oder zu dem gegebenen Datum kein Termin für die Wiederholung stattfindet.
     */
    public Termin getTerminAtDate(Date date) {
        if (isException != 0 || periode == 0) {
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        while (calendar.getTime().compareTo(date) < 0 && calendar.getTime().compareTo(wiederholungsEnde) < 0) {
            calendar.add(GregorianCalendar.DAY_OF_MONTH, periode);
        }
        if (calendar.getTime().compareTo(date) == 0) {
            Termin returnTermin = this.clone();
            returnTermin.startDate = new Date(calendar.getTime().getTime());
            return returnTermin;
        } else {
            return null;
        }
    }

    public long getDuration() {
        if (isGanztagsTermin == 0) {
            return (endTime.getTime() - startTime.getTime());
        } else {
            return 0;
        }
    }

    /**
     * Berechnet, wie viel Zeit alle Regulären Termine einer Terminwiederholung insgesamt dauern. Terminwiederholungsausnahmen sind von der Berechnung aktiv einbezogen.
     *
     * @param date Das Datum bis zu dem gezählt werden soll.
     * @param c
     * @return Wie viel Zeit alle Regulären Termine einer Terminwiederholung insgesamt dauern. Terminwiederholungsausnahmen sind von der Berechnung aktiv einbezogen.
     */
    public long getDurationSumUntil(Date date, Context c) {
        if (isException != 0 || periode == 0) {
            return 0;
        }

        DatabaseInterface di = DatabaseInterface.getInstance(c);
        Termin[] exceptions = di.getExceptionsByWiederholungsID(id);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        long returnValue = 0;
        long terminOnce = getDuration();
        if (exceptions != null) {
            for (Termin exception : exceptions) {
                if (exception.isDeleted == 0 && exception.getStartDate().compareTo(date) < 0) {
                    returnValue += exception.getDuration();
                }
                if (exception.getExceptionTargetDay().compareTo(date) < 0) {
                    returnValue -= terminOnce;
                }
            }
        }
        while (calendar.getTime().compareTo(date) <= 0 && calendar.getTime().compareTo(wiederholungsEnde) <= 0) {
            calendar.add(GregorianCalendar.DAY_OF_MONTH, periode);
            returnValue += terminOnce;
        }
        return returnValue;
    }

    @Override
    public Termin clone() {
        return new Termin(id, name, startDate, wiederholungsEnde, startTime, endTime, ort, typ, prioritaet, planID, modulID, isGanztagsTermin, dozent, periode, isException, exceptionContextID, exceptionTargetDay, isDeleted);
    }

    /**
     * Gibt die Instanz des zu dem Termin gehörigen Moduls zurück.
     *
     * @param c Context für den Datenbankzugriff.
     * @return Null, wenn kein Modul angegeben.
     */
    public Module getModule(Context c) {
        DatabaseInterface di = DatabaseInterface.getInstance(c);
        Module returnModule = di.getModuleByID(getModulID());
        return returnModule;
    }

    /**
     * Löscht den Termin in der Datenbank und alle zugehörigen Exceptions, wenn vorhanden.
     *
     * @param c Context für der Datenbankzugriff.
     */
    public void delete(Context c) {
        DatabaseInterface di = DatabaseInterface.getInstance(c);
        di.deleteTerminByID(id);
    }

    /**
     * Startet einen Intent, um den Termin zu bearbeiten. Unterscheidet dabei zwischen normalen Terminen und Wiederholungsausnahmen.
     *
     * @param c Context für Datenbankzugriffe.
     */
    public void edit(Context c) {
        if(periode < 1 || isException < 1){
            Intent i = new Intent("de.hsmw.semestermanager.TerminInput");
            i.putExtra("ID", getId());
            c.startActivity(i);
            return;
        }

        Intent i = new Intent("de.hsmw.semestermanager.ExceptionInput");
        i.putExtra("ID", getId());

        i.putExtra("terminName", getName());
        i.putExtra("startDate", Helper.sqlToGermanDate(getStartDate().toString()));
        i.putExtra("wiederholungsEnde", Helper.sqlToGermanDate(getWiederholungsEnde().toString()));
        i.putExtra("startZeit", getStartTime().toString().substring(0,5));
        i.putExtra("endZeit", getEndTime().toString().substring(0,5));
        i.putExtra("ort", getOrt());
        i.putExtra("typ", getTyp());
        i.putExtra("priorität", getPrioritaet());
        i.putExtra("planID", getPlanID());
        i.putExtra("modulID", getModulID());
        i.putExtra("isGanztagsTermin", getIsGanztagsTermin());
        i.putExtra("dozent", getDozent());
        i.putExtra("periode", getPeriode());

        i.putExtra("exceptionContextID", getExceptionContextID());
        i.putExtra("targetDay", Helper.sqlToGermanDate(getExceptionTargetDay().toString()));
        i.putExtra("isDeleted", getIsDeleted());

        c.startActivity(i);
    }

    /**
     * Startet einen Intent, um eine Terminwiederholungsausnahme zu erstellen.
     * Die Funktion läuft ins Leere, wenn der Termin keine Wiederholung ist.
     * @param c Context für Datenbankzugriffe.
     */
    public void createException(Context c){

        if(periode < 1){
            return;
        }

        Intent i = new Intent("de.hsmw.semestermanager.ExceptionInput");
        i.putExtra("terminName", getName());
        i.putExtra("startDate", Helper.sqlToGermanDate(getStartDate().toString()));
        i.putExtra("wiederholungsEnde", Helper.sqlToGermanDate(getWiederholungsEnde().toString()));
        i.putExtra("startZeit", getStartTime().toString().substring(0,5));
        i.putExtra("endZeit", getEndTime().toString().substring(0,5));
        i.putExtra("ort", getOrt());
        i.putExtra("typ", getTyp());
        i.putExtra("priorität", getPrioritaet());
        i.putExtra("planID", getPlanID());
        i.putExtra("modulID", getModulID());
        i.putExtra("isGanztagsTermin", getIsGanztagsTermin());
        i.putExtra("dozent", getDozent());
        i.putExtra("periode", getPeriode());

        i.putExtra("exceptionContextID", getId());
        i.putExtra("targetDay", Helper.sqlToGermanDate(getStartDate().toString()));

        c.startActivity(i);
    }
}