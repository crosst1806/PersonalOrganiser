package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class DataSensitiveEntity extends Entity implements DataSensitive {

    private int dataSensitivity = 0;
    private String timeStamp = null;
    private int lastModifiedBy = 0;

    @Override
    public void setDataSensitivity(int dataSensitivity) {
        this.dataSensitivity = dataSensitivity;
    }

    @Override
    public int getDataSensitivity() {
        return this.dataSensitivity;
    }

    @Override
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String getTimeStamp() {
        return this.timeStamp;
    }

    @Override
    public void setLastModifiedBy(int userId) {
        this.lastModifiedBy = userId;
    }

    @Override
    public int getLastModifiedBy() {
        return this.lastModifiedBy;
    }
}
