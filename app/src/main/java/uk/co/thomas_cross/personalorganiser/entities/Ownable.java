package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public interface Ownable {

    public static final int NONE = 0;
    public static final int PERSON = 1;
    public static final int TEAM = 2;
    public static final int GROUP = 3;
    public static final int ORGANISATION = 4;

    public void setOwner(int owner);
    public int getOwner();

    public void setOwnerType(int ownerType);
    public int getOwnerType();

}
