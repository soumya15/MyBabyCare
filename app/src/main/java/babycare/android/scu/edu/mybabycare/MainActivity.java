package babycare.android.scu.edu.mybabycare;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Iterator;
import java.util.List;

import babycare.android.scu.edu.mybabycare.calendar.Activities.CalendarActivity;
import babycare.android.scu.edu.mybabycare.shopping.Activities.ShoppingList;
import babycare.android.scu.edu.mybabycare.shopping.DBModels.Item;
import babycare.android.scu.edu.mybabycare.shopping.DbUtils.ItemDbHelper;
import babycare.android.scu.edu.mybabycare.shopping.locationreceiver.ProximityLocationReceiver;


public class MainActivity extends Activity {
    private final String PROXIMITY_ALERT = "babycare.android.scu.edu.mybabycare.PROXIMITY_ALERT";
    private ProximityLocationReceiver proximityLocationReceiver = null;
    private LocationManager locationManager = null;
    PendingIntent pendingIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLatLongForProximityAlert();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter iFilter = new IntentFilter(PROXIMITY_ALERT);
        iFilter.addDataScheme("latlong");
        registerReceiver(proximityLocationReceiver, iFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(proximityLocationReceiver);
    }

    public void openShoppingList(View view){
        Intent shopListIntent = new Intent(this,ShoppingList.class);
        startActivity(shopListIntent);

    }

    public void openCalendar(View view){
        Intent calendarIntent = new Intent(this,CalendarActivity.class);
        startActivity(calendarIntent);
    }

    private void getLatLongForProximityAlert(){

        int requestCode = 200;
        List<Item> proximityAlertItems;

        //Get all the items for which we need alert
        ItemDbHelper itemDbHelper = new ItemDbHelper(this);
        proximityAlertItems = itemDbHelper.getItemsForProximityAlert();

        //Iterate through the list and set Proximity Alert
        Iterator<Item> proximityItr = proximityAlertItems.iterator();
        while(proximityItr.hasNext()){
            Item i = proximityItr.next();
            setProximityAlert(Double.parseDouble(i.getStoreLatitude()),
                    Double.parseDouble(i.getStoreLongitude()), i.getProductId(), i.getProductName(), requestCode);
        }

        //Register the Proximity Alert Reciever
        proximityLocationReceiver = new ProximityLocationReceiver();
        IntentFilter intentFilter = new IntentFilter(PROXIMITY_ALERT);
        intentFilter.addDataScheme("latlong");
        registerReceiver(proximityLocationReceiver, intentFilter);
    }

    private void setProximityAlert(double lat, double lon,int item_id,String item_name, int requestCode){

        //Radius for proximity alert
        float radius = 22000f;
        //Expiration time in milliseconds
        long expirationTime = 6000000L;
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //Setting data for the intent
        String latlong = "latlong:"+lat+","+lon;
        Intent intent = new Intent(PROXIMITY_ALERT, Uri.parse(latlong));
        intent.putExtra(CommonConstants.ITEM_ID, item_id);
        intent.putExtra(CommonConstants.ITEM_NAME, item_name);

        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Log.v("ProximitAlert", "Message: " +"before setting alert: " +  item_name);

        locationManager.addProximityAlert(lat, lon, radius, expirationTime, pendingIntent);

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
