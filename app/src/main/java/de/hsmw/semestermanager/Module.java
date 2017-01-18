package de.hsmw.semestermanager;

import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Benjamin on 02.12.2016.
 */

public class Module implements DatabaseObject {
    private int id;
    private String name;
    private int planID;

    public Module(int id, String name, int planID) {
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

    /**
     * Erzeugt einen Fortschrittsstring.
     *
     * @param c Context zur Datenbankabfrage
     * @return Einen String, der den Fortschritt in dem Modul anzeigt im Format " Vergangene Stunden / Gesamtstunden "
     */
    public String getProgressString(Context c) {
        DatabaseInterface di = DatabaseInterface.getInstance(c);

        long sumUntilNow = 0;
        long sumTotal = 0;
        Termin[] termine = di.getTermineByModulID(id);
        if (termine == null) {
            return "0:00 / 0:00";
        }
        for (Termin t : termine) {
            if (t.getPeriode() == 0) {
                sumTotal += t.getDuration();
                if (Calendar.getInstance().getTime().compareTo(t.getStartDate()) >= 0) {
                    sumUntilNow += t.getDuration();
                }
            } else if (t.getIsException() == 0) {
                sumTotal += t.getDurationSumUntil(t.getWiederholungsEnde(), c);
                sumUntilNow += t.getDurationSumUntil(new java.sql.Date(Calendar.getInstance().getTime().getTime()), c);
            }
        }
        return Helper.formatLong2HourString(sumUntilNow) + " / " + Helper.formatLong2HourString(sumTotal);
    }

    public void delete(Context c) {
        DatabaseInterface di = DatabaseInterface.getInstance(c);
        di.deleteModulByID(id);
        Termin[] termine = di.getTermineByModulID(id);
        if (termine != null) {
            for (Termin t : termine) {
                t.delete(c);
            }
        }
    }

    @Override
    public void edit(Context c) {
        Intent i = new Intent("de.hsmw.semestermanager.ModulInput");
        i.putExtra("ID", getId());
        c.startActivity(i);
    }
}