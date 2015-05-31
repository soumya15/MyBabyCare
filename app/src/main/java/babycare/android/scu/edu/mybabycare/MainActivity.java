package babycare.android.scu.edu.mybabycare;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Iterator;
import java.util.List;

import babycare.android.scu.edu.mybabycare.calendar.Activities.CalendarActivity;
import babycare.android.scu.edu.mybabycare.events.Activities.EventList;
import babycare.android.scu.edu.mybabycare.photos.Activities.PhotoActivity;
import babycare.android.scu.edu.mybabycare.preferences.Activities.UserPreferencesActivity;
import babycare.android.scu.edu.mybabycare.preferences.Activities.UserPreferencesFragment;
import babycare.android.scu.edu.mybabycare.shopping.Activities.ShoppingList;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;
import babycare.android.scu.edu.mybabycare.shopping.DbUtils.ItemDbHelper;
import babycare.android.scu.edu.mybabycare.shopping.locationreceiver.ProximityLocationReceiver;


public class MainActivity extends ActionBarActivity {
    private final String PROXIMITY_ALERT = "babycare.android.scu.edu.mybabycare.PROXIMITY_ALERT";
    private ProximityLocationReceiver proximityLocationReceiver = null;
    private LocationManager locationManager = null;
    PendingIntent pendingIntent = null;
    boolean notifyPref;
    boolean photoPref;

    SharedPreferences sharedPref=null;
    private PreferenceChangeListener mPreferenceListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting instance of Shared Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferenceListener = new PreferenceChangeListener();
        sharedPref.registerOnSharedPreferenceChangeListener(mPreferenceListener);

        //Getting value of Location notification from preferences
        notifyPref = sharedPref.getBoolean(UserPreferencesFragment.KEY_PREF_NOTIFY_LOCATION, true);
        photoPref = sharedPref.getBoolean(UserPreferencesFragment.KEY_PREF_PHOTO_SAVE, true);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        handldeLocationNotification();


    }

    private void handldeLocationNotification(){
        if (notifyPref) {
            setUnsetProximityAlert(getProximityAlertItems(),true);
            //Register the Proximity Alert Receiver
            proximityLocationReceiver = new ProximityLocationReceiver();
            IntentFilter intentFilter = new IntentFilter(PROXIMITY_ALERT);
            registerReceiver(proximityLocationReceiver, intentFilter);
        } else {
            setUnsetProximityAlert(getProximityAlertItems(),false);
        }
    }

    // Handle preferences changes
    private class PreferenceChangeListener implements
            SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs,
                                              String key) {
            notifyPref = prefs.getBoolean(UserPreferencesFragment.KEY_PREF_NOTIFY_LOCATION, true);
            photoPref = prefs.getBoolean(UserPreferencesFragment.KEY_PREF_PHOTO_SAVE, true);

            Log.i("Pref notify on change", String.valueOf(notifyPref));
            Log.i("Pref photo on change", String.valueOf(photoPref));

            handldeLocationNotification();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter iFilter = new IntentFilter(PROXIMITY_ALERT);
        if(notifyPref) {
            registerReceiver(proximityLocationReceiver, iFilter);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (notifyPref) {
            unregisterReceiver(proximityLocationReceiver);
        }
    }

    public void openShoppingList(View view){
        Intent shopListIntent = new Intent(this,ShoppingList.class);
        startActivity(shopListIntent);

    }

    public void openCheckList(View view){
        Intent checkListIntent = new Intent(this,EventList.class);
        startActivity(checkListIntent);

    }

    public void openPhotoGallery(View view){
        Intent photoIntent = new Intent(this,PhotoActivity.class);
        Bundle photoBundle = new Bundle();
        photoBundle.putBoolean("PhotoPreference",photoPref);
        photoIntent.putExtras(photoBundle);
        startActivity(photoIntent);

    }

    public void openCalendar(View view){
        Intent calendarIntent = new Intent(this,CalendarActivity.class);
        startActivity(calendarIntent);
    }

    private List<Item> getProximityAlertItems(){
        //Get all the items for which we need alert
        List<Item> proximityAlertItems= null;
        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        proximityAlertItems = itemDbHelper.getItemsForProximityAlert();
        return proximityAlertItems;
    }

    private void setUnsetProximityAlert(List<Item> proximityAlertItems, boolean set){
        //Iterate through the list and set Proximity Alert
        Iterator<Item> proximityItr = proximityAlertItems.iterator();
        while(proximityItr.hasNext()){
            Item i = proximityItr.next();

            addRemoveProximityAlert(Double.parseDouble(i.getStoreLatitude()),
                    Double.parseDouble(i.getStoreLongitude()), i.getProductId(), i.getProductName(),set);
        }
    }

    private void addRemoveProximityAlert(double lat, double lon,int item_id,String item_name, boolean add){

        //Radius for proximity alert
        float radius = 2200f;
        //Expiration time in milliseconds
        long expirationTime = 6000000L;
        //Setting data for the intent
        Intent intent = new Intent(PROXIMITY_ALERT);
        intent.putExtra(CommonConstants.ITEM_ID, item_id);
        intent.putExtra(CommonConstants.ITEM_NAME, item_name);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), item_id, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        if(add){
            Log.v("ProximitAlert", "Message: " +"adding alert for: " +  item_name);
            locationManager.addProximityAlert(lat, lon, radius, expirationTime, pendingIntent);
        } else {
            Log.v("ProximitAlert", "Message: " +"removing  alert: " +  item_name);
            locationManager.removeProximityAlert(pendingIntent);
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

        switch (id) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, UserPreferencesActivity.class);
                startActivity(settingsIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
