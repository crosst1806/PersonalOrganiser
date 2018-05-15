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
import uk.co.thomas_cross.personalorganiser.entities.ActivityIteration;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceListItem;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.UserId;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by root on 26/12/17.
 */

public class DBFrontEnd extends SQLiteOpenHelper {

    private static final String TAG = "PersonalOrganiser";

    private static DBFrontEnd sInstance;

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "/co.uk.thomascross/personalOrganiser.db";
    private Context context;

    private SimpleDateFormat timeStampDateFormat =
            UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_TimeStamp_Storage);

    private SimpleDateFormat paTimeStampDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");


    public static synchronized DBFrontEnd getInstance(Context context) {
        // Use the application context, which will ensure you dont
        // accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DBFrontEnd(context.getApplicationContext());
        }
        return sInstance;
    }


    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DBFrontEnd(Context context) {
        super(context,
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createRoles = "Create table roles (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " title text not null, dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";

        String createDataSensitivitys = "Create table dataSensitivitys (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer, title text not null );";

        // Create the persons table

        String createPersons = "Create table persons (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " firstName text not null, middleNames text not null," +
                " lastName text not null, gender integer, userId integer," +
                " dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";

        // Create the userIds table

        String createUserIds = "Create table userIds (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " userName text not null, password text not null," +
                " dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";

        // Create the locations table
        String createLocations = "Create table locations (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer, title text not null," +
                " locationCategory integer, categoryRecordNo integer);";

        String createToDos = "Create table toDos (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " role integer, location integer," +
                " description text not null, priority integer, targetDate text not null," +
                " dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";

        String createActivitys = "Create table activitys (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " role integer, location integer," +
                " description text not null, priority integer," +
                " lowestMinutes integer, highestMinutes integer, medianMinutes integer," +
                " dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";

        String createDiary = "Create table diary (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " role integer, location integer," +
                " dateTime text not null, textEntry text not null, " +
                " dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";

        String createPlannedActivitys = "Create table plannedActivitys (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " role integer, location integer," +
                " activity integer, code text not null, description text not null," +
                " medianMinutes integer, priority integer," +
                " generatorType integer, generatorId integer, " +
                " startDate text not null, startTime text not null, " +
                " endDateTime text not null, timeTaken integer, " +
                " status integer, dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";


        db.execSQL(createPersons);
        db.execSQL(createDataSensitivitys);
        db.execSQL(createRoles);
        db.execSQL(createUserIds);
        db.execSQL(createLocations);
        db.execSQL(createToDos);
        db.execSQL(createActivitys);
        db.execSQL(createDiary);
        db.execSQL(createPlannedActivitys);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {
            case 1:
                //upgrade logic from version 1 to 2
                String createActivityIterations = "Create table activityIterations (" +
                        " _id integer primary key autoincrement," +
                        " owner integer, ownerType integer," +
                        " role integer, location integer," +
                        " activity integer, priority integer, frequencyInterval integer," +
                        " frequency integer, exemptedDays text not null, " +
                        " startDate text not null, endDate text not null, " +
                        " startTime text not null, endTime text not null, " +
                        " status integer, dataSensitivity integer, " +
                        " timeStamp text not null, lastModifiedBy integer)";
                db.execSQL(createActivityIterations);
            case 2:
                //upgrade logic from version 2 to 3
                String createActivitySequenceLists = "Create table activitySequenceLists (" +
                        " _id integer primary key autoincrement," +
                        " owner integer, ownerType integer," +
                        " role integer, location integer," +
                        " description text not null, dataSensitivity integer, " +
                        " timeStamp text not null, lastModifiedBy integer)";
                db.execSQL(createActivitySequenceLists);
            case 3:
                //upgrade logic from version 3 to 4
                String createActivitySequenceListItems = "Create table activitySequenceListItems (" +
                        " _id integer primary key autoincrement," +
                        " activitySequenceList integer, activity integer," +
                        " executionOrder integer, duration integer )";
                db.execSQL(createActivitySequenceListItems);
                break;
            case 4:
                // upgrade logic from version 4 to 5
                String dropTable = "Drop table activitySequenceListItems";
                db.execSQL(dropTable);
                //upgrade logic from version 2 to 3
                String createActivitySequenceListItems2 = "Create table activitySequenceListItems (" +
                        " _id integer primary key autoincrement," +
                        " activitySequenceList integer, activity integer," +
                        " executionOrder integer, duration integer )";
                db.execSQL(createActivitySequenceListItems2);
                break;
            case 5:
                //upgrade logic from version 5 to 6
                String dropTable2 = "Drop table activitySequenceListItems";
                db.execSQL(dropTable2);
                //upgrade logic from version 2 to 3
                String createActivitySequenceListItems3 = "Create table activitySequenceListItems (" +
                        " _id integer primary key autoincrement," +
                        " activitySequenceList integer, activity integer," +
                        " executionOrder integer, duration integer )";
                db.execSQL(createActivitySequenceListItems3);
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown oldVersion " + oldVersion);
        }
    }

    public String getUserName(int userId) {

        UserId userId1 = getUserId(userId);
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
                Person p = getPerson(owner);
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

        ArrayList<Person> people = new ArrayList<Person>();

        String sql = "select * from persons";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    Person person = new Person();
                    person.setDatabaseRecordNo(cursor.getInt(0));
                    person.setOwner(cursor.getInt(1));
                    person.setOwnerType(cursor.getInt(2));
                    person.setFirstName(cursor.getString(3));
                    person.setMiddleNames(cursor.getString(4));
                    person.setLastName(cursor.getString(5));
                    person.setGender(cursor.getInt(6));
                    person.setUserId(cursor.getInt(7));
                    person.setDataSensitivity(cursor.getInt(8));
                    person.setTimeStamp(cursor.getString(9));
                    person.setLastModifiedBy(cursor.getInt(10));
                    people.add(person);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error while fetching roles from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return people;
    }


    public long addPerson(Person person) {

        ContentValues values = new ContentValues();
        values.put("owner", person.getOwner());
        values.put("ownerType", person.getOwnerType());
        values.put("firstName", person.getFirstName());
        values.put("middleNames", person.getMiddleNames());
        values.put("lastName", person.getLastName());
        values.put("gender", person.getGender());
        values.put("userId", person.getUserId());
        values.put("dataSensitivity", person.getDataSensitivity());
        person.setTimeStamp(timeStampDateFormat.format(new Date()));
        values.put("timeStamp", person.getTimeStamp());
        values.put("lastModifiedBy", person.getLastModifiedBy());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("persons", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + person + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public Person getPerson(int id) {

        String q = "Select * from persons where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        Person person = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                person = new Person();
                person.setDatabaseRecordNo(cursor.getInt(0));
                person.setOwner(cursor.getInt(1));
                person.setOwnerType(cursor.getInt(2));
                person.setFirstName(cursor.getString(3));
                person.setMiddleNames(cursor.getString(4));
                person.setLastName(cursor.getString(5));
                person.setGender(cursor.getInt(6));
                person.setUserId(cursor.getInt(7));
                person.setDataSensitivity(cursor.getInt(8));
                person.setTimeStamp(cursor.getString(9));
                person.setLastModifiedBy(cursor.getInt(10));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching a person");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return person;

    }

    public long updatePerson(Person person) {

        ContentValues values = new ContentValues();
        values.put("owner", person.getOwner());
        values.put("ownerType", person.getOwnerType());
        values.put("firstName", person.getFirstName());
        values.put("middleNames", person.getMiddleNames());
        values.put("lastName", person.getLastName());
        values.put("gender", person.getGender());
        values.put("userId", person.getUserId());
        values.put("dataSensitivity", person.getDataSensitivity());
        person.setTimeStamp(timeStampDateFormat.format(new Date()));
        values.put("timeStamp", person.getTimeStamp());
        values.put("lastModifiedBy", person.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("persons", values, "_id = ?",
                new String[]{String.valueOf(person.getDatabaseRecordNo())});
    }

    public void deletePerson(int personID) {

        Person deletedPerson = getPerson(personID);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("persons", "_id = ?",
                    new String[]{String.valueOf(deletedPerson.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting person");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Role> getRoles() {

        ArrayList<Role> roles = new ArrayList<Role>();

        String sql = "select * from roles";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        try {

            if (cursor.moveToFirst()) {

                do {

                    Role role = new Role();
                    role.setDatabaseRecordNo(cursor.getInt(0));
                    role.setOwner(cursor.getInt(1));
                    role.setOwnerType(cursor.getInt(2));
                    role.setTitle(cursor.getString(3));
                    role.setDataSensitivity(cursor.getInt(4));
                    role.setTimeStamp(cursor.getString(5));
                    role.setLastModifiedBy(cursor.getInt(6));
                    roles.add(role);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching roles from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return roles;
    }


    public long addRole(Role role) {

        ContentValues values = new ContentValues();
        values.put("owner", role.getOwner());
        values.put("ownerType", role.getOwnerType());
        values.put("title", role.getTitle());
        values.put("dataSensitivity", role.getDataSensitivity());
        role.setTimeStamp(timeStampDateFormat.format(new Date()));
        values.put("timeStamp", role.getTimeStamp());
        values.put("lastModifiedBy", role.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long newId = 0;
        try {
            newId = db.insertOrThrow("roles", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying add " + role + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public Role getRole(int id) {


        String q = "Select * from roles where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);


        Role role = null;

        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                role = new Role();
                role.setDatabaseRecordNo(cursor.getInt(0));
                role.setOwner(cursor.getInt(1));
                role.setOwnerType(cursor.getInt(2));
                role.setTitle(cursor.getString(3));
                role.setDataSensitivity(cursor.getInt(4));
                role.setTimeStamp(cursor.getString(5));
                role.setLastModifiedBy(cursor.getInt(6));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error trying to fetch role ");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return role;
    }

    public long updateRole(Role role) {

        ContentValues values = new ContentValues();
        values.put("owner", role.getOwner());
        values.put("ownerType", role.getOwnerType());
        values.put("title", role.getTitle());
        values.put("dataSensitivity", role.getDataSensitivity());
        role.setTimeStamp(timeStampDateFormat.format(new Date()));
        values.put("timeStamp", role.getTimeStamp());
        values.put("lastModifiedBy", role.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("roles", values, "_id = ?",
                new String[]{String.valueOf(role.getDatabaseRecordNo())});
    }

    public void deleteRole(int roleID) {

        Role deletedRole = getRole(roleID);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("roles", "_id = ?",
                    new String[]{String.valueOf(deletedRole.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to delete role");
        } finally {
            db.endTransaction();
        }
    }


    public UserId getUserId(int id) {

        String q = "Select * from userIds where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        UserId userId = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                userId = new UserId();
                userId.setDatabaseRecordNo(cursor.getInt(0));
                userId.setOwner(cursor.getInt(1));
                userId.setOwnerType(cursor.getInt(2));
                userId.setUserName(cursor.getString(3));
                userId.setPassword(cursor.getString(4));
                userId.setDataSensitivity(cursor.getInt(5));
                userId.setTimeStamp(cursor.getString(6));
                userId.setLastModifiedBy(cursor.getInt(7));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error trying to fetch userId from model " + e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return userId;

    }

    public long addUserId(UserId userId) {

        long newId = -1;
        ContentValues values = new ContentValues();
        values.put("owner", userId.getOwner());
        values.put("ownerType", userId.getOwnerType());
        values.put("username", userId.getUserName());
        values.put("password", userId.getPassword());
        values.put("dataSensitivity", userId.getDataSensitivity());
        userId.setTimeStamp(timeStampDateFormat.format(new Date()));
        values.put("timeStamp", userId.getTimeStamp());
        values.put("lastModifiedBy", userId.getLastModifiedBy());
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("userIds", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + userId + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }


    public long updateUserId(UserId userId) {

        ContentValues values = new ContentValues();
        values.put("owner", userId.getOwner());
        values.put("ownerType", userId.getOwnerType());
        values.put("userName", userId.getUserName());
        values.put("password", userId.getPassword());
        values.put("dataSensitivity", userId.getDataSensitivity());
        userId.setTimeStamp(timeStampDateFormat.format(new Date()));
        values.put("timeStamp", userId.getTimeStamp());
        values.put("lastModifiedBy", userId.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("userIds", values, "_id = ?",
                new String[]{String.valueOf(userId.getDatabaseRecordNo())});
    }

    public void deleteUserId(int userId) {

        UserId deletedUserId = getUserId(userId);

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("userIds", "_id = ?",
                    new String[]{String.valueOf(deletedUserId.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to delete user id");
        } finally {
            db.endTransaction();
        }
    }


    public ArrayList<DataSensitivity> getDataSensitivitys() {

        ArrayList<DataSensitivity> dataSensitivities = new ArrayList<DataSensitivity>();

        String sql = "select * from dataSensitivitys";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    DataSensitivity ds = new DataSensitivity();
                    ds.setDatabaseRecordNo(cursor.getInt(0));
                    ds.setOwner(cursor.getInt(1));
                    ds.setOwnerType(cursor.getInt(2));
                    ds.setTitle(cursor.getString(3));
                    dataSensitivities.add(ds);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error while fetching data sensitivities from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return dataSensitivities;
    }


    public long addDataSensitivity(DataSensitivity dataSensitivity) {

        ContentValues values = new ContentValues();
        values.put("owner", dataSensitivity.getOwner());
        values.put("ownerType", dataSensitivity.getOwnerType());
        values.put("title", dataSensitivity.getTitle());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("dataSensitivitys", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + dataSensitivity + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public DataSensitivity getDataSensitivity(int id) {

        String q = "Select * from dataSensitivitys where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        DataSensitivity ds = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                ds = new DataSensitivity();
                ds.setDatabaseRecordNo(cursor.getInt(0));
                ds.setOwner(cursor.getInt(1));
                ds.setOwnerType(cursor.getInt(2));
                ds.setTitle(cursor.getString(3));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching a data sensitivity");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return ds;

    }

    public long updateDataSensitivity(DataSensitivity ds) {

        ContentValues values = new ContentValues();
        values.put("owner", ds.getOwner());
        values.put("ownerType", ds.getOwnerType());
        values.put("title", ds.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("dataSensitivitys", values, "_id = ?",
                new String[]{String.valueOf(ds.getDatabaseRecordNo())});
    }

    public void deleteDataSensitivity(int dsId) {

        DataSensitivity ds = getDataSensitivity(dsId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("dataSensitivitys", "_id = ?",
                    new String[]{String.valueOf(ds.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting data sensitivity");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Location> getLocations() {

        ArrayList<Location> locations = new ArrayList<Location>();

        String sql = "select * from locations";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    Location location = new Location();
                    location.setDatabaseRecordNo(cursor.getInt(0));
                    location.setOwner(cursor.getInt(1));
                    location.setOwnerType(cursor.getInt(2));
                    location.setTitle(cursor.getString(3));
                    location.setLocationCategory(cursor.getInt(4));
                    location.setCategoryRecordNo(cursor.getInt(5));
                    locations.add(location);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching locations from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return locations;
    }


    public long addLocation(Location location) {

        ContentValues values = new ContentValues();
        values.put("owner", location.getOwner());
        values.put("ownerType", location.getOwnerType());
        values.put("title", location.getTitle());
        values.put("locationCategory", location.getLocationCategory());
        values.put("categoryRecordNo", location.getCategoryRecordNo());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("locations", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + location + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public Location getLocation(int id) {

        if (id == 0) {

            Location noLocation = new Location();
            noLocation.setOwner(1);
            noLocation.setOwnerType(Ownable.PERSON);
            noLocation.setTitle("No Specific Location");
            return noLocation;

        }

        String q = "Select * from locations where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        Location location = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                location = new Location();
                location.setDatabaseRecordNo(cursor.getInt(0));
                location.setOwner(cursor.getInt(1));
                location.setOwnerType(cursor.getInt(2));
                location.setTitle(cursor.getString(3));
                location.setLocationCategory(cursor.getInt(4));
                location.setCategoryRecordNo(cursor.getInt(5));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching a location");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return location;

    }

    public long updateLocation(Location location) {

        ContentValues values = new ContentValues();
        values.put("owner", location.getOwner());
        values.put("ownerType", location.getOwnerType());
        values.put("title", location.getTitle());
        values.put("locationCategory", location.getLocationCategory());
        values.put("categoryRecordNo", location.getCategoryRecordNo());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("locations", values, "_id = ?",
                new String[]{String.valueOf(location.getDatabaseRecordNo())});
    }

    public void deleteLocation(int id) {

        Location location = getLocation(id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("locations", "_id = ?",
                    new String[]{String.valueOf(location.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting location");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ToDo> getToDos() {

        ArrayList<ToDo> toDos = new ArrayList<ToDo>();

        String sql = "select * from toDos";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    ToDo toDo = new ToDo();
                    toDo.setDatabaseRecordNo(cursor.getInt(0));
                    toDo.setOwner(cursor.getInt(1));
                    toDo.setOwnerType(cursor.getInt(2));
                    toDo.setRole(cursor.getInt(3));
                    toDo.setLocation(cursor.getInt(4));
                    toDo.setDescription(cursor.getString(5));
                    toDo.setPriority(cursor.getInt(6));
                    toDo.setTargetDate(cursor.getString(7));
                    toDo.setDataSensitivity(cursor.getInt(8));
                    toDo.setTimeStamp(cursor.getString(9));
                    toDo.setLastModifiedBy(cursor.getInt(10));
                    toDos.add(toDo);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching toDos from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return toDos;
    }


    public long addToDo(ToDo toDo) {

        ContentValues values = new ContentValues();
        values.put("owner", toDo.getOwner());
        values.put("ownerType", toDo.getOwnerType());
        values.put("role", toDo.getRole());
        values.put("location", toDo.getLocation());
        values.put("description", toDo.getDescription());
        values.put("priority", toDo.getPriority());
        values.put("targetDate", toDo.getTargetDate());
        values.put("dataSensitivity", toDo.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", toDo.getLastModifiedBy());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("toDos", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + toDo + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public ToDo getToDo(int id) {

        String q = "Select * from toDos where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        ToDo toDo = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                toDo = new ToDo();
                toDo.setDatabaseRecordNo(cursor.getInt(0));
                toDo.setOwner(cursor.getInt(1));
                toDo.setOwnerType(cursor.getInt(2));
                toDo.setRole(cursor.getInt(3));
                toDo.setLocation(cursor.getInt(4));
                toDo.setDescription(cursor.getString(5));
                toDo.setPriority(cursor.getInt(6));
                toDo.setTargetDate(cursor.getString(7));
                toDo.setDataSensitivity(cursor.getInt(8));
                toDo.setTimeStamp(cursor.getString(9));
                toDo.setLastModifiedBy(cursor.getInt(10));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching a toDo");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return toDo;

    }

    public long updateToDo(ToDo toDo) {

        ContentValues values = new ContentValues();
        values.put("owner", toDo.getOwner());
        values.put("ownerType", toDo.getOwnerType());
        values.put("role", toDo.getRole());
        values.put("location", toDo.getLocation());
        values.put("description", toDo.getDescription());
        values.put("priority", toDo.getPriority());
        values.put("targetDate", toDo.getTargetDate());
        values.put("dataSensitivity", toDo.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", toDo.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("toDos", values, "_id = ?",
                new String[]{String.valueOf(toDo.getDatabaseRecordNo())});
    }

    public void deleteToDo(int id) {

        ToDo toDo = getToDo(id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("toDos", "_id = ?",
                    new String[]{String.valueOf(toDo.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting toDo");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Activity> getActivitys() {

        ArrayList<Activity> activitys = new ArrayList<Activity>();

        String sql = "select * from activitys";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    Activity activity = new Activity();
                    activity.setDatabaseRecordNo(cursor.getInt(0));
                    activity.setOwner(cursor.getInt(1));
                    activity.setOwnerType(cursor.getInt(2));
                    activity.setRole(cursor.getInt(3));
                    activity.setLocation(cursor.getInt(4));
                    activity.setDescription(cursor.getString(5));
                    activity.setPriority(cursor.getInt(6));
                    activity.setLowestMinutes(cursor.getInt(7));
                    activity.setHighestMinutes(cursor.getInt(8));
                    activity.setMedianMinutes(cursor.getInt(9));
                    activity.setDataSensitivity(cursor.getInt(10));
                    activity.setTimeStamp(cursor.getString(11));
                    activity.setLastModifiedBy(cursor.getInt(12));
                    activitys.add(activity);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Activitys from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return activitys;
    }


    public ArrayList<Activity> getActivitysByRole(int roleId) {
        ArrayList<Activity> activitys = new ArrayList<Activity>();

        String sql = "select * from activitys where role=" + roleId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    Activity activity = new Activity();
                    activity.setDatabaseRecordNo(cursor.getInt(0));
                    activity.setOwner(cursor.getInt(1));
                    activity.setOwnerType(cursor.getInt(2));
                    activity.setRole(cursor.getInt(3));
                    activity.setLocation(cursor.getInt(4));
                    activity.setDescription(cursor.getString(5));
                    activity.setPriority(cursor.getInt(6));
                    activity.setLowestMinutes(cursor.getInt(7));
                    activity.setHighestMinutes(cursor.getInt(8));
                    activity.setMedianMinutes(cursor.getInt(9));
                    activity.setDataSensitivity(cursor.getInt(10));
                    activity.setTimeStamp(cursor.getString(11));
                    activity.setLastModifiedBy(cursor.getInt(12));
                    activitys.add(activity);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Activitys from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return activitys;
    }

    public ArrayList<Activity> getActivitysByRole(int roleId, int locationId) {
        ArrayList<Activity> activitys = new ArrayList<Activity>();

        String sql =
                "select * from activitys where role=" + roleId
                        + " and location=" + locationId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    Activity activity = new Activity();
                    activity.setDatabaseRecordNo(cursor.getInt(0));
                    activity.setOwner(cursor.getInt(1));
                    activity.setOwnerType(cursor.getInt(2));
                    activity.setRole(cursor.getInt(3));
                    activity.setLocation(cursor.getInt(4));
                    activity.setDescription(cursor.getString(5));
                    activity.setPriority(cursor.getInt(6));
                    activity.setLowestMinutes(cursor.getInt(7));
                    activity.setHighestMinutes(cursor.getInt(8));
                    activity.setMedianMinutes(cursor.getInt(9));
                    activity.setDataSensitivity(cursor.getInt(10));
                    activity.setTimeStamp(cursor.getString(11));
                    activity.setLastModifiedBy(cursor.getInt(12));
                    activitys.add(activity);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Activitys from model");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return activitys;
    }

    public long addActivity(Activity activity) {

        ContentValues values = new ContentValues();
        values.put("owner", activity.getOwner());
        values.put("ownerType", activity.getOwnerType());
        values.put("role", activity.getRole());
        values.put("location", activity.getLocation());
        values.put("description", activity.getDescription());
        values.put("priority", activity.getPriority());
        values.put("lowestMinutes", activity.getLowestMinutes());
        values.put("highestMinutes", activity.getHighestMinutes());
        values.put("medianMinutes", activity.getMedianMinutes());
        values.put("dataSensitivity", activity.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", activity.getLastModifiedBy());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("activitys", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + activity + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public Activity getActivity(int id) {

        String q = "Select * from activitys where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        Activity activity = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                activity = new Activity();
                activity.setDatabaseRecordNo(cursor.getInt(0));
                activity.setOwner(cursor.getInt(1));
                activity.setOwnerType(cursor.getInt(2));
                activity.setRole(cursor.getInt(3));
                activity.setLocation(cursor.getInt(4));
                activity.setDescription(cursor.getString(5));
                activity.setPriority(cursor.getInt(6));
                activity.setLowestMinutes(cursor.getInt(7));
                activity.setHighestMinutes(cursor.getInt(8));
                activity.setMedianMinutes(cursor.getInt(9));
                activity.setDataSensitivity(cursor.getInt(10));
                activity.setTimeStamp(cursor.getString(11));
                activity.setLastModifiedBy(cursor.getInt(12));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching an Activity");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return activity;

    }

    public long updateActivity(Activity activity) {

        ContentValues values = new ContentValues();
        values.put("owner", activity.getOwner());
        values.put("ownerType", activity.getOwnerType());
        values.put("role", activity.getRole());
        values.put("location", activity.getLocation());
        values.put("description", activity.getDescription());
        values.put("priority", activity.getPriority());
        values.put("lowestMinutes", activity.getLowestMinutes());
        values.put("highestMinutes", activity.getHighestMinutes());
        values.put("medianMinutes", activity.getMedianMinutes());
        values.put("dataSensitivity", activity.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", activity.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("activitys", values, "_id = ?",
                new String[]{String.valueOf(activity.getDatabaseRecordNo())});
    }

    public void deleteActivity(int id) {

        Activity activity = getActivity(id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("activitys", "_id = ?",
                    new String[]{String.valueOf(activity.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting activity");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<DiaryEntry> getDiaryEntrys(String startDate) {

        ArrayList<DiaryEntry> diaryEntries = new ArrayList<DiaryEntry>();

        String sql = "select * from diary where dateTime like \'" + startDate + "%\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    DiaryEntry diaryEntry = new DiaryEntry();
                    diaryEntry.setDatabaseRecordNo(cursor.getInt(0));
                    diaryEntry.setOwner(cursor.getInt(1));
                    diaryEntry.setOwnerType(cursor.getInt(2));
                    diaryEntry.setRole(cursor.getInt(3));
                    diaryEntry.setLocation(cursor.getInt(4));
                    diaryEntry.setDateTime(cursor.getString(5));
                    diaryEntry.setTextEntry(cursor.getString(6));
                    diaryEntry.setDataSensitivity(cursor.getInt(7));
                    diaryEntry.setTimeStamp(cursor.getString(8));
                    diaryEntry.setLastModifiedBy(cursor.getInt(9));
                    diaryEntries.add(diaryEntry);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Diary Entries from Database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return diaryEntries;
    }


    public long addDiaryEntry(DiaryEntry diaryEntry) {

        ContentValues values = new ContentValues();
        values.put("owner", diaryEntry.getOwner());
        values.put("ownerType", diaryEntry.getOwnerType());
        values.put("role", diaryEntry.getRole());
        values.put("location", diaryEntry.getLocation());
        values.put("dateTime", diaryEntry.getDateTime());
        values.put("textEntry", diaryEntry.getTextEntry());
        values.put("dataSensitivity", diaryEntry.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", diaryEntry.getLastModifiedBy());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("diary", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + diaryEntry + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public DiaryEntry getDiaryEntry(int id) {

        String q = "Select * from diary where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        DiaryEntry diaryEntry = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                diaryEntry = new DiaryEntry();
                diaryEntry.setDatabaseRecordNo(cursor.getInt(0));
                diaryEntry.setOwner(cursor.getInt(1));
                diaryEntry.setOwnerType(cursor.getInt(2));
                diaryEntry.setRole(cursor.getInt(3));
                diaryEntry.setLocation(cursor.getInt(4));
                diaryEntry.setDateTime(cursor.getString(5));
                diaryEntry.setTextEntry(cursor.getString(6));
                diaryEntry.setDataSensitivity(cursor.getInt(7));
                diaryEntry.setTimeStamp(cursor.getString(8));
                diaryEntry.setLastModifiedBy(cursor.getInt(9));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching a Diary Entry");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return diaryEntry;

    }

    public long updateDiary(DiaryEntry diaryEntry) {

        ContentValues values = new ContentValues();
        values.put("owner", diaryEntry.getOwner());
        values.put("ownerType", diaryEntry.getOwnerType());
        values.put("role", diaryEntry.getRole());
        values.put("location", diaryEntry.getLocation());
        values.put("dateTime", diaryEntry.getDateTime());
        values.put("textEntry", diaryEntry.getTextEntry());
        values.put("dataSensitivity", diaryEntry.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", diaryEntry.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("diary", values, "_id = ?",
                new String[]{String.valueOf(diaryEntry.getDatabaseRecordNo())});
    }

    public void deleteDiaryEntry(int id) {

        DiaryEntry diaryEntry = getDiaryEntry(id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("diary", "_id = ?",
                    new String[]{String.valueOf(diaryEntry.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting diaryEntry");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<PlannedActivity> getPlannedActivitys(String startDate) {

        ArrayList<PlannedActivity> plannedActivities = new ArrayList<PlannedActivity>();
        String sql = "select * from plannedActivitys where startDate = " + startDate;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    PlannedActivity pa = new PlannedActivity();
                    pa.setDatabaseRecordNo(cursor.getInt(0));
                    pa.setOwner(cursor.getInt(1));
                    pa.setOwnerType(cursor.getInt(2));
                    pa.setRole(cursor.getInt(3));
                    pa.setLocation(cursor.getInt(4));
                    pa.setActivity(cursor.getInt(5));
                    pa.setCode(cursor.getString(6));
                    pa.setDescription(cursor.getString(7));
                    pa.setMedianMinutes(cursor.getInt(8));
                    pa.setPriority(cursor.getInt(9));
                    pa.setGeneratorType(cursor.getInt(10));
                    pa.setGeneratorId(cursor.getInt(11));
                    pa.setStartDate(cursor.getString(12));
                    pa.setStartTime(cursor.getString(13));
                    pa.setEndDateTime(cursor.getString(14));
                    pa.setTimeTaken(cursor.getInt(15));
                    pa.setStatus(cursor.getInt(16));
                    pa.setDataSensitivity(cursor.getInt(17));
                    pa.setTimeStamp(cursor.getString(18));
                    pa.setLastModifiedBy(cursor.getInt(19));
                    plannedActivities.add(pa);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Planned Activitys from Database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return plannedActivities;
    }


    public ArrayList<PlannedActivity> getPlannedActivitys(Activity activity) {

        ArrayList<PlannedActivity> plannedActivities = new ArrayList<PlannedActivity>();
        String sql = "select * from plannedActivitys where activity = " + activity.getDatabaseRecordNo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    PlannedActivity pa = new PlannedActivity();
                    pa.setDatabaseRecordNo(cursor.getInt(0));
                    pa.setOwner(cursor.getInt(1));
                    pa.setOwnerType(cursor.getInt(2));
                    pa.setRole(cursor.getInt(3));
                    pa.setLocation(cursor.getInt(4));
                    pa.setActivity(cursor.getInt(5));
                    pa.setCode(cursor.getString(6));
                    pa.setDescription(cursor.getString(7));
                    pa.setMedianMinutes(cursor.getInt(8));
                    pa.setPriority(cursor.getInt(9));
                    pa.setGeneratorType(cursor.getInt(10));
                    pa.setGeneratorId(cursor.getInt(11));
                    pa.setStartDate(cursor.getString(12));
                    pa.setStartTime(cursor.getString(13));
                    pa.setEndDateTime(cursor.getString(14));
                    pa.setTimeTaken(cursor.getInt(15));
                    pa.setStatus(cursor.getInt(16));
                    pa.setDataSensitivity(cursor.getInt(17));
                    pa.setTimeStamp(cursor.getString(18));
                    pa.setLastModifiedBy(cursor.getInt(19));
                    plannedActivities.add(pa);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Planned Activitys from Database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return plannedActivities;
    }

    public ArrayList<PlannedActivity> getPlannedActivitys(ActivityIteration iteration) {

        ArrayList<PlannedActivity> plannedActivities = new ArrayList<PlannedActivity>();
        String sql = "select * from plannedActivitys where generatorType = " + PlannedActivity.ACTIVITY_ITERATION_CHILD
                + " and generatorId = " + iteration.getDatabaseRecordNo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    PlannedActivity pa = new PlannedActivity();
                    pa.setDatabaseRecordNo(cursor.getInt(0));
                    pa.setOwner(cursor.getInt(1));
                    pa.setOwnerType(cursor.getInt(2));
                    pa.setRole(cursor.getInt(3));
                    pa.setLocation(cursor.getInt(4));
                    pa.setActivity(cursor.getInt(5));
                    pa.setCode(cursor.getString(6));
                    pa.setDescription(cursor.getString(7));
                    pa.setMedianMinutes(cursor.getInt(8));
                    pa.setPriority(cursor.getInt(9));
                    pa.setGeneratorType(cursor.getInt(10));
                    pa.setGeneratorId(cursor.getInt(11));
                    pa.setStartDate(cursor.getString(12));
                    pa.setStartTime(cursor.getString(13));
                    pa.setEndDateTime(cursor.getString(14));
                    pa.setTimeTaken(cursor.getInt(15));
                    pa.setStatus(cursor.getInt(16));
                    pa.setDataSensitivity(cursor.getInt(17));
                    pa.setTimeStamp(cursor.getString(18));
                    pa.setLastModifiedBy(cursor.getInt(19));
                    plannedActivities.add(pa);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Planned Activitys from Database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return plannedActivities;
    }

    public long addPlannedActivity(PlannedActivity pa) {

        ContentValues values = new ContentValues();
        values.put("owner", pa.getOwner());
        values.put("ownerType", pa.getOwnerType());
        values.put("role", pa.getRole());
        values.put("location", pa.getLocation());
        values.put("activity", pa.getActivity());
        values.put("code", pa.getCode());
        values.put("description", pa.getDescription());
        values.put("medianMinutes", pa.getMedianMinutes());
        values.put("priority", pa.getPriority());
        values.put("generatorType", pa.getGeneratorType());
        values.put("generatorId", pa.getGeneratorId());
        values.put("startDate", pa.getStartDate());
        values.put("startTime", pa.getStartTime());
        values.put("endDateTime", pa.getEndDateTime());
        values.put("timeTaken", pa.getTimeTaken());
        values.put("status", pa.getStatus());
        values.put("dataSensitivity", pa.getDataSensitivity());
        values.put("timeStamp", paTimeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", pa.getLastModifiedBy());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("plannedActivitys", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + pa + " to model");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public PlannedActivity getPlannedActivity(int id) {

        String q = "Select * from plannedActivitys where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        PlannedActivity pa = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                pa = new PlannedActivity();
                pa.setDatabaseRecordNo(cursor.getInt(0));
                pa.setOwner(cursor.getInt(1));
                pa.setOwnerType(cursor.getInt(2));
                pa.setRole(cursor.getInt(3));
                pa.setLocation(cursor.getInt(4));
                pa.setActivity(cursor.getInt(5));
                pa.setCode(cursor.getString(6));
                pa.setDescription(cursor.getString(7));
                pa.setMedianMinutes(cursor.getInt(8));
                pa.setPriority(cursor.getInt(9));
                pa.setGeneratorType(cursor.getInt(10));
                pa.setGeneratorId(cursor.getInt(11));
                pa.setStartDate(cursor.getString(12));
                pa.setStartTime(cursor.getString(13));
                pa.setEndDateTime(cursor.getString(14));
                pa.setTimeTaken(cursor.getInt(15));
                pa.setStatus(cursor.getInt(16));
                pa.setDataSensitivity(cursor.getInt(17));
                pa.setTimeStamp(cursor.getString(18));
                pa.setLastModifiedBy(cursor.getInt(19));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching a Planned Activity");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return pa;

    }

    public long updatePlannedActivity(PlannedActivity pa) {

        ContentValues values = new ContentValues();
        values.put("owner", pa.getOwner());
        values.put("ownerType", pa.getOwnerType());
        values.put("role", pa.getRole());
        values.put("location", pa.getLocation());
        values.put("activity", pa.getActivity());
        values.put("code", pa.getCode());
        values.put("description", pa.getDescription());
        values.put("medianMinutes", pa.getMedianMinutes());
        values.put("priority", pa.getPriority());
        values.put("generatorType", pa.getGeneratorType());
        values.put("generatorId", pa.getGeneratorId());
        values.put("startDate", pa.getStartDate());
        values.put("startTime", pa.getStartTime());
        values.put("endDateTime", pa.getEndDateTime());
        values.put("timeTaken", pa.getTimeTaken());
        values.put("status", pa.getStatus());
        values.put("dataSensitivity", pa.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", pa.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("plannedActivitys", values, "_id = ?",
                new String[]{String.valueOf(pa.getDatabaseRecordNo())});
    }

    public void deletePlannedActivity(int id) {

        PlannedActivity pa = getPlannedActivity(id);
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("plannedActivitys", "_id = ?",
                    new String[]{String.valueOf(pa.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting planned activity");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ActivityIteration> getActivityIterations() {

        ArrayList<ActivityIteration> activityIterations = new ArrayList<ActivityIteration>();

        String sql = "select * from activityIterations";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    ActivityIteration activityIteration = new ActivityIteration();
                    activityIteration.setDatabaseRecordNo(cursor.getInt(0));
                    activityIteration.setOwner(cursor.getInt(1));
                    activityIteration.setOwnerType(cursor.getInt(2));
                    activityIteration.setRole(cursor.getInt(3));
                    activityIteration.setLocation(cursor.getInt(4));
                    activityIteration.setActivity(cursor.getInt(5));
                    activityIteration.setPriority(cursor.getInt(6));
                    activityIteration.setFrequencyInterval(cursor.getInt(7));
                    activityIteration.setFrequency(cursor.getInt(8));
                    activityIteration.setExemptedDays(cursor.getString(9));
                    activityIteration.setStartDate(cursor.getString(10));
                    activityIteration.setEndDate(cursor.getString(11));
                    activityIteration.setStartTime(cursor.getString(12));
                    activityIteration.setEndTime(cursor.getString(13));
                    activityIteration.setStatus(cursor.getInt(14));
                    activityIteration.setDataSensitivity(cursor.getInt(15));
                    activityIteration.setTimeStamp(cursor.getString(16));
                    activityIteration.setLastModifiedBy(cursor.getInt(17));
                    activityIterations.add(activityIteration);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Activity Iterations from Database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return activityIterations;
    }


    public long addActivityIteration(ActivityIteration activityIteration) {

        ContentValues values = new ContentValues();
        values.put("owner", activityIteration.getOwner());
        values.put("ownerType", activityIteration.getOwnerType());
        values.put("role", activityIteration.getRole());
        values.put("location", activityIteration.getLocation());
        values.put("activity", activityIteration.getActivity());
        values.put("priority", activityIteration.getPriority());
        values.put("frequencyInterval", activityIteration.getFrequencyInterval());
        values.put("frequency", activityIteration.getFrequency());
        values.put("exemptedDays", activityIteration.getExemptedDays());
        values.put("startDate", activityIteration.getStartDate());
        values.put("endDate", activityIteration.getEndDate());
        values.put("startTime", activityIteration.getStartTime());
        values.put("endTime", activityIteration.getEndTime());
        values.put("status", activityIteration.getStatus());
        values.put("dataSensitivity", activityIteration.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", activityIteration.getLastModifiedBy());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("activityIterations", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + activityIteration + " to Database");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public ActivityIteration getActivityIteration(int id) {

        String q = "Select * from activityIterations where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        ActivityIteration activityIteration = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                activityIteration = new ActivityIteration();
                activityIteration.setDatabaseRecordNo(cursor.getInt(0));
                activityIteration.setOwner(cursor.getInt(1));
                activityIteration.setOwnerType(cursor.getInt(2));
                activityIteration.setRole(cursor.getInt(3));
                activityIteration.setLocation(cursor.getInt(4));
                activityIteration.setActivity(cursor.getInt(5));
                activityIteration.setPriority(cursor.getInt(6));
                activityIteration.setFrequencyInterval(cursor.getInt(7));
                activityIteration.setFrequency(cursor.getInt(8));
                activityIteration.setExemptedDays(cursor.getString(9));
                activityIteration.setStartDate(cursor.getString(10));
                activityIteration.setEndDate(cursor.getString(11));
                activityIteration.setStartTime(cursor.getString(12));
                activityIteration.setEndTime(cursor.getString(13));
                activityIteration.setStatus(cursor.getInt(14));
                activityIteration.setDataSensitivity(cursor.getInt(15));
                activityIteration.setTimeStamp(cursor.getString(16));
                activityIteration.setLastModifiedBy(cursor.getInt(17));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching an ActivityIteration");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return activityIteration;

    }

    public long updateActivityIteration(ActivityIteration activityIteration) {

        ContentValues values = new ContentValues();
        values.put("owner", activityIteration.getOwner());
        values.put("ownerType", activityIteration.getOwnerType());
        values.put("role", activityIteration.getRole());
        values.put("location", activityIteration.getLocation());
        values.put("activity", activityIteration.getActivity());
        values.put("priority", activityIteration.getPriority());
        values.put("frequencyInterval", activityIteration.getFrequencyInterval());
        values.put("frequency", activityIteration.getFrequency());
        values.put("exemptedDays", activityIteration.getExemptedDays());
        values.put("startDate", activityIteration.getStartDate());
        values.put("endDate", activityIteration.getEndDate());
        values.put("startTime", activityIteration.getStartTime());
        values.put("endTime", activityIteration.getEndTime());
        values.put("status", activityIteration.getStatus());
        values.put("dataSensitivity", activityIteration.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", activityIteration.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("activityIterations", values, "_id = ?",
                new String[]{String.valueOf(activityIteration.getDatabaseRecordNo())});
    }

    public void deleteActivityIteration(int id) {

        ActivityIteration activityIteration = getActivityIteration(id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("activityIterations", "_id = ?",
                    new String[]{String.valueOf(activityIteration.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting activity iteration");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ActivitySequenceList> getActivitySequenceLists() {

        ArrayList<ActivitySequenceList> sequences = new ArrayList<ActivitySequenceList>();

        String sql = "select * from activitySequenceLists";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    ActivitySequenceList asl = new ActivitySequenceList();
                    asl.setDatabaseRecordNo(cursor.getInt(0));
                    asl.setOwner(cursor.getInt(1));
                    asl.setOwnerType(cursor.getInt(2));
                    asl.setRole(cursor.getInt(3));
                    asl.setLocation(cursor.getInt(4));
                    asl.setDescription(cursor.getString(5));
                    asl.setDataSensitivity(cursor.getInt(6));
                    asl.setTimeStamp(cursor.getString(7));
                    asl.setLastModifiedBy(cursor.getInt(8));
                    sequences.add(asl);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Activity Sequence from Database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return sequences;
    }


    public long addActivitySequenceList(ActivitySequenceList sequence) {

        ContentValues values = new ContentValues();
        values.put("owner", sequence.getOwner());
        values.put("ownerType", sequence.getOwnerType());
        values.put("role", sequence.getRole());
        values.put("location", sequence.getLocation());
        values.put("description",sequence.getDescription());
        values.put("dataSensitivity", sequence.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", sequence.getLastModifiedBy());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("activitySequenceLists", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add " + sequence + " to Database");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public ActivitySequenceList getActivitySequenceList(int id) {

        String q = "Select * from activitySequenceLists where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        ActivitySequenceList asl  = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                asl = new ActivitySequenceList();
                asl.setDatabaseRecordNo(cursor.getInt(0));
                asl.setOwner(cursor.getInt(1));
                asl.setOwnerType(cursor.getInt(2));
                asl.setRole(cursor.getInt(3));
                asl.setLocation(cursor.getInt(4));
                asl.setDescription(cursor.getString(5));
                asl.setDataSensitivity(cursor.getInt(6));
                asl.setTimeStamp(cursor.getString(7));
                asl.setLastModifiedBy(cursor.getInt(8));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching an Activity Sequence List");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return asl;

    }

    public long updateActivitySequenceList(ActivitySequenceList sequence) {

        ContentValues values = new ContentValues();
        values.put("owner", sequence.getOwner());
        values.put("ownerType", sequence.getOwnerType());
        values.put("role", sequence.getRole());
        values.put("location", sequence.getLocation());
        values.put("description", sequence.getDescription());
        values.put("dataSensitivity", sequence.getDataSensitivity());
        values.put("timeStamp", timeStampDateFormat.format(new Date()));
        values.put("lastModifiedBy", sequence.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("activitySequenceLists", values, "_id = ?",
                new String[]{String.valueOf(sequence.getDatabaseRecordNo())});
    }

    public void deleteActivitySequenceList(int id) {

        ActivitySequenceList activitySequenceList = getActivitySequenceList(id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("activitySequenceLists", "_id = ?",
                    new String[]{String.valueOf(activitySequenceList.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting activity sequence list");
        } finally {
            db.endTransaction();
        }
    }


    public ArrayList<ActivitySequenceListItem> getActivitySequenceListItems() {

        ArrayList<ActivitySequenceListItem> items = new ArrayList<ActivitySequenceListItem>();

        String sql = "select * from activitySequenceListItems";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                    ActivitySequenceListItem item = new ActivitySequenceListItem();
                    item.setDatabaseRecordNo(cursor.getInt(0));
                    item.setActivitySequenceList(cursor.getInt(1));
                    item.setActivity(cursor.getInt(2));
                    item.setExecutionOrder(cursor.getInt(3));
                    item.setDuration(cursor.getInt(4));
                    items.add(item);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching Activity Sequence List Item from Database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return items;
    }


    public long addActivitySequenceListItem(ActivitySequenceListItem item) {

        ContentValues values = new ContentValues();
        values.put("activitySequenceList", item.getActivitySequenceList());
        values.put("activity",item.getActivity());
        values.put("executionOrder",item.getExecutionOrder());
        values.put("duration", item.getDuration());

        long newId = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            newId = db.insertOrThrow("activitySequenceListItems", null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error trying to add activity sequence list item " + item + " to Database");
        } finally {
            db.endTransaction();
        }
        return newId;

    }

    public ActivitySequenceListItem getActivitySequenceListItem(int id) {

        String q = "Select * from activitySequenceListItems where _id = " + id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q, null);

        ActivitySequenceListItem item  = null;
        try {
            if (cursor.moveToFirst()) {
                cursor.moveToFirst();
                item = new ActivitySequenceListItem();
                item.setDatabaseRecordNo(cursor.getInt(0));
                item.setActivitySequenceList(cursor.getInt(1));
                item.setActivity(cursor.getInt(2));
                item.setExecutionOrder(cursor.getInt(3));
                item.setDuration(cursor.getInt(4));
            }
        } catch (Exception e) {
            Log.i(TAG, "Error whilst fetching an Activity Sequence List Item");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return item;

    }

    public long updateActivitySequenceListItem(ActivitySequenceListItem item) {

        ContentValues values = new ContentValues();
        values.put("activitySequenceList", item.getActivitySequenceList());
        values.put("activity",item.getActivity());
        values.put("executionOrder",item.getExecutionOrder());
        values.put("duration", item.getDuration());

        SQLiteDatabase db = this.getWritableDatabase();

        return db.update("activitySequenceListItems", values, "_id = ?",
                new String[]{String.valueOf(item.getDatabaseRecordNo())});
    }

    public void deleteActivitySequenceListItem(int id) {

        ActivitySequenceListItem activitySequenceListItem = getActivitySequenceListItem(id);

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("activitySequenceListItems", "_id = ?",
                    new String[]{String.valueOf(activitySequenceListItem.getDatabaseRecordNo())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.i(TAG, "Error whilst deleting activity sequence list Item");
        } finally {
            db.endTransaction();
        }
    }



}
