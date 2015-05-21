package babycare.android.scu.edu.mybabycare.shopping.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import babycare.android.scu.edu.mybabycare.CommonUtil;
import babycare.android.scu.edu.mybabycare.R;
import babycare.android.scu.edu.mybabycare.calendar.DBModels.CalendarEvent;
import babycare.android.scu.edu.mybabycare.calendar.DBUtils.CalendarDbHelper;
import babycare.android.scu.edu.mybabycare.shopping.Constants;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;
import babycare.android.scu.edu.mybabycare.shopping.DbUtils.ItemDbHelper;

/**
 * Created by akshu on 5/10/15.
 */
public class UpdateItem extends FragmentActivity {

    ItemDbHelper itemDbHelper = null;
    static String date = "";
    static boolean isPurchaseBtn = false, isExpiryBtn =false;
    double latitude;
    double longitude;
    EditText storeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialize the DB helper class object
        final Item currentItem = SearchList.currentItem;
        itemDbHelper = new ItemDbHelper(this);
        setContentView(R.layout.add_new_item);
        List<String> brandNames =  itemDbHelper.getAllBrands();
        AutoCompleteTextView brandName = (AutoCompleteTextView) findViewById(R.id.act_brand);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, brandNames);
        brandName.setAdapter(adapter);
        setCurrentItemValues(currentItem);
        Button updateItemBtn = (Button)findViewById(R.id.addItemBtn);
        updateItemBtn.setText("Update");
        ImageButton purchaseDateBtn = (ImageButton)findViewById(R.id.purchaseDateBtn);
        ImageButton expiryDateBtn = (ImageButton)findViewById(R.id.expiryDateBtn);
        updateItemBtn.setOnClickListener(new View.OnClickListener() {
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
                    itemDbHelper.updateItem(item);
                    if(item.isReminderSet()){
                        CalendarEvent calendarEvent = new CalendarEvent("Expiry of "+item.getProductName()+" ("+item.getBrandName()+") ",item.getExpiryDate());
                        CalendarDbHelper calendarDbHelper = new CalendarDbHelper(v.getContext());
                        calendarDbHelper.addEvent(calendarEvent);
                    }
                    CommonUtil.showOKDialog("Item Updated Successfully",v.getContext());
                    Intent searchItemsIntent = new Intent(v.getContext(),SearchList.class);
                    startActivity(searchItemsIntent);

                }catch(Exception ex){
                    System.out.println("item update unsuccessful");
                }

            }
        });
        purchaseDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPurchaseBtn = true;
                showDatePicker();

            }
        });
        expiryDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpiryBtn = true;
                showDatePicker();
            }
        });
    }

    private void setCurrentItemValues(Item item){
        CommonUtil.setValueOfEditText((EditText) findViewById(R.id.et_prodName),item.getProductName());
        CommonUtil.setValueOfEditText((EditText) findViewById(R.id.ed_category),item.getCategory());
        CommonUtil.setValueOfEditText((EditText) findViewById(R.id.act_brand),item.getBrandName());
        CommonUtil.setValueOfEditText((EditText) findViewById(R.id.et_itemCount),item.getItemCount().toString());
        CommonUtil.setCheckBoxSelected(((CheckBox) findViewById(R.id.cb_reminder)), item.isReminderSet());
        CommonUtil.setCheckBoxSelected((CheckBox) findViewById(R.id.cb_favorites), item.isFavorite());
        CommonUtil.setValueOfEditText((EditText) findViewById(R.id.expiryDateTxt), item.getExpiryDate());
        CommonUtil.setValueOfEditText((EditText) findViewById(R.id.purchaseDateTxt), item.getPurchaseDate());
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
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getSupportFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            int editView = 0;
            date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear)+ "/" + String.valueOf(year);
            if(isPurchaseBtn){
                editView = R.id.purchaseDateTxt;
                isPurchaseBtn = false;
            }
            if(isExpiryBtn){
                editView = R.id.expiryDateTxt;
                isExpiryBtn = false;
            }

            CommonUtil.setValueOfEditText(((EditText) findViewById(editView)),date);
        }
    };

    public void selectStoreLocation(View view) {
        Intent mapIntent = new Intent(this, StoreLocation.class);
        startActivityForResult(mapIntent, Constants.RESULT_CODE_ADDMAPTOITEM);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == Constants.RESULT_CODE_ADDMAPTOITEM){
            if(resultCode == Activity.RESULT_OK){
                latitude = data.getDoubleExtra(Constants.LATITUDE_KEY,0);
                longitude = data.getDoubleExtra(Constants.LONGITUDE_KEY,0);
                String address = data.getStringExtra(Constants.ADDRESS_KEY);
                storeTitle = (EditText)findViewById(R.id.et_storeLocation);
                storeTitle.setText(address);
            }
        }
    }

}


