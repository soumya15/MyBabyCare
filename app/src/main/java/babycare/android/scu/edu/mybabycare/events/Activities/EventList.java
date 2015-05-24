package babycare.android.scu.edu.mybabycare.events.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import babycare.android.scu.edu.mybabycare.R;

/**
 * Created by akshu on 5/23/15.
 */
public class EventList extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_list);

    }

    public void openAddEventView(View view) {
        Intent addEventViewIntent = new Intent(this, AddEvent.class);
        startActivity(addEventViewIntent);

    }

    public void showAllEvents(View view) {
        Intent showEventsIntent = new Intent(this, ViewEvents.class);
        startActivity(showEventsIntent);
    }
}