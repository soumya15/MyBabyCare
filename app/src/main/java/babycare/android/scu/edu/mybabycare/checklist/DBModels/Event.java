package babycare.android.scu.edu.mybabycare.checklist.DBModels;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by akshu on 5/21/15.
 */
public class Event {

    private Integer checkListId;
    private String eventName;
    private String eventDate;
    private String eventDetails;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getCheckListId() {
        return checkListId;
    }

    public void setCheckListId(Integer checkListId) {
        this.checkListId = checkListId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public Event(){
        //default constructor
    }
    public Event(String eventDetails, Integer checkListId, String eventName, String eventDate) {
        this.eventDetails = eventDetails;
        this.checkListId = checkListId;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }



    public Date getEventDateFormat(){
        DateFormat format = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(getEventDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}
