package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class Activity extends DataSensitiveEntity {

    private int role = 0;
    private int location = 0;
    private String description = "";
    private int priority = 0;
    private int lowestMinutes = 0;
    private int highestMinutes = 0;
    private int medianMinutes = 0;

    public void setRole(int role){ this.role = role; }

    public int getRole(){
        return  this.role;
    }

    public void setLocation(int location){
        this.location = location;
    }

    public int getLocation(){
        return this.location;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setLowestMinutes(int lowestMinutes){
        this.lowestMinutes = lowestMinutes;
    }

    public int getLowestMinutes(){
        return this.lowestMinutes;
    }

    public void setHighestMinutes(int highestMinutes){
        this.highestMinutes = highestMinutes;
    }

    public int getHighestMinutes(){
        return this.highestMinutes;
    }
    public void setMedianMinutes(int medianMinutes){
        this.medianMinutes = medianMinutes;
    }

    public int getMedianMinutes(){
        return this.medianMinutes;
    }




}
