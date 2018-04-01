package uk.co.thomas_cross.personalorganiser.entities;

import java.io.Serializable;

/**
 * Created by root on 24/12/17.
 */

public class DataSensitivity extends Entity implements Serializable {

    public static  final int PRIVATE = 0;
    public static final int PUBLIC = 1;

    private  String title = new String("");

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
