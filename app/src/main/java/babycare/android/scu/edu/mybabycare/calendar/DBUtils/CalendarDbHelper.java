package babycare.android.scu.edu.mybabycare.calendar.DBUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import babycare.android.scu.edu.mybabycare.calendar.DBModels.CalendarEvent;

/**
 * Created by Soumya on 5/11/2015.
 */
public class CalendarDbHelper extends SQLiteOpenHelper {
    public static final String EVENT_ID = "event_id";
    public static final String EVENT_NAME = "event_name";
    public static final String PARENT_EVENT_ID = "parent_event_id";
    public static final String PARENT_EVENT_NAME = "parent_event_name";
    public static final String EVENT_DATE = "event_date";
    public static final String EVENT_DETAILS = "event_details";
    public static final String DATABASE_TABLE = "calendar_events";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "baby_care";
    public static SQLiteDatabase db = null;


    private static final String DATABASE_CREATE = String.format("CREATE TABLE IF NOT EXISTS %s (" +    "  %s integer primary key autoincrement, " +    "  %s integer,"+    "  %s text,"+    "  %s text,"+    "  %s text,"+    "  %s varchar) ",DATABASE_TABLE, EVENT_ID, PARENT_EVENT_ID, PARENT_EVENT_NAME, EVENT_NAME, EVENT_DATE,EVENT_DETAILS);
    public CalendarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
        onCreate(db);
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

    //add a new calendarEvent
    public CalendarEvent addEvent(CalendarEvent calendarEvent) {
        db = this.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(CalendarDbHelper.PARENT_EVENT_ID, calendarEvent.getParentEventID());
        newValues.put(CalendarDbHelper.PARENT_EVENT_NAME, calendarEvent.getParentEventName());
        newValues.put(CalendarDbHelper.EVENT_NAME, calendarEvent.getEventName());
        newValues.put(CalendarDbHelper.EVENT_DATE, calendarEvent.getEventDate());
        newValues.put(CalendarDbHelper.EVENT_DETAILS, calendarEvent.getEventDetails());

        long row = db.insert(CalendarDbHelper.DATABASE_TABLE, null, newValues);
        System.out.println("rows inserted"+row);
        db.close();
        return calendarEvent;
    }
    //update calendarEvent details
    public void updatecalendarEvent(CalendarEvent calendarEvent) {
        db = this.getWritableDatabase();
        String whereClause = CalendarDbHelper.EVENT_ID + "="+calendarEvent.getEventID();
        ContentValues newValues = new ContentValues();
        newValues.put(CalendarDbHelper.EVENT_NAME, calendarEvent.getEventName());
        newValues.put(CalendarDbHelper.EVENT_DATE, calendarEvent.getEventDate());
        newValues.put(CalendarDbHelper.EVENT_DETAILS, calendarEvent.getEventDetails());
        int i = db.update(CalendarDbHelper.DATABASE_TABLE, newValues, whereClause, null);
        db.close();
    }


    // get calendarEvent by id
    public CalendarEvent getCalendarEventById(int id) {
        String whereClause = CalendarDbHelper.EVENT_ID + "="+id;
        CalendarEvent calendarEvent = null;
        db = this.getWritableDatabase();
        String[] resultColumns = {EVENT_ID, EVENT_NAME, EVENT_DATE,EVENT_DETAILS,PARENT_EVENT_ID, PARENT_EVENT_NAME};
        Cursor cursor = db.query(CalendarDbHelper.DATABASE_TABLE, resultColumns, whereClause, null, null, null, null);

        if(cursor == null){
            return calendarEvent;
        }
        while (cursor.moveToNext()) {
            calendarEvent = new CalendarEvent();
            calendarEvent.setEventID(cursor.getInt(0));
            calendarEvent.setEventName(cursor.getString(1));
            calendarEvent.setEventDate(cursor.getString(2));
            calendarEvent.setEventDetails(cursor.getString(3));
            calendarEvent.setParentEventID(cursor.getInt(4));
            calendarEvent.setParentEventName(cursor.getString(4));

        }
        return calendarEvent;
    }

