package com.training.android.footstepsfinalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by mwszedybyl on 4/25/15.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback
{
    private static final String TAG = "MapActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.map_activity);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onMapReady(GoogleMap map)
    {
        map.setMyLocationEnabled(true);

        LatLng startingLoc = new LatLng(getIntent().getDoubleExtra("startingLat", 0), getIntent().getDoubleExtra("startingLong", 0));
        LatLng endingLoc = new LatLng(getIntent().getDoubleExtra("endingLat", 0), getIntent().getDoubleExtra("endingLong", 0));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingLoc, 12));
        map.addMarker(new MarkerOptions().position(startingLoc).icon(BitmapDescriptorFactory.defaultMarker()));
        map.addMarker(new MarkerOptions().position(endingLoc).icon(BitmapDescriptorFactory.defaultMarker()));

    }

    private void setupViews() {

    }
}

