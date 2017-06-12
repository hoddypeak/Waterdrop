package number.android.waterdrop.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.adapters.entities.Vendor;
import number.android.waterdrop.cloud.entities.request.SearchVendorReq;
import number.android.waterdrop.cloud.entities.request.UpdateVendorReq;
import number.android.waterdrop.cloud.entities.response.UpdateVendorRes;
import number.android.waterdrop.cloud.entities.response.VendorsRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.WDSharedPreferences;
import number.android.waterdrop.database.models.VendorModel;
import number.android.waterdrop.network.Internet;
import number.android.waterdrop.utilities.Icons;
import number.android.waterdrop.utilities.WDProgressDialog;
import retrofit2.Call;
import retrofit2.Response;

public class VendorMyContactActivity extends AppCompatActivity implements View.OnClickListener  {

    private static final int CONTACT_PICKER_RESULT = 1001;
    private static final int PERMISSION_REQUEST_CODE = 1;

    TextView btn_continue;
    TextView pick_contact;
    TextView vendor_name_txt,vendor_number_txt;
    EditText mobile_number;
    LinearLayout vendor_detail;
    Icons icons;
    Client client;
    WDProgressDialog wdProgressDialog;

    private int vendor_id = 0;
    private String vendor_name = null;
    private String vendor_mobile_number = null;
    private String vendor_address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_my_contact);
        icons = new Icons(this);
        wdProgressDialog = new WDProgressDialog(this);
        initView();
    }

    private void initView() {

        btn_continue = (TextView) findViewById(R.id.btn_continue);
        btn_continue.setCompoundDrawables(null,null,icons.arrowRightIcon(),null);
        btn_continue.setOnClickListener(this);

        vendor_detail = (LinearLayout) findViewById(R.id.vendor_detail);

        vendor_name_txt = (TextView) findViewById(R.id.vendor_name);
        vendor_number_txt = (TextView) findViewById(R.id.vendor_number);

        mobile_number = (EditText) findViewById(R.id.mobile_number);
        mobile_number.addTextChangedListener(new MobileNumberOnType());

        pick_contact = (TextView) findViewById(R.id.pick_contact);
        pick_contact.setOnClickListener(this);

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public class MobileNumberOnType implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start,
        int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start,
        int before, int count) {
            if(s.length() == 10)
                loadVendorByNumber(s.toString());
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
        if ( ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS )) {

        } else {

        }
    }

    public void launchContactPicker() {
        mobile_number.setEnabled(true);

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        contactPickerIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launchContactPicker();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    setNameAndNumber(intent);
                    break;
            }
        } else {
            Log.w("A", "Warning: activity result not ok");
        }
    }

    void setNameAndNumber(Intent intent){
        Cursor cursor = null;
        try {
            Uri uri = intent.getData();
            String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
            cursor = getContentResolver().query(uri, projection , null, null, null);

            cursor.moveToFirst();
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String name = cursor.getString(nameColumnIndex);

            String phoneNumber = null;
            Cursor phoneCursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER }, ContactsContract.CommonDataKinds.Phone._ID + "= ? ",
                    new String[] { id }, null);
            if (phoneCursor.moveToFirst()) {
                phoneNumber = phoneCursor.getString(phoneCursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phoneCursor.close();

            Log.d("DEBUG_TAG", "number : " + phoneNumber +" , name : "+name);

            if(phoneNumber.length() > 10)
                loadVendorByNumber(phoneNumber.substring(3));
            else
                loadVendorByNumber(phoneNumber);


        } catch (Exception e) {
            Log.e("DEBUG_TAG", "Failed to get data", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    void loadVendorByNumber(String mobileNumber){

        wdProgressDialog.setMessage("Loading...");
        wdProgressDialog.show();

        SearchVendorReq searchVendorReq = new SearchVendorReq();
        searchVendorReq.setMobile(mobileNumber);

        client = new Client();
        Call<VendorsRes> call = client.get().SearchVendor(searchVendorReq);
        call.enqueue(new Callbacks<VendorsRes>() {
            @Override
            public void onResponse(Call<VendorsRes> call, Response<VendorsRes> response) {
                processResponse2(response.body());
                wdProgressDialog.dismissWdProgressDialog();
            }
        });
    }

    void processResponse2(VendorsRes vendorsRes){

         if(vendorsRes.getVendors().size() > 0){
            for (VendorsRes.Vendor vendor : vendorsRes.getVendors()) {
                vendor_id = vendor.getId();
                vendor_name = vendor.getName();
                vendor_mobile_number = vendor.getMobile();
                vendor_address = vendor.getAddress();
            }
            if(vendor_id != 0 && vendor_name != null && vendor_mobile_number != null && vendor_address != null){
                vendor_name_txt.setText( vendor_name );
                vendor_number_txt.setText( vendor_mobile_number.toString() );
                vendor_detail.animate().alpha(1.0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        vendor_detail.setVisibility(View.VISIBLE);
                    }
                });
            }
         } else {
             Toast.makeText(this,"Vendor not found. Please pick any vendor from the previous screen or refer us to your vendor",Toast.LENGTH_LONG).show();
             mobile_number.setEnabled(false);
         }

    }

    @Override
    public void onClick(View v) {
        if(Internet.getInstance().isInternetConnected()) {
            switch (v.getId()) {
                case R.id.btn_continue:
                    if (vendor_id != 0 && vendor_name != null && vendor_mobile_number != null && vendor_address != null) {
                        setDefaultVendor();
                    } else {
                        // TODO: 10/14/2016 show warning
                    }
                    break;
                case R.id.pick_contact: {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        launchContactPicker();
                    }
                }
                break;
            }
        } else {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    void setDefaultVendor(){

        wdProgressDialog.setMessage("Processing please wait...");
        wdProgressDialog.show();

        UpdateVendorReq updateVendorReq = new UpdateVendorReq();
        updateVendorReq.setUserId(UserSession.getInstance().getUser_id());
        updateVendorReq.setVendorId(vendor_id);

        client = new Client();
        Call<UpdateVendorRes> call = client.get().UpdateVendor(updateVendorReq);
        call.enqueue(new Callbacks<UpdateVendorRes>() {
            @Override
            public void onResponse(Call<UpdateVendorRes> call, Response<UpdateVendorRes> response) {
                processResponse(response.body());
                wdProgressDialog.dismissWdProgressDialog();
            }

        });
    }

    void processResponse(UpdateVendorRes updateVendorRes){
        databaseProcess(updateVendorRes);
    }

    void databaseProcess(UpdateVendorRes updateVendorRes){
        DatabaseConfig databaseConfig = new DatabaseConfig(this);

        // insert : vendor
        ContentValues vendorRec = new ContentValues();
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.RMT_ID, vendor_id);
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.NAME, vendor_name);
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.MOBILE_NUMBER, vendor_address);
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.STATUS, 1 );

        VendorModel vendorModel = new VendorModel(databaseConfig);
        vendorModel.insert(vendorRec);

        databaseConfig.close();

        WDSharedPreferences preferences = new WDSharedPreferences(this);
        preferences.setVendorStatus(false);

        Toast.makeText(this,updateVendorRes.getMessage(),Toast.LENGTH_LONG);
        toOrderScreen();
    }

    public void toOrderScreen(){
        // Snackbar.make(findViewById(android.R.id.content), "Under development", Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(VendorMyContactActivity.this,WaterDropActivity.class);
        startActivity(intent);
    }
}
