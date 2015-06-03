package babycare.android.scu.edu.mybabycare.notification.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Soumya on 5/20/2015.
 */

public class NotificationThread implements Runnable{
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;
    private final String title;
    private final String message;

    public NotificationThread(Context context, Calendar date, String title, String message) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.title = title;
        this.message = message;
    }

    @Override
    public void run() {

        long notifyTime = date.getTimeInMillis();
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra(NotifyService.NOTIFICATION_TITLE, title);
        intent.putExtra(NotifyService.NOTIFICATION_MESSAGE, message);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        Log.d("time set in millis: ",""+notifyTime);
        Log.d("time set in millis: ",""+notifyTime);
        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
    }
}