package number.android.waterdrop.database.contracts;

import android.provider.BaseColumns;

/**
 * Created by user on 9/17/2016.
 */
public final class CustomerAddress {

    public static final String TABLE_NAME = "customers_addresses";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + " ("
            + Column._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Column.RMT_ID +" INTEGER,"
            + Column.LATITUDE + " REAL,"
            + Column.LONGITUDE + " REAL,"
            + Column.ADDRESS + " VARCHAR(20),"
            + Column.STREET + " VARCHAR(100),"
            + Column.CITY + " VARCHAR(20),"
            + Column.STATE + " VARCHAR(20),"
            + Column.COUNTRY + " VARCHAR(20),"
            + Column.POSTAL_CODE + " VARCHAR(20),"
            + Column.LANDMARK + " VARCHAR(70),"
            + Column.STATUS + " INTEGER,"
            + Column.CREATED_DATE + " VARCHAR(30),"
            + Column.UPDATED_DATE + " VARCHAR(30)"
            + " );";

    public static abstract class Column implements BaseColumns {

        public static final String RMT_ID = "rmt_id";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String ADDRESS = "address";
        public static final String STREET = "street";
        public static final String CITY = "city";
        public static final String STATE = "state";
        public static final String COUNTRY = "country";
        public static final String POSTAL_CODE = "postal_code";
        public static final String LANDMARK = "landmark";
        public static final String STATUS = "status";
        
        public static final String CREATED_DATE = "created_date";
        public static final String UPDATED_DATE = "updated_date";

    }

    public static final String SQL_ALTER_TABLE = "ALTER TABLE "+ TABLE_NAME +
                " ADD COLUMN "+ Column.LANDMARK +" VARCHAR(70)";

}
