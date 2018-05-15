package uk.co.thomas_cross.personalorganiser.entities;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * Created by root on 25/12/17.
 */

public class ActivityIteration extends DataSensitiveEntity implements Serializable {

    public static final int DISABLED = 0;
    public static final int ENABLED = 1;

    public static final int MINUTES = 0;
    public static final int HOURS = 1;
    public static final int DAYS = 2;
    public static final int WEEKS = 3;
    public static final int MONTHS = 4;
    public static final int YEARS = 5;

    private int role = 0;
    private int location = 0;
    private int activity = 0;
    private int priority = 0;
    private int frequencyInterval = 0;
    private int frequency = 0;
    private String exemptedDays = "";
    private String startDate = "";
    private String endDate = "";
    private String startTime = "";
    private String endTime = "";
    private int status = 0;

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

    public void setActivity(int activity){ this.activity = activity; }

    public int getActivity(){ return this.activity; }

    public void setPriority(int priority){ this.priority = priority;}

    public int getPriority(){ return  this.priority; }

    public void setFrequencyInterval(int frequencyInterval){
        switch (frequencyInterval){
            case MINUTES:
            case HOURS:
            case DAYS:
            case WEEKS:
            case MONTHS:
            case YEARS:
                this.frequencyInterval = frequencyInterval;
                break;
        }
    }

    public int getFrequencyInterval(){ return  this.frequencyInterval; }

    public void setFrequency(int frequency){ this.frequency = frequency; }

    public int getFrequency(){ return this.frequency; }

    public void  setExemptedDays(String exemptedDays){
        this.exemptedDays = exemptedDays;
    }

    public String getExemptedDays(){ return  this.exemptedDays; }

    public  void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public String getStartDate(){ return this.startDate; }

    public void setEndDate(String endDate){ this.endDate = endDate;}

    public String getEndDate(){ return this.endDate; }

    public void setStartTime(String startTime){ this.startTime = startTime; }

    public String getStartTime(){ return  this.startTime; }

    public void setEndTime(String endTime){ this.endTime = endTime; }

    public String getEndTime(){ return  this.endTime; }

    public void setStatus(int status){
        switch (status){
            case ENABLED:
            case DISABLED:
                this.status = status;
                break;
        }
    }

    public int getStatus(){ return  this.status; }

    public String toString(){
        return String.valueOf(this.getDatabaseRecordNo()
         + this.role + this.location);
    }

}
