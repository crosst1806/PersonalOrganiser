package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 25/12/17.
 */

public class ActivitySequenceList extends DataSensitiveEntity {

    private int role = 0;
    private int location = 0;
    private String description = "";

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

}