    // get calendarEvent by id
    public CalendarEvent getCalendarEventByParentId(int id,String parentEventName) {
        String whereClause = CalendarDbHelper.PARENT_EVENT_ID + "="+id+" OR "+CalendarDbHelper.PARENT_EVENT_NAME + " = "+parentEventName;
        CalendarEvent calendarEvent = null;
        db = this.getWritableDatabase();
        String[] resultColumns = {EVENT_ID, EVENT_NAME, EVENT_DATE,EVENT_DETAILS,PARENT_EVENT_ID, PARENT_EVENT_NAME};
        Cursor cursor = db.query(CalendarDbHelper.DATABASE_TABLE, resultColumns, whereClause, null, null, null, null);

        if(cursor == null){
            return calendarEvent;
        }
        while (cursor.moveToNext()) {
            calendarEvent = new CalendarEvent();
            calendarEvent.setEventID(cursor.getInt(0));
            calendarEvent.setEventName(cursor.getString(1));
            calendarEvent.setEventDate(cursor.getString(2));
            calendarEvent.setEventDetails(cursor.getString(3));
            calendarEvent.setParentEventID(cursor.getInt(4));
            calendarEvent.setParentEventName(cursor.getString(5));
        }
        return calendarEvent;
    }

    // get all calendarEvents matching search text
    public List<CalendarEvent> getAllcalendarEvents(String searchText) {
        List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
        // to be modified
        String whereClause = null;
        if(!searchText.equalsIgnoreCase("")){
            //whereClause = CalendarDbHelper.PRODUCT_NAME + "="+searchText;
        }
        CalendarEvent calendarEvent = null;
        db = this.getWritableDatabase();
        String[] resultColumns = {EVENT_ID, EVENT_NAME, EVENT_DATE,EVENT_DETAILS};
        Cursor cursor = db.query(CalendarDbHelper.DATABASE_TABLE, resultColumns, whereClause, null, null, null, null);

        if(cursor == null){
            return calendarEvents;
        }
        while (cursor.moveToNext()) {
            calendarEvent = new CalendarEvent();
            calendarEvent.setEventID(cursor.getInt(0));
            calendarEvent.setEventName(cursor.getString(1));
            calendarEvent.setEventDate(cursor.getString(2));
            calendarEvent.setEventDetails(cursor.getString(3));
            calendarEvents.add(calendarEvent);
        }
        System.out.println("calendarEvents recieved : "+calendarEvents.size());
        return calendarEvents;
    }

    public List<CalendarEvent> findNumberOfEventsPerMonth(int year,int month) {
        List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
        String whereClause = CalendarDbHelper.EVENT_DATE + " LIKE '"+month+"/%/"+year+"'";
        CalendarEvent calendarEvent = null;
        try {
            db = this.getWritableDatabase();
            String[] resultColumns = {EVENT_ID, EVENT_NAME, EVENT_DATE, EVENT_DETAILS};
            Cursor cursor = db.query(CalendarDbHelper.DATABASE_TABLE, resultColumns, whereClause, null, null, null, null);

            if (cursor == null) {
                return calendarEvents;
            }
            while (cursor.moveToNext()) {
                calendarEvent = new CalendarEvent();
                calendarEvent.setEventID(cursor.getInt(0));
                calendarEvent.setEventName(cursor.getString(1));
                calendarEvent.setEventDate(cursor.getString(2));
                calendarEvent.setEventDetails(cursor.getString(3));
                calendarEvent.setParentEventID(cursor.getInt(4));
                calendarEvent.setParentEventName(cursor.getString(4));
                calendarEvents.add(calendarEvent);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println("calendarEvents recieved : " + calendarEvents.size());
        return calendarEvents;
    }
}
