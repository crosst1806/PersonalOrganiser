package uk.co.thomas_cross.personalorganiser.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.entities.UserId;

/**
 * Created by root on 26/12/17.
 */

public class POModel {

    private DBFrontEnd dbFrontEnd = null;
    private SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:sss");

    public POModel(Context context) {
        dbFrontEnd = DBFrontEnd.getInstance(context);
    }

    public String getUserName(int userId) {

        UserId userId1 = dbFrontEnd.getUserId(userId);
        if (userId1 == null) {
            return "Not Found";
        }
        return userId1.getUserName();
    }

    public String getUserTitle(int owner, int ownerType) {

        String title = "Owner Type Not Found";
        switch (ownerType) {
            case Ownable.NONE:
                title = "None";
                break;
            case Ownable.PERSON:
                Person p = dbFrontEnd.getPerson(owner);
                title = p.getFirstName() + " " + p.getLastName();
                break;
            case Ownable.GROUP:
                title = "Not Yet Implemented";
                break;
            case Ownable.TEAM:
                title = "Not yet Implemented";
                break;
            case Ownable.ORGANISATION:
                title = "Not Yet Implemented";
                break;
        }
        return title;
    }


    public String getOwnerType(int ownerType) {

        String title = "Owner Type Not Found";
        switch (ownerType) {
            case Ownable.NONE:
                title = "None";
                break;
            case Ownable.PERSON:
                title = "Person";
                break;
            case Ownable.GROUP:
                title = "Group";
                break;
            case Ownable.TEAM:
                title = "Team";
                break;
            case Ownable.ORGANISATION:
                title = "organisation";
                break;
        }
        return title;
    }


    public ArrayList<Person> getPersons() {
        return dbFrontEnd.getPersons();
    }


    public long addPerson(Person person) {
        return dbFrontEnd.addPerson(person);
    }

    public Person getPerson(int id) {
        return dbFrontEnd.getPerson(id);
    }

    public long updatePerson(Person person) {
        return dbFrontEnd.updatePerson(person);
    }

    public void deletePerson(int personID) {
        dbFrontEnd.deletePerson(personID);
    }

    public ArrayList<Role> getRoles() {

        ArrayList<Role> roles = dbFrontEnd.getRoles();

        Person p = dbFrontEnd.getPerson(1);
        Role personal = new Role();
        personal.setOwner(p.getDatabaseRecordNo());
        personal.setOwnerType(Ownable.PERSON);
        personal.setTitle(p.getFirstName() + " " + p.getLastName());
        personal.setTimeStamp(timeStampFormat.format(new Date()));
        personal.setLastModifiedBy(1);
        roles.add(personal);

        return roles;
    }


    public long addRole(Role role) {
        return dbFrontEnd.addRole(role);
    }

    public Role getRole(int id) {

        if (id == 0) {

            Person p = getPerson(1);
            Role personal = new Role();

            personal.setOwner(personal.getDatabaseRecordNo());
            personal.setOwnerType(Ownable.PERSON);
            personal.setTitle(p.getFirstName() + " " + p.getLastName());
            personal.setTimeStamp(timeStampFormat.format(new Date()));
            personal.setLastModifiedBy(1);
            return personal;

        } else {

            return dbFrontEnd.getRole(id);

        }


    }

    public long updateRole(Role role) {
        return dbFrontEnd.updateRole(role);
    }

    public void deleteRole(int roleID) {
        dbFrontEnd.deleteRole(roleID);
    }


    public UserId getUserId(int id) {
        return dbFrontEnd.getUserId(id);
    }

    public long addUserId(UserId userId) {
        return dbFrontEnd.addUserId(userId);
    }


    public long updateUserId(UserId userId) {
        return dbFrontEnd.updateUserId(userId);
    }

    public void deleteUserId(int userId) {
        dbFrontEnd.deleteUserId(userId);
    }


    public ArrayList<DataSensitivity> getDataSensitivitys() {

        ArrayList<DataSensitivity> dataSensitivities =
                dbFrontEnd.getDataSensitivitys();

        DataSensitivity privateDS = new DataSensitivity();
        privateDS.setOwner(1);
        privateDS.setOwnerType(Ownable.PERSON);
        privateDS.setTitle("Private");
        dataSensitivities.add(privateDS);

        return dataSensitivities;
    }


    public long addDataSensitivity(DataSensitivity dataSensitivity) {
        return dbFrontEnd.addDataSensitivity(dataSensitivity);
    }

    public DataSensitivity getDataSensitivity(int id) {

        DataSensitivity ds = null;

        if (id == 0) {

            DataSensitivity privateDS = new DataSensitivity();
            privateDS.setOwner(1);
            privateDS.setOwnerType(Ownable.PERSON);
            privateDS.setTitle("Private");
            ds = privateDS;

        } else {
            ds = dbFrontEnd.getDataSensitivity(id);
        }

        return ds;
    }

