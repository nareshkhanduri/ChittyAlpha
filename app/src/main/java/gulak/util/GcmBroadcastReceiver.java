package gulak.util;

/**
 * Created by naresh on 4/25/15.
 */
import android.support.v4.content.WakefulBroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ComponentName;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.

        System.out.println("GCM Boradcast Recivers");
        Bundle extras = intent.getExtras();
        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("fromPhone", extras.getString("fromPhone"));
        msgrcv.putExtra("chittyVal", extras.getString("chittyVal"));
        msgrcv.putExtra("toPhone", extras.getString("toPhone"));
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        System.out.println("GCM Boradcast Recivers Context " + context);
        setResultCode(Activity.RESULT_OK);
        System.out.println("GCM Boradcast Recivers intent " + intent);
    }
}
