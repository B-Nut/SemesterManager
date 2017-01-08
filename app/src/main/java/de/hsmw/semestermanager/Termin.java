package de.hsmw.semestermanager;

import android.content.Context;

import java.sql.Date;
import java.sql.Time;
import java.util.GregorianCalendar;

/**
 * Created by Benjamin on 02.12.2016.
 */

public class Termin implements DatabaseObject, Comparable<Termin>{
    private int id;
    private String name;
    private Date startDate;
    private Date wiederholungsEnde;
    private Time startTime;
    private Time endTime;
    private String ort;
    private String typ;
    private int prioritaet;
    private int planID;
    private int modulID;
    private int isGanztagsTermin; //0 false; 1 true -> SQLite kann keine Booleans
    private String dozent;
    private int periode;
    private int isException;
    private int exceptionContextID;
    private Date exceptionTargetDay;
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

    public Termin(int id, String name, Date startDate, Date wiederholungsEnde, Time startTime, Time endTime, String ort, String typ, int prioritaet, int planID, int modulID, int isGanztagsTermin, String dozent, int periode, int isException, int exceptionContextID, Date exceptionTargetDay,int isDeleted) {
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

    public String getDateString(){
        String returnString;
        String[] temp = startDate.toString().split("-");
        returnString = temp[2] + "." + temp[1];
        return returnString;
    }
    public String getTimeString(){
        String returnString;
        if (isGanztagsTermin != 0){
            returnString = "Ganztags";
        }else {
            String[] temp = startTime.toString().split(":");
            returnString = temp[0] + ":" + temp[1] + " - ";
            temp = endTime.toString().split(":");
            returnString += temp[0] + ":" + temp[1];
        }
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

    public int getIsException(){return isException ;}
    public int getExceptionContextID(){return exceptionContextID;}
    public Date getExceptionTargetDay() {return exceptionTargetDay;}
    public  int getIsDeleted(){return isDeleted;}

    @Override
    public int compareTo(Termin o) {
        int comparedStart = Helper.compareSQLTime(startTime, o.startTime);
        if(comparedStart == 0){
            return  Helper.compareSQLTime(endTime, o.endTime);
        }else{
            return comparedStart;
        }
    }

  /*  public Date getDuration(){
        return new Date(endDate.getTime() - startDate.getTime());
    }*/

    public Termin getTerminAtDate(Date date){
        if(isException != 0 || periode == 0){
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        while (calendar.getTime().compareTo(date) < 0 && calendar.getTime().compareTo(wiederholungsEnde) < 0) {
            calendar.add(GregorianCalendar.DAY_OF_MONTH, periode);
        }
        if(calendar.getTime().compareTo(date) == 0){
            Termin returnTermin = this.clone();
            returnTermin.startDate = new Date (calendar.getTime().getTime());
            return returnTermin;
        }else {
            return null;
        }
    }

    /**
     * Berechnet, wie viel Zeit alle Regulären Termine einer Terminwiederholung insgesamt dauern. Terminwiederholungsausnahmen sind von der Berechnung aktiv einbezogen.
     * @param date Das Datum bis zu dem gezählt werden soll.
     * @param c
     * @return Wie viel Zeit alle Regulären Termine einer Terminwiederholung insgesamt dauern. Terminwiederholungsausnahmen sind von der Berechnung aktiv einbezogen.
     */
    public long getDurationSumUntil(Date date, Context c){
        if(isException != 0 || periode == 0){
            return 0;
        }

        DatabaseInterface di = DatabaseInterface.getInstance(c);
        Termin[] exceptions = di.getExceptionsByWiederholungsID(id);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        long returnValue = 0;
        long terminOnce = getDuration();
        for(Termin exception : exceptions){
            if (exception.getStartDate().compareTo(date) < 0) {
                returnValue += exception.getDuration();
            }
            if (exception.getExceptionTargetDay().compareTo(date) < 0) {
                returnValue -= exception.getDuration();
            }
        }
        while (calendar.getTime().compareTo(date) <= 0 && calendar.getTime().compareTo(wiederholungsEnde) <= 0) {
            calendar.add(GregorianCalendar.DAY_OF_MONTH, periode);
            returnValue += terminOnce;
        }
        returnValue -= (di.getCountExceptionsByID(id) * terminOnce);
        return returnValue;
    }
    public long getDuration(){
        if(isGanztagsTermin == 0) {
            return (endTime.getTime() - startTime.getTime());
        }else{
            return 0;
        }
    }

    public Termin clone(){
       return new Termin(id,name,startDate,wiederholungsEnde,startTime,endTime,ort,typ,prioritaet,planID,modulID,isGanztagsTermin,dozent,periode,isException,exceptionContextID,exceptionTargetDay, isDeleted);
    }
    public Module getModule(Context c){
        DatabaseInterface di = DatabaseInterface.getInstance(c);
        Module returnModule = di.getDataByIDModules(getModulID());
        return returnModule;
    }
    public void resetModule(){
        modulID = 0;
    }
    public void updateDatabase(Context c){
        DatabaseInterface di = DatabaseInterface.getInstance(c);
        di.updateDataTermine(id,name,startDate.toString(),wiederholungsEnde.toString(),startTime.toString(),endTime.toString(),ort,typ,prioritaet,planID,modulID,isGanztagsTermin,dozent,periode,isException,exceptionContextID,exceptionTargetDay.toString(),isDeleted);
    }
}