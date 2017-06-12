package number.android.waterdrop.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import number.android.waterdrop.R;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.adapters.VendorPickListAdapter;
import number.android.waterdrop.adapters.entities.Vendor;
import number.android.waterdrop.cloud.entities.request.SignUpReq;
import number.android.waterdrop.cloud.entities.request.UpdateVendorReq;
import number.android.waterdrop.cloud.entities.request.VendorsReq;
import number.android.waterdrop.cloud.entities.response.SignUpRes;
import number.android.waterdrop.cloud.entities.response.UpdateVendorRes;
import number.android.waterdrop.cloud.entities.response.VendorsRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.database.WDSharedPreferences;
import number.android.waterdrop.database.contracts.Customer;
import number.android.waterdrop.database.contracts.CustomerAddress;
import number.android.waterdrop.database.models.CustomerAddressModel;
import number.android.waterdrop.database.models.CustomerModel;
import number.android.waterdrop.database.models.VendorModel;
import number.android.waterdrop.network.Internet;
import number.android.waterdrop.utilities.Icons;
import number.android.waterdrop.utilities.WDProgressDialog;
import retrofit2.Call;
import retrofit2.Response;

public class VendorPickActivity extends AppCompatActivity implements View.OnClickListener {

    ListView vendors;
    VendorPickListAdapter vendorPickListAdapter;

    List<Vendor> vendorList = new ArrayList<Vendor>();

    TextView btn_continue;

    Icons icons;

    Client client;

    View emptyView;

    WDProgressDialog wdProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_pick);
        icons = new Icons(this);
        wdProgressDialog = new WDProgressDialog(this);

        initView();

    }

    private void initView(){

        btn_continue = (TextView) findViewById(R.id.btn_continue);
        btn_continue.setCompoundDrawables(null,null,icons.arrowRightIcon(),null);
        btn_continue.setOnClickListener(this);

        loadVendorsFromCloud();

        vendors = (ListView) findViewById(R.id.vendors);
        vendors.setEmptyView(emptyView);
        vendorPickListAdapter = new VendorPickListAdapter(this,vendorList);
        vendors.setAdapter(vendorPickListAdapter);
        vendors.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        TextView existing_vendor_intent = (TextView) findViewById(R.id.existing_vendor_intent);
        existing_vendor_intent.setOnClickListener(this);
    }

    void loadVendorsFromCloud(){
        wdProgressDialog.setMessage("Loading...");
        wdProgressDialog.show();

        VendorsReq vendorsReq = new VendorsReq();
        vendorsReq.setUserId(UserSession.getInstance().getUser_id());
        vendorsReq.setLatitude(String.valueOf( UserSession.getInstance().getLatitude() ));
        vendorsReq.setLongitude(String.valueOf( UserSession.getInstance().getLongitude() ));

        client = new Client();
        Call<VendorsRes> call = client.get().Vendors(vendorsReq);
        call.enqueue(new Callbacks<VendorsRes>() {
            @Override
            public void onResponse(Call<VendorsRes> call, Response<VendorsRes> vendorsRes) {
                if (vendorsRes.code() == 200) {
                    processResponse( vendorsRes.body() );
                } else {
                    // TODO: 12/18/2016
                }
                wdProgressDialog.dismissWdProgressDialog();
            }
        });
    }

    /**
     *
     * @param vendorsRes
     */
    void processResponse(VendorsRes vendorsRes){
        for (VendorsRes.Vendor vendor : vendorsRes.getVendors()) {
            Vendor listItem = new Vendor();
            listItem.setId(vendor.getId());
            listItem.setName(vendor.getName());
            listItem.setMobile_number(vendor.getMobile());
            vendorList.add(listItem);
        }
        // update vendor list
        vendorPickListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(Internet.getInstance().isInternetConnected()) {
            switch (v.getId()) {
                case R.id.btn_continue:
                    setDefaultVendor();
                    break;
                case R.id.existing_vendor_intent:
                    toVendorMyContactScreen();
                    break;
            }
        } else {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    public void toOrderScreen(){
        // Snackbar.make(findViewById(android.R.id.content), "Under development", Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(VendorPickActivity.this,WaterDropActivity.class);
        startActivity(intent);
    }

    public void toVendorMyContactScreen(){
        // Snackbar.make(findViewById(android.R.id.content), "Under development", Snackbar.LENGTH_LONG).show();
        Intent intent = new Intent(VendorPickActivity.this,VendorMyContactActivity.class);
        startActivity(intent);
    }

    void setDefaultVendor(){

        wdProgressDialog.setMessage("Processing please wait...");
        wdProgressDialog.show();
        final Vendor vendor = vendorPickListAdapter.getmSelectedVendor();

        UpdateVendorReq updateVendorReq = new UpdateVendorReq();
        updateVendorReq.setUserId(UserSession.getInstance().getUser_id());
        updateVendorReq.setVendorId(vendor.getId());


        client = new Client();
        Call<UpdateVendorRes> call = client.get().UpdateVendor(updateVendorReq);
        call.enqueue(new Callbacks<UpdateVendorRes>() {
            @Override
            public void onResponse(Call<UpdateVendorRes> call, Response<UpdateVendorRes> response) {
                processResponse(response.body(),vendor);
                wdProgressDialog.dismissWdProgressDialog();
            }
        });
    }

    void processResponse(UpdateVendorRes updateVendorRes,Vendor vendor){
        databaseProcess(updateVendorRes,vendor);
    }

    void databaseProcess(UpdateVendorRes updateVendorRes, Vendor vendor){
        DatabaseConfig databaseConfig = new DatabaseConfig(this);

        // insert : vendor
        ContentValues vendorRec = new ContentValues();
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.RMT_ID, vendor.getId());
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.NAME, vendor.getName());
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.MOBILE_NUMBER, vendor.getMobile_number());
        vendorRec.put(number.android.waterdrop.database.contracts.Vendor.Column.STATUS, 1 );

        VendorModel vendorModel = new VendorModel(databaseConfig);
        vendorModel.insert(vendorRec);

        databaseConfig.close();

        Toast.makeText(this,updateVendorRes.getMessage(),Toast.LENGTH_LONG);
        UserSession.getInstance().setVendor_id(vendor.getId());

        WDSharedPreferences preferences = new WDSharedPreferences(this);
        preferences.setVendorStatus(false);

        toOrderScreen();
        finish();
    }
}
