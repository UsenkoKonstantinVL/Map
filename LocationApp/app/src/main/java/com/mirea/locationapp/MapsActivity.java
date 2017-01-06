package com.mirea.locationapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Parcelable> arrayGPSLocations;
    private ArrayList<Parcelable> arrayGSMLocations;
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        arrayGSMLocations = intent.getParcelableArrayListExtra(MainActivity.EXTRA_GSM);
        arrayGPSLocations = intent.getParcelableArrayListExtra(MainActivity.EXTRA_GPS);

        if(arrayGPSLocations == null)
        {
            arrayGPSLocations = new ArrayList<>();
        }

        if(arrayGSMLocations == null)
        {
            arrayGSMLocations = new ArrayList<>();
        }




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String text = "";

        text = "GPS: " + String.valueOf( arrayGPSLocations.size()) +
        "\nGSM: " + String.valueOf(arrayGSMLocations.size());

        Toast.makeText(this , text,
                Toast.LENGTH_LONG).show();

        drawGPSLocations();
        drawGSMLocations();

        CameraUpdate center;
        if(arrayGPSLocations != null && arrayGPSLocations.size() > 0) {
            center =
                    CameraUpdateFactory.newLatLng((LatLng) arrayGPSLocations.get(0));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }
        else if(arrayGSMLocations != null  && arrayGSMLocations.size() > 0)
        {
            center =
                    CameraUpdateFactory.newLatLng((LatLng) arrayGSMLocations.get(0));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
        }
        // Add a marker in Sydney and move the camera
       /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        /*PolylineOptions polylineOptions = new PolylineOptions()
                .add(new LatLng(0, 15)).add(new LatLng(0, 16))
                .add(new LatLng(0, 17))
                .color(Color.MAGENTA).width(1);

        mMap.addPolyline(polylineOptions);

        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(0, 15)).radius(50000)
                .fillColor(Color.YELLOW).strokeColor(Color.DKGRAY)
                .strokeWidth(5);

        mMap.addCircle(circleOptions);

        circleOptions = new CircleOptions()
                .center(new LatLng(0, 16)).radius(50000)
                .fillColor(Color.YELLOW).strokeColor(Color.DKGRAY)
                .strokeWidth(5);

        mMap.addCircle(circleOptions);

        circleOptions = new CircleOptions()
                .center(new LatLng(0, 17)).radius(50000)
                .fillColor(Color.YELLOW).strokeColor(Color.DKGRAY)
                .strokeWidth(5);

        mMap.addCircle(circleOptions);*/
    }

    private static final long POLYLINE_WIDTH = 5;
    private  static  final  int CIRCLE_RADIUS = 2;
    private  static  final  int CIRCLE_STROKE_WIDTH = 2;
    //private  static final Color CIRCLE_FILL_COLOR = Color.YELLOW;

    private void drawGPSLocations()
    {
        PolylineOptions polylineOptions = new PolylineOptions();

        for (Parcelable locations: arrayGPSLocations
             ) {
            polylineOptions.add((LatLng) locations).zIndex(1.8f);
        }
        polylineOptions.color(Color.MAGENTA).width(POLYLINE_WIDTH);
        mMap.addPolyline(polylineOptions);

        CircleOptions circleOptions;

        for (Parcelable locations: arrayGPSLocations
                ) {
            circleOptions = new CircleOptions()
                    .center((LatLng)locations).radius(CIRCLE_RADIUS)
                    .fillColor(Color.YELLOW).strokeColor(Color.DKGRAY)
                    .strokeWidth(CIRCLE_STROKE_WIDTH).zIndex(2f);
            mMap.addCircle(circleOptions);
        }

    }


    private  static  final  int GSM_CIRCLE_RADIUS = 3;
    private  static  final  int GSM_CIRCLE_STROKE_WIDTH = 5;

    private void drawGSMLocations()
    {
        CircleOptions circleOptions;

        for (Parcelable locations: arrayGSMLocations
                ) {
            circleOptions = new CircleOptions()
                    .center((LatLng)locations).radius(GSM_CIRCLE_RADIUS)
                    .fillColor(Color.RED).strokeColor(Color.DKGRAY)
                    .strokeWidth(GSM_CIRCLE_STROKE_WIDTH)
            .zIndex(1.5f);
            mMap.addCircle(circleOptions);
        }
    }
}
