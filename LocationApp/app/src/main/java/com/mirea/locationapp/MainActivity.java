package com.mirea.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Public
    public static  final String EXTRA_GPS = "GPS";
    public  static final String EXTRA_GSM = "GSM";


    //Private
    TextView tvTextCurrentLocation;
    TextView tvCurrentInformation;
    Button buttonStart;
    boolean isWorking = false;
    private LocationManager locationManager;
    private ArrayList<Parcelable> arrayGPS;
    private ArrayList<Parcelable> arrayGSM;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 200 * 60 * 1; // 1 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tvTextCurrentLocation = (TextView)findViewById(R.id.textViewCurrentLocation);
        tvCurrentInformation = (TextView)findViewById(R.id.textViewInformation);
        buttonStart = (Button)findViewById(R.id.button);

        buttonStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MapsActivity.class);
                intent.putParcelableArrayListExtra(EXTRA_GPS, arrayGPS);
                intent.putParcelableArrayListExtra(EXTRA_GSM, arrayGSM);

                /*Bundle args = new Bundle();
                args.putParcelable("from_position", arrayGPS);
                args.putParcelable("to_position", arrayGSM);*/

                startActivity(intent);
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        arrayGPS = new ArrayList<>();
        arrayGSM = new ArrayList<>();

        isAlreadyWork = false;

        //fillArray();

    }


    private  void fillArray()
    {
        LatLng l = new LatLng(55.915867, 37.773123);
        arrayGPS.add(l);
        l = new LatLng(55.915867, 37.773123);
        arrayGPS.add(l);
        l = new LatLng(55.915903, 37.773257);
        arrayGPS.add(l);
        l = new LatLng(55.915954, 37.773418);
        arrayGPS.add(l);
        l = new LatLng(55.915990, 37.773520);
        arrayGPS.add(l);

        l = new LatLng(55.915867, 37.773123);
        arrayGSM.add(l);
        l = new LatLng(55.915903, 37.773257);
        arrayGSM.add(l);
        l = new LatLng(55.915954, 37.773418);
        arrayGSM.add(l);
        l = new LatLng(55.915990, 37.773520);
        arrayGSM.add(l);
      /*  l = new LatLng(1, 20.5);
        arrayGSM.add(l);*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
        }
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    locationListener);

        }
        checkEnabled();
    }


    private static final String GPS_ARRAY = "GPS";
    private static final String GSM_ARRAY = "GSM";
    private boolean isAlreadyWork = true;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        //outState.putSerializable(STATE_ITEMS, mItems);
        outState.putParcelableArrayList(GPS_ARRAY, arrayGPS);
        outState.putParcelableArrayList(GSM_ARRAY, arrayGSM);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        /*boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
        double myDouble = savedInstanceState.getDouble("myDouble");
        int myInt = savedInstanceState.getInt("MyInt");
        String myString = savedInstanceState.getString("MyString");*/

        arrayGPS = savedInstanceState.getParcelableArrayList(GPS_ARRAY);
        arrayGSM = savedInstanceState.getParcelableArrayList(GSM_ARRAY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }

    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    private String GPSLocation = "";
    private String GSMLocation = "";

    private void showLocation(Location location)
    {
        if(location == null)
        {
            tvTextCurrentLocation.setText("Проблемы с определением координат");
            return;
        }
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            GPSLocation = (formatLocation(location));
            arrayGPS.add(new LatLng(location.getLatitude(), location.getLongitude()));
        } else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {
            GSMLocation = (formatLocation(location));
            arrayGSM.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        setLocation();
    }

    private void setLocation()
    {
        String text = "GPS (" + arrayGPS.size() + "):" + GPSLocation + "\nGSM: (" + arrayGSM.size() + "):"  + GSMLocation;
        tvTextCurrentLocation.setText(text);
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    private  void checkEnabled()
    {
        String text = "Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)  +
            "\nEnabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        tvCurrentInformation.setText(text);
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