    public long updateDataSensitivity(DataSensitivity ds) {
        return dbFrontEnd.updateDataSensitivity(ds);
    }

    public void deleteDataSensitivity(int dsId) {
        dbFrontEnd.deleteDataSensitivity(dsId);
        ;
    }

    public ArrayList<Location> getLocations() {

        ArrayList<Location> locations = dbFrontEnd.getLocations();

        Location noLocation = new Location();
        noLocation.setOwner(1);
        noLocation.setOwnerType(Ownable.PERSON);
        noLocation.setTitle("None Specific");
        locations.add(noLocation);

        return locations;
    }


    public long addLocation(Location location) {
        return dbFrontEnd.addLocation(location);
    }

    public Location getLocation(int id) {

        Location location = null;
        if (id == 0) {

            Location noLocation = new Location();
            noLocation.setOwner(1);
            noLocation.setOwnerType(Ownable.PERSON);
            noLocation.setTitle("None Specific");
            location = noLocation;

        } else {
            location = dbFrontEnd.getLocation(id);
        }
        return location;
    }

    public long updateLocation(Location location) {
        return dbFrontEnd.updateLocation(location);
    }

    public void deleteLocation(int id) {
        dbFrontEnd.deleteLocation(id);
    }

    public ArrayList<ToDo> getToDos() {
        return dbFrontEnd.getToDos();
    }

    public long addToDo(ToDo toDo) {
        return dbFrontEnd.addToDo(toDo);
    }

    public ToDo getToDo(int id) {
        return dbFrontEnd.getToDo(id);
    }

    public long updateToDo(ToDo toDo) {
        return dbFrontEnd.updateToDo(toDo);
    }

    public void deleteToDo(int id) {
        dbFrontEnd.deleteToDo(id);
    }

    public ArrayList<Activity> getActivitys() {
        return dbFrontEnd.getActivitys();
    }

    public long addActivity(Activity activity) {
        return dbFrontEnd.addActivity(activity);
    }

    public Activity getActivity(int id) {
        return dbFrontEnd.getActivity(id);
    }

    public long updateActivity(Activity activity) {
        return dbFrontEnd.updateActivity(activity);
    }

    public void deleteActivity(int id) {
        dbFrontEnd.deleteActivity(id);
    }

    public ArrayList<DiaryEntry> getDiaryEntrys() {

        ArrayList<DiaryEntry> testData = new ArrayList<DiaryEntry>();

        DiaryEntry de1 = new DiaryEntry();
        de1.setDatabaseRecordNo(1);
        de1.setOwner(1);
        de1.setOwnerType(Ownable.PERSON);
        de1.setRole(0);
        de1.setLocation(0);
        de1.setDateTime("18-06-1962 21:30:25");
        String message =
                "This is the first ever diary entry. It has been generated " +
                        "by the po model class as a test example. Let us see" +
                        " what happens";
        de1.setTextEntry(message);
        de1.setDataSensitivity(0);
        de1.setTimeStamp(timeStampFormat.format(new Date()));
        de1.setLastModifiedBy(1);
        testData.add(de1);

        DiaryEntry de2 = new DiaryEntry();
        de2.setDatabaseRecordNo(2);
        de2.setOwner(1);
        de2.setOwnerType(Ownable.PERSON);
        de2.setRole(0);
        de2.setLocation(0);
        de2.setDateTime("18-06-1962 21:35:25");
        String message2 =
                "This is the second ever diary entry. It has been generated " +
                        "by the po model class as a test example. Let us see" +
                        " what happens";
        de2.setTextEntry(message2);
        de2.setDataSensitivity(0);
        de2.setTimeStamp(timeStampFormat.format(new Date()));
        de2.setLastModifiedBy(1);
        testData.add(de2);

        DiaryEntry de3 = new DiaryEntry();
        de3.setDatabaseRecordNo(1);
        de3.setOwner(1);
        de3.setOwnerType(Ownable.PERSON);
        de3.setRole(0);
        de3.setLocation(0);
        de3.setDateTime("18-06-1962 21:30:25");
        String message3 =
                "This is the third ever diary entry. It has been generated " +
                        "by the po model class as a test example. Let us see" +
                        " what happens";
        de3.setTextEntry(message3);
        de3.setDataSensitivity(0);
        de3.setTimeStamp(timeStampFormat.format(new Date()));
        de3.setLastModifiedBy(1);
        testData.add(de3);

        return testData;
//        return dbFrontEnd.getDiaryEntrys();
    }


    public long addDiaryEntry(DiaryEntry diaryEntry) {

        return dbFrontEnd.addDiaryEntry(diaryEntry);

    }

    public DiaryEntry getDiaryEntry(int id) {

        return dbFrontEnd.getDiaryEntry(id);

    }

    public long updateDiary(DiaryEntry diaryEntry) {

        return dbFrontEnd.updateDiary(diaryEntry);

    }

    public void deleteDiaryEntry(int id) {

        dbFrontEnd.deleteDiaryEntry(id);

    }

}
