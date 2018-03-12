package uk.co.thomas_cross.personalorganiser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by root on 26/12/17.
 */

public class DBFrontEnd extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "/co.uk.thomascross/personalOrganiser.db";


    public DBFrontEnd(Context context) {

        super(context,
                Environment.getExternalStorageDirectory().getAbsolutePath()
                        + DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createRoles = "Create table roles (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " title text not null, dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";
        db.execSQL(createRoles);

        // Create the userId table

        String createPersons = "Create table persons (" +
                " _id integer primary key autoincrement," +
                " owner integer, ownerType integer," +
                " firstName text not null, middleNames text not null," +
                " lastName text not null, gender integer, userId integer," +
                " dataSensitivity integer, " +
                " timeStamp text not null, lastModifiedBy integer)";
        db.execSQL(createPersons);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          int oldVersion, int newVersion) {

        switch (oldVersion) {
            case 1:
                //upgrade logic from version 1 to 2
            case 2:
                //upgrade logic from version 2 to 3
            case 3:
                //upgrade logic from version 3 to 4
                break;
            default:
                throw new IllegalStateException(
                        "onUpgrade() with unknown oldVersion " + oldVersion);
        }
    }


}
