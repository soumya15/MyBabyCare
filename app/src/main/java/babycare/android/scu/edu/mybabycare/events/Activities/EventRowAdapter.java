package babycare.android.scu.edu.mybabycare.events.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.events.DBModels.Event;

/**
 * Created by akshu on 5/24/15.
 */
public class EventRowAdapter extends ArrayAdapter<Event> {
    private final List<Event> events;

    public EventRowAdapter(Context context, int resource, List<Event> events) {
        super(context, resource, events);

        System.out.println("items recieved count : " + events.size());
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View row = inflater.inflate(R.layout.event_row, null);
        TextView eventName = (TextView) row.findViewById(R.id.tv_eventName);
        TextView eventDate = (TextView) row.findViewById(R.id.tv_eventDate);

        eventName.setText(events.get(position).getEventName() == null ? "" : events.get(position).getEventName().toString());
        eventDate.setText(events.get(position).getEventDate() == null ? "" : events.get(position).getEventDate().toString());
        Button editEventBtn = (Button) row.findViewById(R.id.btn_eventEdit);

        editEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), UpdateEvent.class);
                ViewEvents.currentEvent = events.get(pos);
                v.getContext().startActivity(myIntent);
            }
        });
        return row;
    }


}
