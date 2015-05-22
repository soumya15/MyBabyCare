package babycare.android.scu.edu.mybabycare.checklist.DBUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import babycare.android.scu.edu.mybabycare.checklist.DBModels.Event;

/**
 * Created by akshu on 5/21/15.
 */
public class EventDbHelper extends SQLiteOpenHelper {
        public static final String EVENT_ID = "event_id";
        public static final String EVENT_NAME = "event_name";
        public static final String EVENT_DATE = "event_date";
        public static final String EVENT_DESC = "event_description";
        public static final String DATABASE_TABLE = "event_details";
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "baby_care";
        public static SQLiteDatabase db = null;


        private static final String DATABASE_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s (" + "  %s integer primary key autoincrement, " + "  %s text," + "  %s text," + "  %s text)", DATABASE_TABLE, EVENT_ID, EVENT_NAME, EVENT_DATE, EVENT_DESC);

        public EventDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            db = this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

        //add a new event
        public boolean addEvent(Event event) {
            db = this.getWritableDatabase();
            ContentValues newEvent = new ContentValues();
            newEvent.put(EventDbHelper.EVENT_NAME, (event.getEventName()));
            newEvent.put(EventDbHelper.EVENT_DATE, event.getEventDate());
            newEvent.put(EventDbHelper.EVENT_DESC, event.getEventDetails());
            long row = db.insert(EventDbHelper.DATABASE_TABLE, null, newEvent);
            System.out.println("Event row inserted"+row);
            db.close();
            return true;
        }

}
