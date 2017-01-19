package de.hsmw.semestermanager;

import android.content.Context;
import android.content.Intent;

import java.sql.Date;

/**
 * Created by Benjamin on 30.11.2016.
 */

public class Plan implements DatabaseObject {
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;

    public Plan(int id, String name, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.startDate = Date.valueOf(startDate);
        this.endDate = Date.valueOf(endDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getId() {
        return id;
    }

    public Date getDuration() {
        return new Date(endDate.getTime() - startDate.getTime());
    }

    /**
     * Löscht das Semester mitsamt allen zugewiesenen Modulen und Terminen in der Datenbank.
     */
    public void delete(Context c) {
        DatabaseInterface di = DatabaseInterface.getInstance(c);
        di.deletePlanByID(id);
        Termin[] termine = di.getTermineByPlanID(id);
        if (termine != null) {
            for (Termin t : termine) {
                t.delete(c);
            }
        }
        Module[] module = di.getModulesByPlanID(id);
        if (module != null){
            for (Module m : module) {
                m.delete(c);
            }
        }
    }

    @Override
    public void edit(Context c) {
        Intent i = new Intent("de.hsmw.semestermanager.SemesterplanInput");
        i.putExtra("ID", getId());
        c.startActivity(i);
    }
}
