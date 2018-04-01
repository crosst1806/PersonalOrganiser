package uk.co.thomas_cross.personalorganiser.entities;

import java.io.Serializable;

/**
 * Created by root on 24/12/17.
 */

public class Location extends Entity implements Serializable {

    public static final int NO_PLACE = 0;

    private  String title = null;
    private int locationCategory = 0;
    private int categoryRecordNo = 0;

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setLocationCategory(int locationCategory){
        this.locationCategory = locationCategory;
    }

    public int getLocationCategory(){
        return  this.locationCategory;
    }

    public void setCategoryRecordNo(int categoryRecordNo){
        this.categoryRecordNo = categoryRecordNo;
    }

    public int getCategoryRecordNo() {
        return this.categoryRecordNo;
    }

    public String toString(){
        return  this.title;
    }

}
