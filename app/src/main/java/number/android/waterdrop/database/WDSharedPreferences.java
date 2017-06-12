package number.android.waterdrop.database;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 11/17/2016.
 */

public class WDSharedPreferences {

    private Context context;
    public static final String DEVICE_TOKEN = "device_token";
    public static final String VENDOR_STATUS = "vendor_status";


    public WDSharedPreferences(Context context) {
        this.context = context;
    }

    /**
     *  Device token
     */
    public void setDeviceToken(String deviceToken){
        SharedPreferences.Editor vendor_editor = context.getSharedPreferences(DEVICE_TOKEN, MODE_PRIVATE).edit();
        vendor_editor.putString(DEVICE_TOKEN, deviceToken);
        vendor_editor.commit();
    }

    public String getDeviceToken(){
        SharedPreferences prefs = context.getSharedPreferences(DEVICE_TOKEN, MODE_PRIVATE);
        return prefs.getString(DEVICE_TOKEN,"NA");
    }


    /**
     *  is vendor set
     */
    public void setVendorStatus(boolean status){
        SharedPreferences.Editor vendor_status = context.getSharedPreferences(VENDOR_STATUS, MODE_PRIVATE).edit();
        vendor_status.putBoolean(VENDOR_STATUS, status);
        vendor_status.commit();
    }

    public boolean getVendorStatus(){
        SharedPreferences prefs = context.getSharedPreferences(VENDOR_STATUS, MODE_PRIVATE);
        return prefs.getBoolean(VENDOR_STATUS,false);
    }

}
