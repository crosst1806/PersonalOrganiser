package uk.co.thomas_cross.personalorganiser.entities;

import java.io.Serializable;

import uk.co.thomas_cross.personalorganiser.LocationAssociated;

/**
 * Created by root on 24/12/17.
 */

public class PlannedActivity extends DataSensitiveEntity implements RoleAssociated,
        LocationAssociated,
        Serializable {

    public static final int PENDING = 0;
    public static final int EXECUTING = 1;
    public static final int PAUSED = 2;
    public static final int COMPLETED = 3;
    public static final int NOT_NECESSARY = 4;
    public static final int LACK_OF_TIME = 5;
    public static final int ADVERSE_WEATHER = 6;
    public static final int NO_ACCESS = 7;
    public static final int MACHINE_PROBLEM = 8;


    public static final int SINGLE_INSTANCE = 0;
    public static final int ACTIVITY_ITERATION_CHILD = 1;
    public static final int SEQUENCE_ITERATION_CHILD = 2;

    private int role = 0;
    private int location = 0;
    private int activity = 0;
    private String code = "";
    private String description = "";
    private int medianMinutes = 0;
    private int priority = 0;
    private int generatorType = 0;
    private int generatorId = 0;
    private String startDate = "";
    private String startTime = "";
    private String endDateTime = "";
    private int timeTaken = 0;
    private int status = 0;

    public void setRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return this.role;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getLocation() {
        return this.location;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public int getActivity() {
        return this.activity;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setMedianMinutes(int medianMinutes) {
        this.medianMinutes = medianMinutes;
    }

    public int getMedianMinutes() {
        return this.medianMinutes;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setGeneratorType(int generatorType) {
        switch (generatorType) {
            case SINGLE_INSTANCE:
            case ACTIVITY_ITERATION_CHILD:
            case SEQUENCE_ITERATION_CHILD:
                this.generatorType = generatorType;
                break;
            default:
                break;
        }
    }

    public int getGeneratorType() {
        return this.generatorType;
    }

    public void setGeneratorId(int generatorId) {
        this.generatorId = generatorId;
    }

    public int getGeneratorId() {
        return this.generatorId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getEndDateTime() {
        return this.endDateTime;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public int getTimeTaken() {
        return this.timeTaken;
    }

    public void setStatus(int status) {
        switch (status) {
            case PENDING:
            case EXECUTING:
            case PAUSED:
            case COMPLETED:
            case NOT_NECESSARY:
            case NO_ACCESS:
            case LACK_OF_TIME:
            case ADVERSE_WEATHER:
            case MACHINE_PROBLEM:
                this.status = status;
                break;
        }
    }

    public int getStatus() {
        return this.status;
    }

    public String toString() {
        return description;
    }

}
