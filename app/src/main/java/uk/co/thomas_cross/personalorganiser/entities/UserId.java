package uk.co.thomas_cross.personalorganiser.entities;

/**
 * Created by root on 24/12/17.
 */

public class UserId extends DataSensitiveEntity {

    private String userName = null;
    private String password = null;

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

    public String toString(){
        return this.userName + " " + this.getPassword();
    }
}
