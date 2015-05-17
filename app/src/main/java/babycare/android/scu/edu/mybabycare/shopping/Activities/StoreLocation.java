package babycare.android.scu.edu.mybabycare.shopping.Activities;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import babycare.android.scu.edu.mybabycare.R;

/***
 * This class opens the map, allowing the user to type in the preferred store and select its location.
 * Created by akshu on 5/16/15.
 */
public class StoreLocation extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng currentLocation;
    EditText location;
    String criteria;
    Button searchbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_location);
        setUpMapIfNeeded();

        //Initializing fields
        location = (EditText) findViewById(R.id.et_address);
        searchbtn = (Button) findViewById(R.id.btn_searchLocation);

        //Search button on click
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide KeyBoard
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        getBaseContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(location.getWindowToken(), 0);

                //Clear all existing marker if any
                mMap.clear();

                //Getting the user input
                criteria = location.getText().toString();
                searchLocation(criteria);
            }

        });

        /***
         *  Just showing Latitude/Longitude on longclick
         */
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Toast.makeText(getBaseContext(), String.valueOf(latLng.latitude) + "  :  " + String.valueOf(latLng.longitude), Toast.LENGTH_LONG).show();

            }
        });
    }

    /***
     *  Search locations based on Forward geo coding
     * @param criteria : User input
     */
    public void searchLocation(String criteria) {
        Geocoder fwdGeocoder = new Geocoder(this, Locale.US);
        List<Address> locations = null;
        try {
            double[] boundary = getBoundingBox(currentLocation.latitude,currentLocation.longitude,48280); // Boundary of 30 miles around the center

            locations = fwdGeocoder.getFromLocationName(criteria, 7, boundary[0], boundary[1], boundary[2],boundary[3]);
            Iterator<Address> itr = locations.iterator();
            while (itr.hasNext()) {
                Address a = itr.next();
                String markerMessage = criteria + "," + a.getAddressLine(1);
                if (null != mMap) {
                    mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(a.getLatitude(), a.getLongitude()))
                                    .title(markerMessage)
                                    .draggable(false)
                    );
                }
            }
        } catch (IOException e) {
            //TODO: HAndle exception Properly
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locationMap))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location location) {
                        currentLocation = new LatLng(location.getLatitude(),location.getLongitude());

                        // Show the current location in Google Map
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

                        // Zoom in the Google Map
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                    }
                });

            }
        }
    }

    private void setUpMap() {

    }

    /***
     * this function calculates range from current location for the geocoder
     * @param pLatitude : Latitude of center of the range
     * @param pLongitude : Longitude of center of the range
     * @param pDistanceInMeters : Range in meters
     * @return : Array of lat/long range
     */
    private double[] getBoundingBox(final double pLatitude, final
    double pLongitude, final int pDistanceInMeters) {

        final double[] boundingBox = new double[4];

        final double latRadian = Math.toRadians(pLatitude);

        final double degLatKm = 110.574235;
        final double degLongKm = 110.572833 * Math.cos(latRadian);
        final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
        final double deltaLong = pDistanceInMeters / 1000.0 /
                degLongKm;

        final double minLat = pLatitude - deltaLat;
        final double minLong = pLongitude - deltaLong;
        final double maxLat = pLatitude + deltaLat;
        final double maxLong = pLongitude + deltaLong;

        boundingBox[0] = minLat;
        boundingBox[1] = minLong;
        boundingBox[2] = maxLat;
        boundingBox[3] = maxLong;

        return boundingBox;
    }




}
