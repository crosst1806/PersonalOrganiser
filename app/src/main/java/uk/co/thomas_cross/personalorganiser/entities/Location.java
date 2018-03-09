package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class Location extends Entity {

    public static final int NO_PLACE = 0;

    private  String title = null;
    private int locationCategory = 0;
    private int locationRecordNo = 0;

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

    public void setLocationRecordNo(int locationRecordNo){
        this.locationRecordNo = locationRecordNo;
    }

    public int getLocationRecordNo() {
        return this.locationRecordNo;
    }

    public String toString(){
        return  this.title;
    }

}
