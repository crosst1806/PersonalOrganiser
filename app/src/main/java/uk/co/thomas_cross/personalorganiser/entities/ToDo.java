package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class ToDo extends DataSensitiveEntity {

    private int role = 0;
    private int location = 0;
    private String description = "";
    private int priority = 0;
    private String targetDateTime = "";

    public void setRole(int role){
        this.role = role;
    }

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

    public void setTargetDateTime(String targetDateTime){
        this.targetDateTime = targetDateTime;
    }

    public String getTargetDateTime(){
        return this.targetDateTime;
    }

    public String toString(){
        return this.description;
    }


}
