package babycare.android.scu.edu.mybabycare.events.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.events.DBModels.Event;
import babycare.android.scu.edu.mybabycare.events.DBUtils.EventDbHelper;

/**
 * Created by akshu on 5/24/15.
 */
public class ViewEvents extends Activity {

    public static Event currentEvent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_eventlist);
        List<Event> events = null;
        final EventDbHelper eventDbHelper = new EventDbHelper(this);
        List<Event> eventList = eventDbHelper.getAllEvents();
        ListView listView = (ListView) findViewById(R.id.eventListView);
        listView.setAdapter(new EventRowAdapter(this, R.layout.event_row, eventList));

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
