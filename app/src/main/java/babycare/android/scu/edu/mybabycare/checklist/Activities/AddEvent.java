package babycare.android.scu.edu.mybabycare.checklist.Activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import babycare.android.scu.edu.mybabycare.CommonUtil;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.checklist.DBModels.Event;
import babycare.android.scu.edu.mybabycare.checklist.DBUtils.EventDbHelper;
import babycare.android.scu.edu.mybabycare.shopping.Activities.DatePickerFragment;

/**
 * Created by akshu on 5/21/15.
 */
public class AddEvent extends FragmentActivity {

    EventDbHelper eventDbHelper = null;
    static String eventDate = "";
    EditText addEventName;
    EditText addEventDate;
    EditText addEventDetails;
    ImageButton imgEventDate;
    private static String date;
    Button btnAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_checklist);

        eventDbHelper = new EventDbHelper(this);
        addEventName = (EditText) findViewById(R.id.et_eventName);
        addEventDate = (EditText) findViewById(R.id.et_eventDate);
        addEventDetails =  (EditText) findViewById(R.id.et_eventDetail);
        imgEventDate = (ImageButton) findViewById(R.id.img_eventDate);
        btnAddEvent = (Button)findViewById(R.id.btn_addNewCheckList);

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setEventName(CommonUtil.getValueFromEditText(addEventName));
                event.setEventDate(CommonUtil.getValueFromEditText(addEventDate));
                event.setEventDetails(CommonUtil.getValueFromEditText(addEventDetails));

                try {
                    eventDbHelper.addEvent(event);
                    Toast.makeText(getBaseContext(),"Event created successfully",Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    System.out.println("item add unsuccessful");
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        imgEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }
        });

    }

    private void showDatePicker() {
        date = "";
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onDateClick);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener onDateClick = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date = String.valueOf(monthOfYear+1) + "/" + String.valueOf(dayOfMonth)+ "/" + String.valueOf(year);
            CommonUtil.setValueOfEditText(((EditText)findViewById(R.id.et_eventDate)),date);
        }
    };

}