package number.android.waterdrop.database.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.models.entities.User;


public class VendorModel {

    DatabaseConfig dbHelper;
    SQLiteDatabase db;

    String TABLE = number.android.waterdrop.database.contracts.Vendor.TABLE_NAME;

    public VendorModel(DatabaseConfig dbHelper) {
        this.dbHelper = dbHelper;
        db = dbHelper.getReadableDatabase();
    }

    public VendorModel(){

    }

    /**
     * Gets the list of Messages from the database.
     *
     * @return the current projects from the database.
     */
    public ArrayList getAll() {

        ArrayList vendors = new ArrayList();

        // Gets the database in the current database helper in read-only mode
        Cursor cursor = db.query(TABLE, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            User msgs = new User();
//            msgs.getId();
           // msgs.setMessage(cursor.getString(cursor.getColumnIndex(number.android.waterdrop.database.contracts.User.Column.NAME)));
            vendors.add(msgs);
        }

        cursor.close();
        return (vendors);
    }

    /*
    *   Insert the message
    */
    public long insert(ContentValues contentValues) {
        return db.insert(TABLE, null, contentValues);
    }

    public int getVendor(){
        int vendorId = 0;
        String[] tableColumns = new String[] {"*"};
        String whereClause = "status = ?";
        String[] whereArgs = new String[] { "1" };
        String orderBy = null;
        Cursor cursor = db.query(TABLE, tableColumns, whereClause, whereArgs, null, null, null);

        while (cursor.moveToNext()) {
            vendorId = cursor.getInt(cursor.getColumnIndex(number.android.waterdrop.database.contracts.Vendor.Column.RMT_ID));
        }

        cursor.close();
        return (vendorId);

    }




}
