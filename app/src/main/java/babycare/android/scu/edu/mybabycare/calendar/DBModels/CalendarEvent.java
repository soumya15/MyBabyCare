package babycare.android.scu.edu.mybabycare.calendar.DBModels;

/**
 * Created by Soumya on 5/19/2015.
 */
public class CalendarEvent {

    public String getEventDetails() {
        return eventDetails;
    }

    public CalendarEvent(int eventID, String eventName, String eventDate, String eventDetails) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventDetails = eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public int getEventID() {
        return eventID;

    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    int eventID;
    int parentEventID;
    String parentEventName;
    String eventName;

    public int getParentEventID() {
        return parentEventID;
    }

    public void setParentEventID(int parentEventID) {
        this.parentEventID = parentEventID;
    }

    public String getParentEventName() {
        return parentEventName;
    }

    public void setParentEventName(String parentEventName) {
        this.parentEventName = parentEventName;
    }

    public CalendarEvent(String parentEventName, int parentEventID,String eventName, String eventDate) {
        this.parentEventID = parentEventID;
        this.parentEventName = parentEventName;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    String eventDate;
    String eventDetails;
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventString) {
        this.eventDate = eventString;
    }

    public CalendarEvent(String eventName, String eventDate) {
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    public CalendarEvent(int eventID, String eventName, String eventString) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDate = eventString;
    }

    public CalendarEvent() {
    }
}
