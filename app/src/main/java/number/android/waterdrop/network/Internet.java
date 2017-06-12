package number.android.waterdrop.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by user on 12/25/2016.
 */
public class Internet {

    private static Internet ourInstance = new Internet();

    public static Internet getInstance() {
        return ourInstance;
    }

    private static boolean internetConnectionStatus;

    private Internet() {
    }

    public static boolean isInternetConnected() {
        return internetConnectionStatus;
    }

    public static void setInternetConnectionStatus(boolean internetConnectionStatus) {
        Internet.internetConnectionStatus = internetConnectionStatus;
    }

    public void checkConnection (Context context){
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            Internet.getInstance().setInternetConnectionStatus(true);
        } else {
            Internet.getInstance().setInternetConnectionStatus(false);
        }
    }
}
