package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class Role extends DataSensitiveEntity {

    private String title = null;

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public String toString(){
        return this.title;
    }

}
