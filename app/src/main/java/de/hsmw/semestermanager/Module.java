package de.hsmw.semestermanager;

import android.content.Context;
import android.icu.text.TimeZoneFormat;

import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Benjamin on 02.12.2016.
 */

public class Module implements DatabaseObject{
    private  int id;
    private  String name;
    private  int planID;

    public Module(int id, String name, int planID){
        this.id = id;
        this.name = name;
        this.planID = planID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPlanID() {
        return planID;
    }
    public String getProgressString(Context c){
        DatabaseInterface di = DatabaseInterface.getInstance(c);

        long sumUntilNow = 0;
        long sumTotal = 0;
        Termin[] termine = di.getTermineByModulID(id);
        for (Termin t : termine){
            if (t.getPeriode() == 0 || t.getIsException() != 0){
                sumTotal += t.getDuration();
                if (Calendar.getInstance().getTime().compareTo(t.getStartDate()) >= 0){
                    sumUntilNow += t.getDuration();
                }
            }else{
                sumTotal += t.getDurationSumUntil(t.getWiederholungsEnde(), c);
                sumUntilNow += t.getDurationSumUntil(new java.sql.Date(Calendar.getInstance().getTime().getTime()), c);
            }
        }
        return Helper.formatLong2HourString(sumUntilNow) + " / " + Helper.formatLong2HourString(sumTotal);
    }
}