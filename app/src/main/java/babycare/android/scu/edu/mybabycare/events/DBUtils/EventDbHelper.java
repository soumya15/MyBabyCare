package babycare.android.scu.edu.mybabycare.events.DBUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import babycare.android.scu.edu.mybabycare.events.DBModels.Event;
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
    //get All events
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<Event>();
        Event event = null;
        db = this.getWritableDatabase();
        String[] resultColumns = {EVENT_ID, EVENT_NAME, EVENT_DATE, EVENT_DESC};
        Cursor cursor = db.query(EventDbHelper.DATABASE_TABLE, resultColumns, null, null, null, null, null);

        if(cursor == null){
            return events;
        }
        while (cursor.moveToNext()) {
            event = new Event();
            event.setEventId(cursor.getInt(0));
            event.setEventName(cursor.getString(1));
            event.setEventDate(cursor.getString(2));
            event.setEventDetails(cursor.getString(3));

            events.add(event);
        }
        return events;
    }

    //Delete Event

    public void deleteEvent(Integer eventId) {
        db = this.getWritableDatabase();
        String whereClause = EventDbHelper.EVENT_ID + "="+eventId;
        db.delete(EventDbHelper.DATABASE_TABLE,whereClause,null);
        db.close();
    }

    //update Event details
    public void updateEvent(Event event) {
        db = this.getWritableDatabase();
        String whereClause = EventDbHelper.EVENT_ID + "="+event.getEventId();
        ContentValues newValues = new ContentValues();
        newValues.put(EventDbHelper.EVENT_NAME, event.getEventName());
        newValues.put(EventDbHelper.EVENT_DATE, event.getEventDate());
        newValues.put(EventDbHelper.EVENT_DESC, event.getEventDetails());

        int i = db.update(EventDbHelper.DATABASE_TABLE, newValues, whereClause, null);

        db.close();
    }

}
