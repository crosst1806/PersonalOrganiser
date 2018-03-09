package uk.co.thomas_cross.personalorganiser.entities;


/**
 * Created by root on 05/01/18.
 */

public class GroundHogActivity implements DatabaseRecordable {

    private int _id = 0;
    private String role;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    private int duration;

    @Override
    public void setDatabaseRecordNo(int _id) {
        this._id = _id;
    }

    @Override
    public int getDatabaseRecordNo() {
        return this._id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
