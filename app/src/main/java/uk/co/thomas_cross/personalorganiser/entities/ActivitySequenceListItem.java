package uk.co.thomas_cross.personalorganiser.entities;

import java.io.Serializable;

/**
 * Created by root on 25/12/17.
 */

public class ActivitySequenceListItem implements DatabaseRecordable, Serializable{

    private int _id = 0;
    private int activitySequenceList = 0;
    private int activity = 0;
    private int executionOrder = 0;
    private int duration = 0;


    public ActivitySequenceListItem(){
        super();
    }

    public ActivitySequenceListItem(int activitySequenceList){
        super();
        setActivitySequenceList(activitySequenceList);
    }

    @Override
    public void setDatabaseRecordNo(int _id) {
        this._id = _id;
    }

    @Override
    public int getDatabaseRecordNo() {
        return _id;
    }

    public void setActivitySequenceList(int activitySequenceList){
        this.activitySequenceList = activitySequenceList;
    }

    public int getActivitySequenceList(){ return this.activitySequenceList; }

    public void setActivity(int activity){ this.activity = activity; }

    public int getActivity(){ return this.activity; }

    public void setExecutionOrder(int executionOrder){
        this.executionOrder = executionOrder;
    }

    public int getExecutionOrder(){ return this.executionOrder;}

    public void setDuration(int duration){ this.duration = duration; }

    public int getDuration(){
        return duration;
    }

    public String toString(){
        return this._id+","+activitySequenceList+","+activity+","+executionOrder+","+duration;
    }
}
