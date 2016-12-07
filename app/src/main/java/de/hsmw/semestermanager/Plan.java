package de.hsmw.semestermanager;

import java.sql.Date;

/**
 * Created by Benjamin on 30.11.2016.
 */

public class Plan implements DatabaseObject{
    private int id;
    private String name;
    private Date startDate;
    private Date endDate;

    public Plan(int id, String name, String startDate, String endDate){
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

    public Date getDuration(){
        return new Date(endDate.getTime() - startDate.getTime());
    }
}
