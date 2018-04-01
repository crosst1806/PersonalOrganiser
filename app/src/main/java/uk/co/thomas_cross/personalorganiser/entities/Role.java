package uk.co.thomas_cross.personalorganiser.entities;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by root on 24/12/17.
 */

public class Role extends DataSensitiveEntity implements Serializable {

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
