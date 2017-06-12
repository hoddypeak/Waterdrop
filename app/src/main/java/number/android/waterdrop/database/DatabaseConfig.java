package number.android.waterdrop.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import number.android.waterdrop.database.contracts.Customer;
import number.android.waterdrop.database.contracts.CustomerAddress;
import number.android.waterdrop.database.contracts.Vendor;


public class DatabaseConfig extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "waterdrop_customer.db";

    public static final String DATABASE_VACUUM = "VACUUM";

    public DatabaseConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Customer.SQL_CREATE_TABLE);
        db.execSQL(CustomerAddress.SQL_CREATE_TABLE);
        db.execSQL(Vendor.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            version_2(db);
        }
    }

    /**
     * Version 2
     *
     * @param db
     * @return
     */
    SQLiteDatabase version_2(SQLiteDatabase db){
        db.execSQL(CustomerAddress.SQL_ALTER_TABLE);
        return db;
    }
}
