package uk.co.thomas_cross.personalorganiser.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by thomas on 07/03/18.
 */

public class RoleDBFrontEnd extends DBFrontEnd {

    public RoleDBFrontEnd(Context context) {
        super(context);
    }


    public ArrayList<Role> getRoles(){

        ArrayList<Role> roles = new ArrayList<Role>();

        String sql = "select * from roles";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);

        if ( cursor.moveToFirst() ){

            do {

                Role role = new Role();
                role.setDatabaseRecordNo(Integer.parseInt(cursor.getString(0)));
                role.setOwner(Integer.parseInt(cursor.getString(1)));
                role.setOwnerType(Integer.parseInt(cursor.getString(2)));
                role.setTitle(cursor.getString(3));
                role.setDataSensitivity(Integer.parseInt(cursor.getString(4)));
                role.setTimeStamp(cursor.getString(5));
                role.setLastModifiedBy(Integer.parseInt(cursor.getString(6)));
                roles.add(role);

            } while (cursor.moveToNext());
        }

        return roles;
    }


    public Role addRole(Role role){

        ContentValues values = new ContentValues();
        values.put("_id",0);
        values.put("owner", role.getOwner());
        values.put("ownerType", role.getOwnerType());
        values.put("title", role.getTitle());
        values.put("dataSensitivity",role.getDataSensitivity());
        values.put("timeStamp",role.getTimeStamp());
        values.put("lastModifiedBy", role.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        long newId = db.insert("roles",null,values);
        db.close();
        return getRole(newId);

    }

    public Role getRole(long id){

        String q = "Select * from roles where _id = "+id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(q,null);

        Role role = null;

        if ( cursor.moveToFirst() ){
            cursor.moveToFirst();
            role.setDatabaseRecordNo(Integer.parseInt(cursor.getString(0)));
            role.setOwner(Integer.parseInt(cursor.getString(1)));
            role.setOwnerType(Integer.parseInt(cursor.getString(2)));
            role.setTitle(cursor.getString(3));
            role.setDataSensitivity(Integer.parseInt(cursor.getString(4)));
            role.setTimeStamp(cursor.getString(5));
            role.setLastModifiedBy(Integer.parseInt(cursor.getString(6)));
        }

        cursor.close();
        db.close();
        return role;

    }

    public Role updateRole(Role role){

        ContentValues values = new ContentValues();
        values.put("role_id",role.getDatabaseRecordNo());
        values.put("owner", role.getOwner());
        values.put("ownerType", role.getOwnerType());
        values.put("title", role.getTitle());
        values.put("dataSensitivity",role.getDataSensitivity());
        values.put("timeStamp",role.getTimeStamp());
        values.put("lastModifiedBy", role.getLastModifiedBy());

        SQLiteDatabase db = this.getWritableDatabase();

        long updateId = db.update("roles",values,"_id = ?",
                new String[] {String.valueOf(role.getDatabaseRecordNo())});
        db.close();

        return getRole(updateId);
    }

    public Role deleteRole(long roleID){

        Role deletedRole = getRole(roleID);
        SQLiteDatabase db = this.getWritableDatabase();
        long id =
                db.delete("roles","_id = ?",
                        new String[] {String.valueOf(deletedRole.getDatabaseRecordNo())});
        db.close();
        if ( id != roleID )
            deletedRole = null;

        return deletedRole;
    }

}
