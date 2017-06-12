package number.android.waterdrop.services;


import android.util.Log;

import com.google.android.gms.games.event.Event;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.greenrobot.eventbus.EventBus;

import number.android.waterdrop.activities.entities.DeviceToken;
import number.android.waterdrop.database.WDSharedPreferences;
import number.android.waterdrop.events.FragmentReloadEvent;

public class WDFireBaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "WaterDropFireBaseID";

    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        saveToken(refreshedToken);
    }

    private void saveToken(String token) {
        DeviceToken.getInstance().setToken(token);

        WDSharedPreferences wdSharedPreferences = new WDSharedPreferences(this);
        wdSharedPreferences.setDeviceToken(token);
    }

}
