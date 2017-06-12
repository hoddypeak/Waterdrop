package number.android.waterdrop.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.models.CustomerModel;
import number.android.waterdrop.database.models.VendorModel;
import number.android.waterdrop.database.models.entities.User;
import number.android.waterdrop.network.Internet;
import number.android.waterdrop.network.NetworkState;

public class ActivityOne extends AppCompatActivity {

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        userSession = UserSession.getInstance();

        Internet.getInstance().checkConnection(this);

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isRegistered()){
                    if(isDefaultVendorSet()){
                        toOrderScreen();
                    } else {
                        toVendorPickScreen();
                    }
                } else {
                    toSignUpScreen();
                }

            }
        }, 1000);
    }

    void toSignUpScreen(){
        Intent intent = new Intent(ActivityOne.this,SignupActivity.class);
        startActivity(intent);
    }

    void toOrderScreen(){
        Intent intent = new Intent(ActivityOne.this,WaterDropActivity.class);
        startActivity(intent);
    }

    void toVendorPickScreen(){
        Intent intent = new Intent(ActivityOne.this,VendorPickActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onNetworkState(NetworkState event) {
        if (!event.isInternetConnected()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Yes Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isRegistered(){
        DatabaseConfig databaseConfig = new DatabaseConfig(this);
        CustomerModel customerModel = new CustomerModel(databaseConfig);
        if(customerModel.getRootUserId() != 0) {

            userSession.setUser_id(customerModel.getRootUserId());

            User user = customerModel.getUserById(customerModel.getRootUserId());

            userSession.getInstance().setLatitude(
                    Double.parseDouble( user.getUserLocation().getLatitude() )
            );
            userSession.getInstance().setLongitude(
                    Double.parseDouble( user.getUserLocation().getLongitude() )
            );

            return true;
        } else {
            return false;
        }
    }

    public boolean isDefaultVendorSet(){
        DatabaseConfig databaseConfig = new DatabaseConfig(this);
        VendorModel vendorModel = new VendorModel(databaseConfig);
        if(vendorModel.getVendor() != 0) {
            userSession.setVendor_id(vendorModel.getVendor());
            return true;
        } else {
            return false;
        }
    }
}
