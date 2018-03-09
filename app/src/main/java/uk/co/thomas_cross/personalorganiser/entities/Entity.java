package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class Entity implements DatabaseRecordable, Ownable {

    private int _id = 0;
    private int owner = 0;
    private int ownerType = 0;

    @Override
    public void setDatabaseRecordNo(int _id) {
        this._id = _id;
    }

    @Override
    public int getDatabaseRecordNo() {
        return this._id;
    }

    @Override
    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public int getOwner() {
        return this.owner;
    }

    @Override
    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public int getOwnerType() {
        return this.ownerType;
    }
}
