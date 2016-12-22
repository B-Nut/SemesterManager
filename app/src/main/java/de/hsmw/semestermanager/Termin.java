package de.hsmw.semestermanager;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Benjamin on 02.12.2016.
 */

public class Termin implements DatabaseObject{
    int id;
    String name;
    Date startDate, wiederholungsStart, wiederholungsEnde;
    Time startTime, endTime;
    String ort;
    String typ;
    int prioritaet;
    int planID;
    int modulID;
    int istGanztagsTermin; //0 false; 1 true -> SQLite kann keine Booleans
    String dozent;
    int periode;
    boolean isException;
    int exceptionContextID;
    Date exceptionTargetDay;

    public Termin(int id, String name, Date startDate, Date wiederholungsStart, Date wiederholungsEnde, Time startTime, Time endTime, String ort, String typ, int prioritaet, int planID, int modulID, int istGanztagsTermin, String dozent, int periode, boolean isException, int exceptionContextID, Date exceptionTargetDay) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.wiederholungsStart = wiederholungsStart;
        this.wiederholungsEnde = wiederholungsEnde;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ort = ort;
        this.typ = typ;
        this.prioritaet = prioritaet;
        this.planID = planID;
        this.modulID = modulID;
        this.istGanztagsTermin = istGanztagsTermin;
        this.dozent = dozent;
        this.periode = periode;
        this.isException = isException;
        this.exceptionContextID = exceptionContextID;
        this.exceptionTargetDay = exceptionTargetDay;
    }

    public String getDateString(){
        String returnString;
        String[] temp = startDate.toString().split("-");
        returnString = temp[2] + "." + temp[1];
        return returnString;
    }
    public String getTimeString(){
        String returnString;
        String[] temp = startTime.toString().split(":");
        returnString = temp[0] + ":" + temp[1] + " - ";
        temp = endTime.toString().split(":");
        returnString += temp[0] + ":" + temp[1];
        return returnString;
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

    public Date getWiederholungsStart() {
        return wiederholungsStart;
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

    public int getIstGanztagsTermin() {
        return istGanztagsTermin;
    }

    public String getDozent() {
        return dozent;
    }

    public int getPeriode() {
        return periode;
    }

    public Time getDuration(){
        return  new Time(endTime.getTime() - startTime.getTime());
    }
}
