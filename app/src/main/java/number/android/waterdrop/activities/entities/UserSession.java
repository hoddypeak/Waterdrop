package number.android.waterdrop.activities.entities;

/**
 * Created by user on 10/10/2016.
 */
public class UserSession {

    private static UserSession ourInstance = new UserSession();

    public static UserSession getInstance() {
        return ourInstance;
    }

    private static int user_id = 0;
    private static int vendor_id = 0;
    private static int open_order_count = 0;
    private static double latitude;
    private static double longitude;
    private static boolean vendor_status = false;


    private UserSession() {
    }

    public static int getUser_id() {
        return user_id;
    }

    public static void setUser_id(int user_id) {
        UserSession.user_id = user_id;
    }

    public static int getVendor_id() {
        return vendor_id;
    }

    public static void setVendor_id(int vendor_id) {
        UserSession.vendor_id = vendor_id;
    }

    public static UserSession getOurInstance() {
        return ourInstance;
    }

    public static void setOurInstance(UserSession ourInstance) {
        UserSession.ourInstance = ourInstance;
    }

    public static int getOpen_order_count() {
        return open_order_count;
    }

    public static void setOpen_order_count(int open_order_count) {
        UserSession.open_order_count = open_order_count;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static void setLatitude(double latitude) {
        UserSession.latitude = latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    public static void setLongitude(double longitude) {
        UserSession.longitude = longitude;
    }

    public static boolean isVendor_status() {
        return vendor_status;
    }

    public static void setVendor_status(boolean vendor_status) {
        UserSession.vendor_status = vendor_status;
    }
}
