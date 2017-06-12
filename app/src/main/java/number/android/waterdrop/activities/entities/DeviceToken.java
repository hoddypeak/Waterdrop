package number.android.waterdrop.activities.entities;

/**
 * Created by user on 12/4/2016.
 */
public class DeviceToken {

    private static DeviceToken ourInstance = new DeviceToken();

    public static DeviceToken getInstance() {
        return ourInstance;
    }

    String token;

    private DeviceToken() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
