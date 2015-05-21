package babycare.android.scu.edu.mybabycare.shopping.locationreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import babycare.android.scu.edu.mybabycare.shopping.Activities.AddNewItem;
import babycare.android.scu.edu.mybabycare.CommonConstants;

/**
 * Created by akshu on 5/19/15.
 */

public class ProximityLocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        if(extras != null) {
            int item_id = extras.getInt(CommonConstants.ITEM_ID);
            String item_name = extras.getString(CommonConstants.ITEM_NAME);
            Log.i("Setting notification","You are near your store to pick up item: " + item_name);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(android.support.v7.appcompat.R.drawable.abc_ic_search_api_mtrl_alpha);
            mBuilder.setContentTitle("Notification for Baby App");
            mBuilder.setContentText("You are near your store to pick up item: "+ item_name);

            /* Creates an explicit intent for an Activity in your app */
            Intent resultIntent = new Intent(context, AddNewItem.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(AddNewItem.class);

            /* Adds the Intent that starts the Activity to the top of the stack */
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(item_id, mBuilder.build());
        }

    }

}