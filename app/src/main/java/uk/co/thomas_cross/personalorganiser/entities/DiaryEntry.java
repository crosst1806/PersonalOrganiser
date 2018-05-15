package uk.co.thomas_cross.personalorganiser.entities;

import java.io.Serializable;

import uk.co.thomas_cross.personalorganiser.LocationAssociated;

/**
 * Created by root on 24/12/17.
 */

public class DiaryEntry extends DataSensitiveEntity
                        implements RoleAssociated, LocationAssociated, Serializable {

    private int role = 0;
    private int location = 0;
    private String dateTime = "";
    private String textEntry = "";

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

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    public String getDateTime(){
        return this.dateTime;
    }

    public void setTextEntry(String textEntry){
        this.textEntry = textEntry;
    }

    public String getTextEntry(){
        return this.textEntry;
    }

    public String toString(){
        return this.textEntry.substring(0,20);
    }

}
