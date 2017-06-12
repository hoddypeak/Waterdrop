package number.android.waterdrop.database.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.contracts.Customer;
import number.android.waterdrop.database.contracts.CustomerAddress;
import number.android.waterdrop.database.models.entities.User;


public class CustomerAddressModel {

    DatabaseConfig dbHelper;
    SQLiteDatabase db;

    public CustomerAddressModel(DatabaseConfig dbHelper) {
        this.dbHelper = dbHelper;
        db = dbHelper.getReadableDatabase();
    }

    public CustomerAddressModel(){

    }

    /**
     * Gets the list of Messages from the database.
     *
     * @return the current projects from the database.
     */
    public ArrayList getAll() {

        ArrayList messages = new ArrayList();

        // Gets the database in the current database helper in read-only mode

        Cursor cursor = db.query(number.android.waterdrop.database.contracts.CustomerAddress.TABLE_NAME, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            User msgs = new User();
//            msgs.getId();
           // msgs.setMessage(cursor.getString(cursor.getColumnIndex(number.android.waterdrop.database.contracts.User.Column.NAME)));
            messages.add(msgs);
        }

        cursor.close();
        return (messages);
    }

    /*
    *   Insert the message
    */
    public long insert(ContentValues contentValues) {
        return db.insert(number.android.waterdrop.database.contracts.CustomerAddress.TABLE_NAME, null, contentValues);
    }


    public void update(ContentValues contentValues,String[] whereArgs){
        String whereClause = "RMT_ID = ?";
        db.update(CustomerAddress.TABLE_NAME,contentValues,whereClause,whereArgs);
    }




}
