package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class DataSensitivity extends Entity {

    public static  final int PRIVATE = 0;
    public static final int PUBLIC = 1;

    private  String title = null;

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public String toString(){
        return  this.title;
    }


}
