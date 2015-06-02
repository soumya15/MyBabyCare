package babycare.android.scu.edu.mybabycare.events.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import babycare.android.scu.edu.mybabycare.CommonRowObject;
import babycare.android.scu.edu.mybabycare.ImgNameRowAdapter;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.shopping.Activities.AddNewItem;

/**
 * Created by akshu on 5/23/15.
 */
public class EventList extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_list);
        ListView listView = (ListView) findViewById(R.id.listView);
        List<CommonRowObject> rowObject = new ArrayList<>();
        rowObject.add(new CommonRowObject("Add New Event",R.drawable.add_icon,addEventListener));
        rowObject.add(new CommonRowObject("View Events",R.drawable.search_icon,searchEventListener));
        // set the custom adapter with rowObject data passed
        listView.setAdapter(new ImgNameRowAdapter(this, R.layout.custom_row, rowObject));
    }

    View.OnClickListener addEventListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent addEventViewIntent = new Intent(v.getContext(), AddEvent.class);
            startActivity(addEventViewIntent);
        }
    };

    View.OnClickListener searchEventListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent addEventViewIntent = new Intent(v.getContext(), ViewEvents.class);
            startActivity(addEventViewIntent);
        }
    };

}