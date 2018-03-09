package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class Person extends DataSensitiveEntity {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private String firstName = null;
    private String middleNames = null;
    private String lastName = null;
    private int gender = 0;
    private int userId = 0;

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

    private void setUserId(int userId){
        this.userId = userId;
    }

    public int getUserId(){
        return this.userId;
    }


    public String toString(){
        return  this.lastName+", "+this.firstName;
    }

}
