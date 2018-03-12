package uk.co.thomas_cross.personalorganiser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.Person;

/**
 * Created by thomas on 07/03/18.
 */

public class PersonsDBFrontEnd extends DBFrontEnd {

    public PersonsDBFrontEnd(Context context) {
        super(context);
    }

    public ArrayList<Person> getPersons(){

        ArrayList<Person> people = new ArrayList<Person>();

        String sql = "select * from persons";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if ( cursor.moveToFirst() ){

            do {

                Person person = new Person();
                person.setDatabaseRecordNo(Integer.parseInt(cursor.getString(0)));
                person.setOwner(Integer.parseInt(cursor.getString(1)));
                person.setOwnerType(Integer.parseInt(cursor.getString(2)));
                person.setFirstName(cursor.getString(3));
                person.setMiddleNames(cursor.getString(4));
                person.setLastName(cursor.getString(5));
                person.setGender(Integer.parseInt(cursor.getString(6)));
                person.setUserId(Integer.parseInt(cursor.getString(7)));
                person.setDataSensitivity(Integer.parseInt(cursor.getString(8)));
                person.setTimeStamp(cursor.getString(9));
                person.setLastModifiedBy(Integer.parseInt(cursor.getString(10)));
                people.add(person);

            } while (cursor.moveToNext());
        }

        return people;
    }


    public Person addPerson(Person person){

        ContentValues values = new ContentValues();
        values.put("_id",0);
        values.put("owner", person.getOwner());
        values.put("ownerType", person.getOwnerType());
        values.put("firstName",person.getFirstName());
        values.put("middleNames",person.getMiddleNames());
        values.put("lastName",person.getLastName());
        values.put("gender",person.getGender());;
        values.put("userId",person.getUserId());
        values.put("dataSensitivity",person.getDataSensitivity());
        values.put("timeStamp",person.getTimeStamp());
        values.put("lastModifiedBy", person.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        long newId = db.insert("persons",null,values);
        db.close();
        return getPerson(newId);

    }

    public Person getPerson(long id){

        String q = "Select * from persons where _id = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);

        Person person = null;

        if ( cursor.moveToFirst() ){
            cursor.moveToFirst();
            person.setDatabaseRecordNo(Integer.parseInt(cursor.getString(0)));
            person.setOwner(Integer.parseInt(cursor.getString(1)));
            person.setOwnerType(Integer.parseInt(cursor.getString(2)));
            person.setFirstName(cursor.getString(3));
            person.setMiddleNames(cursor.getString(4));
            person.setLastName(cursor.getString(5));
            person.setGender(Integer.parseInt(cursor.getString(6)));
            person.setUserId(Integer.parseInt(cursor.getString(7)));
            person.setDataSensitivity(Integer.parseInt(cursor.getString(8)));
            person.setTimeStamp(cursor.getString(9));
            person.setLastModifiedBy(Integer.parseInt(cursor.getString(10)));
        }

        cursor.close();
        db.close();
        return person;

    }

    public Person updatePerson(Person person){

        ContentValues values = new ContentValues();
        values.put("_id",0);
        values.put("owner", person.getOwner());
        values.put("ownerType", person.getOwnerType());
        values.put("firstName",person.getFirstName());
        values.put("middleNames",person.getMiddleNames());
        values.put("lastName",person.getLastName());
        values.put("gender",person.getGender());;
        values.put("userId",person.getUserId());
        values.put("dataSensitivity",person.getDataSensitivity());
        values.put("timeStamp",person.getTimeStamp());
        values.put("lastModifiedBy", person.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        long updateId = db.update("persons",values,"_id = ?",
                new String[] {String.valueOf(person.getDatabaseRecordNo())});
        db.close();

        return getPerson(updateId);
    }

    public Person deletePerson(long personID){

        Person deletedPerson = getPerson(personID);
        SQLiteDatabase db = this.getWritableDatabase();
        long id =
                db.delete("persons","_id = ?",
                        new String[] {String.valueOf(deletedPerson.getDatabaseRecordNo())});
        db.close();
        if ( id != personID )
            deletedPerson = null;

        return deletedPerson;
    }

}
