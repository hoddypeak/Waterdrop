package number.android.waterdrop.database.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.contracts.Customer;
import number.android.waterdrop.database.contracts.CustomerAddress;
import number.android.waterdrop.database.models.entities.User;


public class CustomerModel {

    DatabaseConfig dbHelper;
    SQLiteDatabase db;

    public CustomerModel(DatabaseConfig dbHelper) {
        this.dbHelper = dbHelper;
        db = dbHelper.getReadableDatabase();
    }

    public CustomerModel(){

    }

    /**
     * Gets the list of Messages from the database.
     *
     * @return the current projects from the database.
     */
    public ArrayList getAll() {

        ArrayList messages = new ArrayList();

        // Gets the database in the current database helper in read-only mode
        Cursor cursor = db.query(number.android.waterdrop.database.contracts.Customer.TABLE_NAME, null, null,
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
        long messageId;

        messageId = db.insert(number.android.waterdrop.database.contracts.Customer.TABLE_NAME, null, contentValues);
        return messageId;
    }

    public void update(ContentValues contentValues,String[] whereArgs){
        String whereClause = "RMT_ID = ?";
        db.update(Customer.TABLE_NAME,contentValues,whereClause,whereArgs);
    }

    public int getRootUserId(){
        int rootUserId = 0;
        String[] tableColumns = new String[] {"*"};
        String whereClause = "PARENT_ID = ?";
        String[] whereArgs = new String[] { "0" };
        String orderBy = null;
        Cursor cursor = db.query(number.android.waterdrop.database.contracts.Customer.TABLE_NAME,
                tableColumns, whereClause, whereArgs, null, null, null);

        while (cursor.moveToNext()) {
            rootUserId = cursor.getInt(cursor.getColumnIndex(number.android.waterdrop.database.contracts.Customer.Column.RMT_ID));
        }

        cursor.close();
        return (rootUserId);
    }

    public User getUserById(int id){
        User user = new User();
        User.UserLocation userLocation = new User.UserLocation();
        String table = number.android.waterdrop.database.contracts.Customer.TABLE_NAME + " as c, " +
                CustomerAddress.TABLE_NAME + " as ca";
        String[] tableColumns = new String[] {"*"};
        String whereClause = "c.RMT_ADDRESS_ID = ca.RMT_ID and c.RMT_ID = ?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        String orderBy = null;
        Cursor cursor = db.query(table, tableColumns, whereClause, whereArgs, null, null, null);

        Log.d("dumpCursor",DatabaseUtils.dumpCursorToString(cursor));

        while (cursor.moveToNext()) {
            user.setId( cursor.getInt(cursor.getColumnIndex(number.android.waterdrop.database.contracts.Customer.Column.RMT_ID)) );
            user.setName( cursor.getString(cursor.getColumnIndex(Customer.Column.NAME)) );
            user.setMobile_number( cursor.getString(cursor.getColumnIndex(Customer.Column.MOBILE_NUMBER)) );
            user.setAddress(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.ADDRESS)));
            user.setRmt_address_id(cursor.getInt(cursor.getColumnIndex(Customer.Column.RMT_ADDRESS_ID)));

            userLocation.setAddress(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.ADDRESS)));
            userLocation.setStreet(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.STREET)));
            userLocation.setCity(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.CITY)));
            userLocation.setState(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.STATE)));
            userLocation.setCountry(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.COUNTRY)));
            userLocation.setLandmark(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.LANDMARK)));
            userLocation.setPostalCode(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.POSTAL_CODE)));
            userLocation.setLatitude(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.LATITUDE)));
            userLocation.setLongitude(cursor.getString(cursor.getColumnIndex(CustomerAddress.Column.LONGITUDE)));

            user.setUserLocation(userLocation);
        }

        cursor.close();
        return user;
    }


}
