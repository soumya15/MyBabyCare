package babycare.android.scu.edu.mybabycare.events.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.Iterator;
import java.util.List;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.events.DBModels.Event;
import babycare.android.scu.edu.mybabycare.events.DBUtils.EventDbHelper;

/**
 * Created by akshu on 5/24/15.
 */
public class ViewEvents extends Activity {

    public static Event currentEvent = null;
    ImageButton deleteEvent;
    ListView listView;
    List<Event> events = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_eventlist);

        final EventDbHelper eventDbHelper = new EventDbHelper(this);
        List<Event> eventList = eventDbHelper.getAllEvents();
        listView = (ListView) findViewById(R.id.eventListView);
        listView.setAdapter(new EventRowAdapter(this, R.layout.event_row, eventList));

        deleteEvent = (ImageButton)findViewById(R.id.img_deleteButton);
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator itr = EventRowAdapter.toDeleteEvents.iterator();
                while (itr.hasNext()){

                    eventDbHelper.deleteEvent(Integer.parseInt(itr.next().toString()));

                    Log.i("Deleted", "this event deleted");
                }
                refreshListView();

            }
        });

    }

    public void refreshListView() {
        final EventDbHelper eventDbHelper = new EventDbHelper(this);
        events = eventDbHelper.getAllEvents();
        listView.setAdapter(null);
        listView.setAdapter(new EventRowAdapter(this, R.layout.item_row, events));
        Log.i("Refreshed","Refreshed after deletion");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
