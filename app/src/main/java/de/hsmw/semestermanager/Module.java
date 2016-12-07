package de.hsmw.semestermanager;

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
}
