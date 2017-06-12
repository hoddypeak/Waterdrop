package number.android.waterdrop.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import number.android.waterdrop.R;
import number.android.waterdrop.activities.VendorPickActivity;
import number.android.waterdrop.activities.WaterDropActivity;
import number.android.waterdrop.activities.entities.UserSession;
import number.android.waterdrop.cloud.entities.request.OrderReq;
import number.android.waterdrop.cloud.entities.request.SignUpReq;
import number.android.waterdrop.cloud.entities.response.OrderRes;
import number.android.waterdrop.cloud.entities.response.SignUpRes;
import number.android.waterdrop.cloud.rest.Callbacks;
import number.android.waterdrop.cloud.rest.Client;
import number.android.waterdrop.database.DatabaseConfig;
import number.android.waterdrop.network.Internet;
import number.android.waterdrop.utilities.Icons;
import number.android.waterdrop.utilities.WDProgressDialog;
import retrofit2.Call;
import retrofit2.Response;

public class OrderFragment extends Fragment implements View.OnClickListener {

    View view;

    TextView quantity,btn_order_now,verses;
    ImageView plus,minus;
    WDProgressDialog wdProgressDialog;

    Icons icons;

    Client client;

    String[] verses_array;

    public int QUANTITY = 1;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order, container, false);
        icons = new Icons(getActivity());

        /*
         *  Setup progress dialog
         */
        wdProgressDialog = new WDProgressDialog(getActivity());
        wdProgressDialog.setMessage("Ordering...");

        initView(view);

        return view;
    }

    private void initView(View view){

        int min = 0;
        int max = 6;

        Random r = new Random();
        int i1 = r.nextInt(max - min + 1) + min;

        Resources res = getResources();
        verses_array = res.getStringArray(R.array.verses);

        plus = (ImageView) view.findViewById(R.id.plus);
        plus.setOnClickListener(this);

        verses = (TextView) view.findViewById(R.id.verses);
        verses.setText(verses_array[i1]);

        quantity = (TextView) view.findViewById(R.id.quantity);

        minus = (ImageView) view.findViewById(R.id.minus);
        minus.setOnClickListener(this);

        btn_order_now = (TextView) view.findViewById(R.id.btn_order_now);
        btn_order_now.setCompoundDrawables(null,null,icons.arrowRightIcon(),null);
        btn_order_now.setOnClickListener(this);

        if( ! UserSession.getInstance().isVendor_status() ){
            Toast.makeText(getActivity(),"Waiting for vendor approval",Toast.LENGTH_LONG).show();
            plus.setEnabled(false);
            minus.setEnabled(false);
            btn_order_now.setClickable(false);
        } else {
            plus.setEnabled(true);
            minus.setEnabled(true);
            btn_order_now.setClickable(true);
        }

        checkOpenOrderStatus();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.plus:
                incrementQuantityBy(1);
                break;
            case R.id.minus:
                decrementQuantityBy(1);
                break;
            case R.id.btn_order_now:
                if( checkOpenOrderStatus() && Internet.getInstance().isInternetConnected() ){
                    sendOrder();
                } else {
                    Toast.makeText(getActivity(), "Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    void incrementQuantityBy(int q){
        if(QUANTITY < 10 ) {
            QUANTITY = QUANTITY + q;
            setQuantityTextView(QUANTITY);
        } else {
            Snackbar.make(view, "Max 10 quantity. Please contact your supplier for bulk orders. Thank you :) ", Snackbar.LENGTH_LONG).show();
        }
    }

    void decrementQuantityBy(int q){
        if(QUANTITY > 1 ) {
            QUANTITY = QUANTITY - q;
            setQuantityTextView(QUANTITY);
        } else {
            Snackbar.make(view, "Minimum 1 quantity. Please :)", Snackbar.LENGTH_LONG).show();
        }
    }

    void setQuantityTextView(int Q){
        String q = String.valueOf(Q);
        quantity.setText(q);
    }

    void resetQuantityTextView(){
        String q = String.valueOf(1);
        quantity.setText(q);
        QUANTITY = 1;
    }

    public void sendOrder(){
        wdProgressDialog.show();
        btn_order_now.setEnabled(false);

        OrderReq orderReq = new OrderReq();
        orderReq.setUserId(UserSession.getInstance().getUser_id());
        orderReq.setQuantity(QUANTITY);
        orderReq.setProductId(1);
        orderReq.setVendorId(UserSession.getInstance().getVendor_id());

        client = new Client();
        Call<OrderRes> call = client.get().Order(orderReq);
        call.enqueue(new Callbacks<OrderRes>() {
            @Override
            public void onResponse(Call<OrderRes> call, Response<OrderRes> orderResResponse) {
                if( orderResResponse.code() == 201 ) {
                    processResponse(orderResResponse.body().getMessage());
                } else {
                    Toast.makeText(getActivity(), "Something went wrong" , Toast.LENGTH_LONG).show();
                }
                btn_order_now.setEnabled(true);
            }
        });
    }

    void processResponse(String message){
        wdProgressDialog.dismissWdProgressDialog();
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        resetQuantityTextView();
    }

    boolean checkOpenOrderStatus(){
        if(UserSession.getInstance().getOpen_order_count() > 2) {
            btn_order_now.setEnabled(false);
            Toast.makeText(getActivity(),"Close the open orders",Toast.LENGTH_LONG).show();
            return false;
        } else {
            btn_order_now.setEnabled(true);
            return true;
        }

    }
}
