package babycare.android.scu.edu.mybabycare.notification.utils;

/**
 * Created by Soumya on 5/20/2015.
 */

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;


public class ScheduleService extends Service {

    public class ServiceBinder extends Binder {
        ScheduleService getService() {
            return ScheduleService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ScheduleService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients. See
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Show an alarm for a certain date when the alarm is called it will pop up a notification
     */
    public void setNotification(Calendar c, String title, String message) {
        // This starts a new thread to set the notification
        // You want to push off your tasks onto a new thread to free up the UI to carry on responding
        new NotificationThread(this, c, title, message).run();
    }
}