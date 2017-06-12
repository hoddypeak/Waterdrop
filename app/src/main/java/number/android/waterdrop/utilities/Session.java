package number.android.waterdrop.utilities;

import android.content.Context;

import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.WDSharedPreferences;
import number.android.waterdrop.database.models.CustomerModel;
import number.android.waterdrop.database.models.VendorModel;
import number.android.waterdrop.database.models.entities.User;

/**
 * Created by user on 1/5/2017.
 */

public class Session {

    Context context;
    CustomerModel customerModel;
    DatabaseConfig databaseConfig;
    VendorModel vendorModel;

    WDSharedPreferences preferences;

    public Session(Context context) {
        this.context = context;
        databaseConfig = new DatabaseConfig(context);
        preferences = new WDSharedPreferences(context);
    }

    public boolean isRegistered(){
        customerModel = new CustomerModel(databaseConfig);
        if(customerModel.getRootUserId() != 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isDefaultVendorSet(){
        vendorModel = new VendorModel(databaseConfig);
        if(vendorModel.getVendor() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void start(){

        if( isRegistered() ){
            UserSession.getInstance().setUser_id(customerModel.getRootUserId());
            User user = customerModel.getUserById(customerModel.getRootUserId());
            UserSession.getInstance().getInstance().setLatitude(Double.parseDouble( user.getUserLocation().getLatitude() ));
            UserSession.getInstance().getInstance().setLongitude(Double.parseDouble( user.getUserLocation().getLongitude() ));
            if( isDefaultVendorSet() ){
                UserSession.getInstance().setVendor_id( vendorModel.getVendor() );
                UserSession.getInstance().setVendor_status( preferences.getVendorStatus() );
            }
        }
        databaseConfig.close();
    }

    public void stop(){

    }

}
