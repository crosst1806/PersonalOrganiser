package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public interface DataSensitive {

    public void setDataSensitivity(int dataSensitivity);
    public int getDataSensitivity();

    public void setTimeStamp(String timeStamp);
    public String getTimeStamp();

    public void setLastModifiedBy(int userId);
    public int getLastModifiedBy();

}
