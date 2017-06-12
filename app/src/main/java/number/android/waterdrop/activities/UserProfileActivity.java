package number.android.waterdrop.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.schibstedspain.leku.LocationPickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.cloud.entities.request.UpdateUserReq;
import number.android.waterdrop.cloud.entities.response.UpdateUserRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.contracts.Customer;
import number.android.waterdrop.database.contracts.CustomerAddress;
import number.android.waterdrop.database.models.CustomerAddressModel;
import number.android.waterdrop.database.models.CustomerModel;
import number.android.waterdrop.database.models.entities.User;
import number.android.waterdrop.network.Internet;
import number.android.waterdrop.network.NetworkState;
import number.android.waterdrop.utilities.Icons;
import number.android.waterdrop.utilities.Session;
import number.android.waterdrop.utilities.WDProgressDialog;
import retrofit2.Call;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Icons icons;

    EditText name, mobile, address, landmark;
    int rmt_address_id;
    TextView update;
    MaterialProgressBar loading_indicator;
    Button current_location;

    private static final int PERMISSION_REQUEST_CODE = 1;
    final static int REQUEST_LOCATION = 199;

    GoogleApiClient googleApiClient;

    LocationManager locationManager;
    GPSCoordinates gpsCoordinates;
    WDProgressDialog wdProgressDialog;

    UpdateUserReq.UserLocation customerLocation;

    Client client;

    int canProceed = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        new Session(this).start();
        icons = new Icons(this);
        customerLocation = new UpdateUserReq.UserLocation();

        /*
         *  Setup progress dialog
         */
        wdProgressDialog = new WDProgressDialog(this);
        wdProgressDialog.setMessage("Updating...");

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
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

    private void initView() {
        name = (EditText) findViewById(R.id.user_name);
        name.setCompoundDrawables(icons.personIcon(), null, null, null);

        mobile = (EditText) findViewById(R.id.mobile_number);
        mobile.setCompoundDrawables(icons.mobileIcon(), null, null, null);

        address = (EditText) findViewById(R.id.address);
        address.setCompoundDrawables(icons.homeIcon(), null, null, null);

        current_location = (Button) findViewById(R.id.current_location);
        current_location.setOnClickListener(this);

        landmark = (EditText) findViewById(R.id.landmark);
        landmark.setCompoundDrawables(icons.personIcon(), null, null, null);

        update = (TextView) findViewById(R.id.update);
        update.setOnClickListener(this);

        loading_indicator = (MaterialProgressBar)  findViewById(R.id.loading_indicator);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showMessage();
        }

        getUserDetail();
    }

    void activateLeku(){
        Intent intent = new Intent(this, LocationPickerActivity.class);
        intent.putExtra(LocationPickerActivity.BACK_PRESSED_RETURN_OK, true);
        startActivityForResult(intent, 1);
    }

    void getLocationFromLeku(Intent data){

        Address leku_address = data.getParcelableExtra(LocationPickerActivity.ADDRESS);

        if (leku_address != null) {
            StringBuilder strAddress = new StringBuilder();
            for (int i = 0; i < leku_address.getMaxAddressLineIndex(); i++) {
                strAddress.append(leku_address.getAddressLine(i)).append("\n");
            }
            address.setText(strAddress.toString());

            customerLocation = new UpdateUserReq.UserLocation();
            customerLocation.setAddress(strAddress.toString());
            customerLocation.setStreet(leku_address.getAddressLine(0));
            customerLocation.setCity(leku_address.getLocality());
            customerLocation.setState(leku_address.getAdminArea());
            customerLocation.setCountry(leku_address.getCountryName());
            customerLocation.setPostCode(leku_address.getPostalCode());
            customerLocation.setLatitude(String.valueOf(leku_address.getLatitude()));
            customerLocation.setLongitude(String.valueOf(leku_address.getLongitude()));
            canProceed += 1;
        }

    }

    void pickLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showMessage();
        } else {
            if (!checkPermission()) {
                requestPermission();
            } else {
                getLocation();
            }
        }
    }

    public void showMessage() {
        new MaterialDialog.Builder(this)
                .customView(R.layout.dialog_location_message, false)
                .positiveText("Continue")
                .negativeText("Quit")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        enableGPS();
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                finish();
            }
        })
                .show();
    }

    void enableGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(UserProfileActivity.this).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(UserProfileActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void toVendorPickScreen() {
        Intent intent = new Intent(UserProfileActivity.this, VendorPickActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //pickLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        finish();
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                getLocationFromLeku(data);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(Internet.getInstance().isInternetConnected()) {
            switch (v.getId()) {
                case R.id.update:
                    if (validateSignUpCustomer())
                        updateCustomerDetails();
                    break;
                case R.id.current_location:
                    activateLeku();
                    break;
            }
        } else {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    boolean validateSignUpCustomer(){
        boolean N = true, A = true;
        if(name.getText().toString().isEmpty()){
            name.setBackgroundResource(R.drawable.edit_text_border_error);
            name.setHintTextColor(ContextCompat.getColor(this,R.color.red));
            name.setHint("Enter your name");
            N = false;
        }

        if(address.getText().toString().isEmpty()){
            address.setBackgroundResource(R.drawable.edit_text_border_error);
            address.setHintTextColor(ContextCompat.getColor(this,R.color.red));
            address.setHint("Could not get your address..!");
            A = false;
        }

        if(N && A){
            canProceed += 1;
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static class GPSCoordinates {
        public float longitude = -1;
        public float latitude = -1;

        public GPSCoordinates(float theLongitude, float theLatitude) {
            longitude = theLongitude;
            latitude = theLatitude;
        }

        public GPSCoordinates(double theLongitude, double theLatitude) {
            longitude = (float) theLongitude;
            latitude = (float) theLatitude;
        }
    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            gpsCoordinates = new GPSCoordinates(
                    location.getLongitude(),
                    location.getLatitude()
            ) ;
            getLocationAddress();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void getLocation() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        //        if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION )) {
//            Toast.makeText(getApplicationContext(), "GPS permission allows us to access location data. " +
//                    "Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//        } else {
//
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    getLocationAddress();
                }
                break;
        }
    }

    public void getLocationAddress() {

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation( gpsCoordinates.latitude, gpsCoordinates.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null) {
                Address cl = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < cl.getMaxAddressLineIndex(); i++) {
                    strAddress.append(cl.getAddressLine(i)).append("\n");
                }

                address.setText(strAddress.toString());

                customerLocation.setAddress(strAddress.toString());
                customerLocation.setStreet(cl.getAddressLine(0));
                customerLocation.setCity(cl.getLocality());
                customerLocation.setState(cl.getAdminArea());
                customerLocation.setCountry(cl.getCountryName());
                customerLocation.setPostCode(cl.getPostalCode());
                customerLocation.setLatitude(String.valueOf(cl.getLatitude()));
                customerLocation.setLongitude(String.valueOf(cl.getLongitude()));

                // locationManager.removeUpdates(locationListener);
            }
            else {
                address.setText("No location found...!");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean canAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    void updateCustomerDetails(){
        if(canProceed >= 1) {

            wdProgressDialog.show();
            final UpdateUserReq updateUserReq = new UpdateUserReq();

            updateUserReq.setUser(getUser());
            updateUserReq.setUserLocation(getUserLocation());

            client = new Client();
            Call<UpdateUserRes> call = client.get().UpdateUserDetails(updateUserReq);
            call.enqueue(new Callbacks<UpdateUserRes>() {
                @Override
                public void onResponse(Call<UpdateUserRes> call, Response<UpdateUserRes> updateUserResResponse) {
                    processResponse(updateUserResResponse.body(),updateUserReq);
                    //toVendorPickScreen();
                    wdProgressDialog.dismissWdProgressDialog();
                }
            });
        }
    }

    UpdateUserReq.User getUser(){
        UpdateUserReq.User user = new UpdateUserReq.User();
        user.setName(name.getText().toString());
        user.setId(UserSession.getInstance().getUser_id());
        return user;
    }

    UpdateUserReq.UserLocation getUserLocation(){
        customerLocation.setLandmark(landmark.getText().toString());
        return customerLocation;
    }

    /**
     *
     * @param updateUserRes
     * @param updateUserReq
     */
    void processResponse(UpdateUserRes updateUserRes,UpdateUserReq updateUserReq){
        databaseProcess(updateUserRes,updateUserReq);
    }

    void databaseProcess(UpdateUserRes updateUserRes,UpdateUserReq updateUserReq){
        DatabaseConfig databaseConfig = new DatabaseConfig(this);

        // insert : customer
        ContentValues customer = new ContentValues();
        customer.put(Customer.Column.NAME, updateUserReq.getUser().getName());
        customer.put(Customer.Column.UPDATED_DATE, new Date().toString());


        CustomerModel customerModel = new CustomerModel(databaseConfig);
        String[] whereArgs = new String[] { String.valueOf(updateUserReq.getUser().getId()) };
        customerModel.update(customer, whereArgs );

        // insert : User Address
        ContentValues customerAddress = new ContentValues();
        customerAddress.put(CustomerAddress.Column.LATITUDE,updateUserReq.getUserLocation().getLatitude());
        customerAddress.put(CustomerAddress.Column.LONGITUDE,updateUserReq.getUserLocation().getLongitude());
        customerAddress.put(CustomerAddress.Column.ADDRESS,updateUserReq.getUserLocation().getAddress());
        customerAddress.put(CustomerAddress.Column.STREET,updateUserReq.getUserLocation().getStreet());
        customerAddress.put(CustomerAddress.Column.CITY,updateUserReq.getUserLocation().getCity());
        customerAddress.put(CustomerAddress.Column.STATE,updateUserReq.getUserLocation().getState());
        customerAddress.put(CustomerAddress.Column.COUNTRY,updateUserReq.getUserLocation().getCountry());
        customerAddress.put(CustomerAddress.Column.LANDMARK,updateUserReq.getUserLocation().getLandmark());
        customerAddress.put(CustomerAddress.Column.POSTAL_CODE,updateUserReq.getUserLocation().getPostCode());
        customerAddress.put(CustomerAddress.Column.UPDATED_DATE, new Date().toString());

        CustomerAddressModel customerAddressModel = new CustomerAddressModel(databaseConfig);
        whereArgs = new String[] { String.valueOf(rmt_address_id) };
        customerAddressModel.update(customerAddress,whereArgs);

        // close db
        databaseConfig.close();

        Toast.makeText(this,updateUserRes.getMessage(),Toast.LENGTH_LONG).show();
    }

    public boolean getUserDetail(){
        DatabaseConfig databaseConfig = new DatabaseConfig(this);
        CustomerModel customerModel = new CustomerModel(databaseConfig);
        if(customerModel.getRootUserId() != 0) {
            populateForm( customerModel.getUserById( UserSession.getInstance().getUser_id() ) );
            return true;
        } else {
            return false;
        }
    }

    public void populateForm(User user){
        name.setText(user.getName());
        mobile.setText(user.getMobile_number());
        address.setText(user.getAddress());
        landmark.setText(user.getUserLocation().getLandmark());
        rmt_address_id = user.getRmt_address_id();

        customerLocation.setAddress(user.getUserLocation().getAddress());
        customerLocation.setStreet(user.getUserLocation().getStreet());
        customerLocation.setCity(user.getUserLocation().getCity());
        customerLocation.setState(user.getUserLocation().getState());
        customerLocation.setCountry(user.getUserLocation().getCountry());
        customerLocation.setPostCode(user.getUserLocation().getPostalCode());
        customerLocation.setLandmark(user.getUserLocation().getLandmark());
        customerLocation.setLatitude(String.valueOf(user.getUserLocation().getLatitude()));
        customerLocation.setLongitude(String.valueOf(user.getUserLocation().getLongitude()));

    }

}
