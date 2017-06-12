package number.android.waterdrop.database.contracts;

import android.provider.BaseColumns;

/**
 * Created by user on 9/17/2016.
 */
public final class Customer {

    public static final String TABLE_NAME = "customer";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + " ("
            + Column._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Column.RMT_ID +" INTEGER,"
            + Column.CUSTOMER_CODE + " VARCHAR(20),"
            + Column.PARENT_ID + " INTEGER,"
            + Column.NAME + " VARCHAR(20),"
            + Column.RMT_ADDRESS_ID + " INTEGER,"
            + Column.MOBILE_NUMBER + " VARCHAR(20),"
            + Column.STATUS + " INTEGER,"
            + Column.CREATED_DATE + " VARCHAR(30),"
            + Column.UPDATED_DATE + " VARCHAR(30)"
//            + " FOREIGN KEY ("+ Column.RMT_PARENT_ID + ") " +
//            "REFERENCES "+ ParentsContract.TABLE_NAME + " (" + ParentsContract.Column.RMT_PARENT_ID + "), "
            + " );";

    public static abstract class Column implements BaseColumns {

        public static final String RMT_ID = "rmt_id";
        public static final String CUSTOMER_CODE = "customer_code";
        public static final String PARENT_ID = "parent_id";
        public static final String NAME = "name";
        public static final String RMT_ADDRESS_ID = "rmt_address_id";
        public static final String MOBILE_NUMBER = "mobile_number";
        public static final String STATUS = "status";

        public static final String CREATED_DATE = "created_date";
        public static final String UPDATED_DATE = "updated_date";

    }
}
