package com.training.android.footstepsfinalproject;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.android.footstepsfinalproject.data.FootstepsContract;

/**
 * Created by mwszedybyl on 4/25/15.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = "MapActivity";
    private Uri mUri;

    private static final String[] DETAIL_COLUMNS = {
            FootstepsContract.WalkEntry.TABLE_NAME + "." + FootstepsContract.WalkEntry._ID,
            FootstepsContract.WalkEntry.COLUMN_STARTING_LAT,
            FootstepsContract.WalkEntry.COLUMN_STARTING_LONG,
            FootstepsContract.WalkEntry.COLUMN_ENDING_LAT,
            FootstepsContract.WalkEntry.COLUMN_ENDING_LONG,
            FootstepsContract.WalkEntry.COLUMN_DISTANCE,
            FootstepsContract.WalkEntry.COLUMN_TEMP
    };
    public static final int COL_WALK_ID = 0;
    public static final int COL_STARTING_LAT = 1;
    public static final int COL_STARTING_LONG = 2;
    public static final int COL_ENDING_LAT = 3;
    public static final int COL_ENDING_LONG = 4;
    public static final int COL_DISTANCE = 5;
    public static final int COL_TEMP = 6;

    private LatLng startingLoc;
    private LatLng endingLoc;

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
        startingLoc = new LatLng(getIntent().getDoubleExtra("startingLat", 0), getIntent().getDoubleExtra("startingLong", 0));
        endingLoc = new LatLng(getIntent().getDoubleExtra("endingLat", 0), getIntent().getDoubleExtra("endingLong", 0));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingLoc, 12));
        map.addMarker(new MarkerOptions().position(startingLoc).icon(BitmapDescriptorFactory.defaultMarker()));
        map.addMarker(new MarkerOptions().position(endingLoc).icon(BitmapDescriptorFactory.defaultMarker()));

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    this,
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        if (data != null && data.moveToFirst())
        {
            // Read weather condition ID from cursor
            startingLoc = new LatLng(data.getDouble(COL_STARTING_LAT), data.getDouble(COL_STARTING_LONG));
            endingLoc = new LatLng(data.getDouble(COL_ENDING_LAT), data.getDouble(COL_ENDING_LONG));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }
}

