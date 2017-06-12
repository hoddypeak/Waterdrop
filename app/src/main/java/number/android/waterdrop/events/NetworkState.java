package number.android.waterdrop.events;

/**
 * Created by user on 9/21/2016.
 */
public class NetworkState {

    private boolean mIsInternetConnected;

    public NetworkState(boolean isInternetConnected) {
        this.mIsInternetConnected = isInternetConnected;
    }

    public boolean isInternetConnected() {
        return this.mIsInternetConnected;
    }

}
