package de.hsmw.semestermanager;

/**
 * Created by Benjamin on 30.11.2016.
 */

public class Semester {
    private int id;
    private String name;
    private String startDate;
    private String endDate;

    public Semester(int id, String name, String startDate, String endDate){
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getId() {
        return id;
    }
}
