package babycare.android.scu.edu.mybabycare.shopping.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import babycare.android.scu.edu.mybabycare.CommonConstants;
import babycare.android.scu.edu.mybabycare.CommonUtil;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.calendar.DBModels.CalendarEvent;
import babycare.android.scu.edu.mybabycare.calendar.DBUtils.CalendarDbHelper;
import babycare.android.scu.edu.mybabycare.notification.utils.Scheduler;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;
import babycare.android.scu.edu.mybabycare.shopping.DbUtils.ItemDbHelper;

/**
 * Created by akshu on 5/10/15.
 */
public class AddNewItem extends FragmentActivity {

    ItemDbHelper itemDbHelper = null;
    static String date = "";
    static boolean isPurchaseBtn = false, isExpiryBtn =false;
    double latitude;
    double longitude;
    EditText storeTitle;
    private Scheduler scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a new service client and bind our activity to this service
        scheduler = new Scheduler(this);
        scheduler.doBindService();

        //initialize the DB helper class object
        itemDbHelper = new ItemDbHelper(this);
        setContentView(R.layout.add_new_item);
        List<String> brandNames =  itemDbHelper.getAllBrands();
        AutoCompleteTextView brandName = (AutoCompleteTextView) findViewById(R.id.act_brand);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, brandNames);
        brandName.setAdapter(adapter);
        Button addItemBtn = (Button) findViewById(R.id.addItemBtn);
        ImageButton purchaseDateBtn = (ImageButton)findViewById(R.id.purchaseDateBtn);
        ImageButton expiryDateBtn = (ImageButton)findViewById(R.id.expiryDateBtn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = new Item();
                item.setProductName(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.et_prodName))));
                item.setCategory(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.ed_category))));
                item.setBrandName(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.act_brand))));
                item.setItemCount(Integer.parseInt(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.et_itemCount)))));
                item.setReminderSet(CommonUtil.isCheckBoxSelected(((CheckBox) findViewById(R.id.cb_reminder))));
                item.setFavorite(CommonUtil.isCheckBoxSelected(((CheckBox) findViewById(R.id.cb_favorites))));
                item.setExpiryDate(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.expiryDateTxt))));
                item.setPurchaseDate(CommonUtil.getValueFromEditText(((EditText) findViewById(R.id.purchaseDateTxt))));
                item.setStoreAddress(CommonUtil.getValueFromEditText(((EditText)findViewById(R.id.et_storeLocation))));
                item.setStoreLatitude(String.valueOf(latitude));
                item.setStoreLongitude(String.valueOf(longitude));
                try {
                    long rowID = itemDbHelper.addItem(item);
                    item.setProductId((int)rowID);
                    Log.d("ds","reminder set"+item.isReminderSet());
                    if(item.isReminderSet()){
                        Log.d("ds","true");
                        if(item.getExpiryDate() != "") {
                            Log.d("ds","true");
                            CalendarEvent calendarEvent = new CalendarEvent(CommonConstants.ITEM_EVENT_NAME, item.getProductId(), "Expiry of " + item.getProductName() + " (" + item.getBrandName() + ") ", item.getExpiryDate());
                            CalendarDbHelper calendarDbHelper = new CalendarDbHelper(v.getContext());
                            calendarDbHelper.addEvent(calendarEvent);
                            Log.d("ds","done adding");
                            Calendar c = Calendar.getInstance();
                            c.setTimeInMillis(System.currentTimeMillis());
                            c.clear();
                            c.add(Calendar.YEAR, Integer.parseInt(item.getExpiryDate().split("/")[2]));
                            c.add(Calendar.MONTH,Integer.parseInt(item.getExpiryDate().split("/")[0])-1);
                            c.add(Calendar.DAY_OF_MONTH,Integer.parseInt(item.getExpiryDate().split("/")[1]));
                            c.add(Calendar.HOUR,12);
                            c.add(Calendar.MINUTE,50);
                            c.setTimeZone(TimeZone.getTimeZone("PST"));
                            //c.set(Integer.parseInt(item.getExpiryDate().split("/")[2]), Integer.parseInt(item.getExpiryDate().split("/")[0]), Integer.parseInt(item.getExpiryDate().split("/")[1]), 00,0);
                            Log.d("ds","calendar instance");
                            //c.setTime(item.getExpiryDateFormat());
                            Log.d("ds","calendar get expiry");
                            // Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
                            scheduler.setAlarmForNotification(c, "Time to buy a new item for your baby!!!" , calendarEvent.getEventName());
                        }

                    }

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setTitle("Baby Item:");
                    alertDialogBuilder.setMessage("Item Added Successfully");
                    alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            finish();
                            Intent searchItemsIntent = new Intent(getBaseContext(),SearchList.class);
                            startActivity(searchItemsIntent);
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }catch(Exception ex){
                    System.out.println("item add unsuccessful"+ex);
                }

            }
        });
        purchaseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("purchaseDate");

            }
        });
        expiryDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker("expiryDate");
            }
        });
    }

    public void selectStoreLocation(View view) {
        Intent mapIntent = new Intent(this, StoreLocation.class);
        startActivityForResult(mapIntent, CommonConstants.RESULT_CODE_ADDMAPTOITEM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CommonConstants.RESULT_CODE_ADDMAPTOITEM){
            if(resultCode == Activity.RESULT_OK){
                latitude = data.getDoubleExtra(CommonConstants.LATITUDE_KEY,0);
                longitude = data.getDoubleExtra(CommonConstants.LONGITUDE_KEY,0);
                String address = data.getStringExtra(CommonConstants.ADDRESS_KEY);
                storeTitle = (EditText)findViewById(R.id.et_storeLocation);
                storeTitle.setText(address);
            }
        }
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
    private void showDatePicker(String type) {
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
        /**
         * Set Call back to capture selected date
         */
        if(type.equals("purchaseDate")){
            date.setCallBack(onPurchaseDate);
        } else {
            date.setCallBack(onExpiryDate);
        }
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener onPurchaseDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date = String.valueOf(monthOfYear+1) + "/" + String.valueOf(dayOfMonth)+ "/" + String.valueOf(year);
            CommonUtil.setValueOfEditText(((EditText)findViewById(R.id.purchaseDateTxt)),date);
        }
    };

    DatePickerDialog.OnDateSetListener onExpiryDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            date = String.valueOf(monthOfYear+1) + "/" + String.valueOf(dayOfMonth)+ "/" + String.valueOf(year);
            CommonUtil.setValueOfEditText(((EditText)findViewById(R.id.expiryDateTxt)),date);
        }
    };

    @Override
    protected void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if(scheduler != null)
            scheduler.doUnbindService();
        super.onStop();
    }

    public void showSearchIntent(){
        Intent searchItemsIntent = new Intent(this,SearchList.class);
        startActivity(searchItemsIntent);
    }
}


