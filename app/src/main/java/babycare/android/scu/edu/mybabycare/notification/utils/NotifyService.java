package babycare.android.scu.edu.mybabycare.notification.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.notification.Activities.NotificationActivity;

/**
 * Created by Soumya on 5/20/2015.
 */
public class NotifyService extends Service {

    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 222;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "intent_notify";

    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_MESSAGE = "message";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification(intent.getStringExtra(NOTIFICATION_TITLE),intent.getStringExtra(NOTIFICATION_MESSAGE));

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification(String title,String message) {

        // This is the icon to use on the notification
        int icon = android.R.drawable.ic_dialog_alert;
        // What time to show on the notification
        long time = System.currentTimeMillis();

        Notification notification = new Notification(icon, message, time);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Notification.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, title, message, contentIntent);

        // Clear the notification when it is pressed
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Send the notification to the system.
        mNM.notify(NOTIFICATION, notification);
        Log.i("NotifyService", "Alarm!!");

        // Stop the service when we are finished
        stopSelf();
    }
}