package gulak.util;

/**
 * Created by naresh on 4/25/15.
 */

import android.app.IntentService;
import android.content.Intent;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.util.Log;
import android.os.SystemClock;
import android.content.Context;
import android.app.PendingIntent;
import gulak.chittyalpha.MainActivity;
import gulak.chittyalpha.MyChittyActivity;
import gulak.chittyalpha.R;
import android.content.SharedPreferences;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    static final String TAG = "ChittyApp";
    NotificationCompat.Builder builder;
    SharedPreferences prefs;
    NotificationCompat.Builder notification;
    NotificationManager manager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        prefs = getSharedPreferences("Parchi", 0);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString(),"","");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(),"","");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // This loop represents the service doing some work.
                //Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                    sendNotification(extras.getString("fromPhone"), extras.getString("chittyVal"), extras.getString("toPhone"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String fromPhone,String chittyVal, String toPhone) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this,MyChittyActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // I would have to check how i can make different Intent to be invoked for Shop Keeper its Issued Parachi and for customer its MychittyActivity
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.goldcoin)
                        .setContentTitle("Parchi")
                        .setTicker("New Parchi")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(chittyVal))
                        .setContentText(fromPhone + " Parchi Points " + chittyVal);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
