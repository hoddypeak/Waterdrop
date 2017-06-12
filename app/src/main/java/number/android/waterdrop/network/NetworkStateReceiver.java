package number.android.waterdrop.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getExtras() != null) {
            ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                // there is Internet connection
                EventBus.getDefault().postSticky(new NetworkState(true));
                Internet.getInstance().setInternetConnectionStatus(true);

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                // no Internet connection, send network state changed
                EventBus.getDefault().postSticky(new NetworkState(false));
                Internet.getInstance().setInternetConnectionStatus(false);

            }
        }
    }

}
