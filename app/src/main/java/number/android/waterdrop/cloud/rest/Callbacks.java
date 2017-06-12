package number.android.waterdrop.cloud.rest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public abstract class Callbacks<T> implements Callback<T> {

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if(t instanceof SocketTimeoutException){
            Log.d("SocketTimeoutException","Socket Time out. Please try again.");
        }
    }
}