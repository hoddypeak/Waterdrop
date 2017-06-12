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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.schibstedspain.leku.LocationPickerActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.DeviceToken;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.cloud.entities.request.SignUpReq;
import number.android.waterdrop.cloud.entities.response.SampleResponse;
import number.android.waterdrop.cloud.entities.response.SignUpRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.WDSharedPreferences;
import number.android.waterdrop.database.contracts.Customer;
import number.android.waterdrop.database.contracts.CustomerAddress;
import number.android.waterdrop.database.models.CustomerAddressModel;
import number.android.waterdrop.database.models.CustomerModel;
import number.android.waterdrop.events.FragmentReloadEvent;
import number.android.waterdrop.network.Internet;
import number.android.waterdrop.network.NetworkState;
import number.android.waterdrop.utilities.Icons;
import number.android.waterdrop.utilities.WDProgressDialog;
import retrofit2.Call;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnFocusChangeListener {

    Icons icons;

    EditText name, mobile, address, landmark;
    TextView proceed;
    WDProgressDialog wdProgressDialog;

    private static final int PERMISSION_REQUEST_CODE = 1;
    final static int REQUEST_LOCATION = 199;

    GoogleApiClient googleApiClient;

    LocationManager locationManager;
    GPSCoordinates gpsCoordinates;

    SignUpReq.UserLocation customerLocation;

    Client client;

    int canProceed = 0;
    WDSharedPreferences wdSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        icons = new Icons(this);

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        wdSharedPreferences = new WDSharedPreferences(this);

        /*
         *  Setup progress dialog
         */
        wdProgressDialog = new WDProgressDialog(this);
        wdProgressDialog.setMessage("Signing up...");

        initView();
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

    @Subscribe
    public void onFragmentReloadEvent(FragmentReloadEvent event){
        Toast.makeText(this, "L"+event.getTokenId() , Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        name = (EditText) findViewById(R.id.user_name);
        name.setCompoundDrawables(icons.personIcon(), null, null, null);

        mobile = (EditText) findViewById(R.id.mobile_number);
        mobile.setCompoundDrawables(icons.mobileIcon(), null, null, null);

        address = (EditText) findViewById(R.id.address);
        address.setCompoundDrawables(icons.homeIcon(), null, null, null);
        address.setOnFocusChangeListener(this);

        landmark = (EditText) findViewById(R.id.landmark);
        landmark.setCompoundDrawables(icons.personIcon(), null, null, null);

        proceed = (TextView) findViewById(R.id.proceed);
        proceed.setCompoundDrawables(null, null, icons.arrowRightIcon(), null);
        proceed.setCompoundDrawablePadding(5);
        proceed.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showMessage();
        }

        //pickLocation();
    }

    void activateLeku(){
        Intent intent = new Intent(this, LocationPickerActivity.class);
        intent.putExtra(LocationPickerActivity.BACK_PRESSED_RETURN_OK, true);
        startActivityForResult(intent, 1);
    }

    void getLocationFromLeku(Intent data){

        customerLocation = new SignUpReq.UserLocation();
        Address leku_address = data.getParcelableExtra(LocationPickerActivity.ADDRESS);

        if (leku_address != null) {
            StringBuilder strAddress = new StringBuilder();
            for (int i = 0; i < leku_address.getMaxAddressLineIndex(); i++) {
                strAddress.append(leku_address.getAddressLine(i)).append("\n");
            }
            address.setText(strAddress.toString());

            customerLocation = new SignUpReq.UserLocation();
            customerLocation.setAddress(strAddress.toString());
            customerLocation.setStreet(leku_address.getAddressLine(0));
            customerLocation.setCity(leku_address.getLocality());
            customerLocation.setState(leku_address.getAdminArea());
            customerLocation.setCountry(leku_address.getCountryName());
            customerLocation.setPostalCode(leku_address.getPostalCode());
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
                getGPSLocation();
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
                        dialog.dismiss();
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
                    .addOnConnectionFailedListener(SignupActivity.this).build();
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
                                status.startResolutionForResult(SignupActivity.this, REQUEST_LOCATION);
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
        Intent intent = new Intent(SignupActivity.this, VendorPickActivity.class);
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
                case R.id.proceed:
                    if (validateSignUpCustomer())
                        signUpCustomer();
                    break;
            }
        } else {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    boolean validateSignUpCustomer(){
        boolean N = true, M = true, A = true;
        if(name.getText().toString().isEmpty()){
            name.setBackgroundResource(R.drawable.edit_text_border_error);
            name.setHintTextColor(ContextCompat.getColor(this,R.color.red));
            name.setHint("Enter your name");
            N = false;
        }

        if(mobile.getText().toString().isEmpty()){
            mobile.setBackgroundResource(R.drawable.edit_text_border_error);
            mobile.setHintTextColor(ContextCompat.getColor(this,R.color.red));
            mobile.setHint("Enter mobile number");
            M = false;
        } else if (mobile.getText().toString().length() != 10) {
            mobile.setBackgroundResource(R.drawable.edit_text_border_error);
            mobile.setHintTextColor(ContextCompat.getColor(this,R.color.red));
            mobile.setHint("Enter valid mobile number");
            M = false;
        }

        if(address.getText().toString().isEmpty()){
            address.setBackgroundResource(R.drawable.edit_text_border_error);
            address.setHintTextColor(ContextCompat.getColor(this,R.color.red));
            address.setHint("Could not get your address..!");
            A = false;
        }

        if(N && M && A && validateDeviceToken()){
            canProceed += 1;
            return true;

        } else {
            return false;
        }
    }

    boolean validateDeviceToken() {
        wdSharedPreferences = new WDSharedPreferences(this);
        if(wdSharedPreferences.getDeviceToken() != null && wdSharedPreferences.getDeviceToken() != "") {
            return true;
        } else {
            Toast.makeText(this,"Device token empty",Toast.LENGTH_LONG).show();
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()){
            case R.id.address:
                if (hasFocus)
                    activateLeku();
                break;
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

    private void getGPSLocation() {
        Toast.makeText(this,"Loading your address...",Toast.LENGTH_LONG).show();
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        } else {
            requestPermission();
        }
    }

    private void getNetworkLocation() {
        Toast.makeText(this,"Retrying to load your address...",Toast.LENGTH_LONG).show();
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getGPSLocation();
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

                customerLocation = new SignUpReq.UserLocation();
                customerLocation.setAddress(strAddress.toString());
                customerLocation.setStreet(cl.getAddressLine(0));
                customerLocation.setCity(cl.getLocality());
                customerLocation.setState(cl.getAdminArea());
                customerLocation.setCountry(cl.getCountryName());
                customerLocation.setPostalCode(cl.getPostalCode());
                customerLocation.setLatitude(String.valueOf(cl.getLatitude()));
                customerLocation.setLongitude(String.valueOf(cl.getLongitude()));

                // locationManager.removeUpdates(locationListener);
            }
            else {
                address.setBackgroundResource(R.drawable.edit_text_border_error);
                address.setHintTextColor(ContextCompat.getColor(this,R.color.red));
                address.setHint("Could not get your address..!");

                getNetworkLocation();
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not get your address..!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean canAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    void signUpCustomer(){
        if(canProceed >= 2) {
            wdProgressDialog.show();
            final SignUpReq signUpReq = new SignUpReq();

            signUpReq.setUser(getUser());
            signUpReq.setUserDevice(getUserDevice());
            signUpReq.setUserLocation(getUserLocation());

            client = new Client();
            Call<SignUpRes> call = client.get().SignUp(signUpReq);
            call.enqueue(new Callbacks<SignUpRes>() {
                @Override
                public void onResponse(Call<SignUpRes> call, Response<SignUpRes> signUpRes) {
                    if( signUpRes.code() == 201) {
                        Log.i("Response", signUpRes.body().toString());
                        processResponse(signUpRes.body(), signUpReq);
                    } else {
                        Toast.makeText(SignupActivity.this,"Server error please try later",Toast.LENGTH_LONG).show();
                    }
                    //toVendorPickScreen();

                }
            });
        } else {
            Toast.makeText(SignupActivity.this,"Insufficient data",Toast.LENGTH_LONG).show();
        }
    }

    SignUpReq.User getUser(){
        SignUpReq.User user = new SignUpReq.User();
        user.setName(name.getText().toString());
        user.setMobileNumber(mobile.getText().toString());
        return user;
    }

    SignUpReq.UserDevice getUserDevice(){
        SignUpReq.UserDevice userDevice = new SignUpReq.UserDevice();
        userDevice.setDeviceOs("A");
        userDevice.setDeviceOsVersion(Build.VERSION.RELEASE);
        userDevice.setDeviceToken(wdSharedPreferences.getDeviceToken());
        return userDevice;
    }

    SignUpReq.UserLocation getUserLocation(){
        customerLocation.setLandmark(landmark.getText().toString());
        customerLocation.setCity("Chennai");
        return customerLocation;
    }

    /**
     *
     * @param signUpRes
     */
    void processResponse(SignUpRes signUpRes,SignUpReq signUpReq){
        databaseProcess(signUpRes,signUpReq);
    }

    void databaseProcess(SignUpRes signUpRes,SignUpReq signUpReq){
        DatabaseConfig databaseConfig = new DatabaseConfig(this);

        // insert : customer
        ContentValues customer = new ContentValues();
        customer.put(Customer.Column.RMT_ID, signUpRes.getUserId());
        customer.put(Customer.Column.NAME, signUpReq.getUser().getName());
        customer.put(Customer.Column.MOBILE_NUMBER, signUpReq.getUser().getMobileNumber());
        customer.put(Customer.Column.CUSTOMER_CODE, signUpRes.getUserCode());
        customer.put(Customer.Column.RMT_ADDRESS_ID, 1 );
        customer.put(Customer.Column.STATUS, 1 );
        customer.put(Customer.Column.PARENT_ID, 0 );
        customer.put(Customer.Column.CREATED_DATE, new Date().toString());
        customer.put(Customer.Column.UPDATED_DATE, new Date().toString());

        CustomerModel customerModel = new CustomerModel(databaseConfig);
        long customer_id = customerModel.insert(customer);

        // insert : User Address
        ContentValues customerAddress = new ContentValues();
        customerAddress.put(CustomerAddress.Column.RMT_ID,1);
        customerAddress.put(CustomerAddress.Column.LATITUDE,signUpReq.getUserLocation().getLatitude());
        customerAddress.put(CustomerAddress.Column.LONGITUDE,signUpReq.getUserLocation().getLongitude());
        customerAddress.put(CustomerAddress.Column.ADDRESS,signUpReq.getUserLocation().getAddress());
        customerAddress.put(CustomerAddress.Column.STREET,signUpReq.getUserLocation().getStreet());
        customerAddress.put(CustomerAddress.Column.CITY,signUpReq.getUserLocation().getCity());
        customerAddress.put(CustomerAddress.Column.STATE,signUpReq.getUserLocation().getState());
        customerAddress.put(CustomerAddress.Column.COUNTRY,signUpReq.getUserLocation().getCountry());
        customerAddress.put(CustomerAddress.Column.POSTAL_CODE,signUpReq.getUserLocation().getPostalCode());
        customerAddress.put(CustomerAddress.Column.STATUS,1);
        customerAddress.put(CustomerAddress.Column.CREATED_DATE, new Date().toString());
        customerAddress.put(CustomerAddress.Column.UPDATED_DATE, new Date().toString());

        CustomerAddressModel customerAddressModel = new CustomerAddressModel(databaseConfig);
        customerAddressModel.insert(customerAddress);

        databaseConfig.close();

        UserSession.getInstance().setUser_id(signUpRes.getUserId());
        UserSession.getInstance().setLatitude(Double.parseDouble(signUpReq.getUserLocation().getLatitude()));
        UserSession.getInstance().setLongitude(Double.parseDouble(signUpReq.getUserLocation().getLongitude()));

        wdProgressDialog.dismissWdProgressDialog();
        toVendorPickScreen();
        finish();
    }

}
