package number.android.waterdrop.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import number.android.waterdrop.R;
import number.android.waterdrop.activities.WaterDropActivity;
import number.android.waterdrop.database.WDSharedPreferences;

/**
 * Created by user on 10/24/2016.
 */
public class WDFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "WDFireBaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        Map<String, String> params = remoteMessage.getData();
        JSONObject object = new JSONObject(params);
        Log.i("JSON_OBJECT", object.toString());

        //Calling method to generate notification
        sendNotification(object);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(JSONObject data) {

        Intent intent;

        String message = "";
        String title = "";
        int type = 1;
        int status = 0;

        try {
            message = data.getString("message");
            title = data.getString("title");
            type = data.getInt("request_type");
            status = data.getInt("status");
        } catch (JSONException e){
            e.printStackTrace();
        }

        switch (type){
            case 1:
                intent = new Intent(this, WaterDropActivity.class);
                intent.putExtra("request_type",1);
                break;
            case 3:
                intent = new Intent(this, WaterDropActivity.class);
                intent.putExtra("request_type",type);
                intent.putExtra("tab",2);
                intent.putExtra("status",status);
                break;
            case 4:
                intent = new Intent(this, WaterDropActivity.class);
                intent.putExtra("request_type",type);
                intent.putExtra("tab",1);
                intent.putExtra("status",status);
                break;
            default:
                intent = new Intent(this, WaterDropActivity.class);
                intent.putExtra("request_type",1);
                break;
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.notification);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(title);
        inboxStyle.setSummaryText(message);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setColor(Color.WHITE)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                //.setStyle(inboxStyle)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
