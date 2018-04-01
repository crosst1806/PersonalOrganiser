package uk.co.thomas_cross.personalorganiser.entities;

import android.widget.EditText;

/**
 * Created by root on 24/12/17.
 */

public class Person extends DataSensitiveEntity {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private String firstName = new String("");
    private String middleNames = new String("");
    private String lastName = new String("");
    private int gender = 0;
    private long userId = 0;

    public Person(){
        super();
    }

    public Person(String firstName, String lastName){
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public String getFirstName(){
        return this.firstName;
    }

    public void setMiddleNames(String middleNames){
        this.middleNames = middleNames;
    }
    public String getMiddleNames(){
        return this.middleNames;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public String getLastName(){
        return this.lastName;
    }

    public void setGender(int gender){
        this.gender = gender;
    }

    public int getGender(){
        return this.gender;
    }

    public void setUserId(long userId){
        this.userId = userId;
    }

    public long getUserId(){
        return this.userId;
    }


    public String toString(){
        return  this.lastName+", "+this.firstName;
    }

}
