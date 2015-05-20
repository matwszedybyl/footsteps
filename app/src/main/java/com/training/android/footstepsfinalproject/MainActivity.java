package com.training.android.footstepsfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.training.android.footstepsfinalproject.models.Walk;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback
{

    protected FragmentManager fm;
    private boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private ListView walksListView;
    private MapFragment mapFragment;
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.map) != null)
        {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            if (savedInstanceState == null)
            {
                getFragmentManager().beginTransaction()
                        .replace(R.id.map, mapFragment, DETAILFRAGMENT_TAG)
                        .commit();
            }

        } else
        {
            mTwoPane = false;
        }

        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null)
        {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        setListItemOnClickListener();

    }

    private void setListItemOnClickListener(){
        walksListView = (ListView) findViewById(R.id.distances_listview);

        if (mTwoPane) {
            walksListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id)
                {
                    Walk walk = (Walk) walksListView.getItemAtPosition(position);
                    LatLng newStartingLatLng = new LatLng(walk.getStartingLocation().getLatitude(), walk.getStartingLocation().getLongitude());
                    LatLng newEndingLatLng = new LatLng(walk.getEndingLocation().getLatitude(), walk.getEndingLocation().getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(newStartingLatLng, 12));
                    map.addMarker(new MarkerOptions().position(newStartingLatLng).icon(BitmapDescriptorFactory.defaultMarker()));
                    map.addMarker(new MarkerOptions().position(newEndingLatLng).icon(BitmapDescriptorFactory.defaultMarker()));
                }
            });


        } else {
            walksListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id)
                {

                    Walk walk = (Walk) walksListView.getItemAtPosition(position);
                    Intent i = new Intent(getBaseContext(), MapActivity.class);
                    i.putExtra("startingLat", walk.getStartingLocation().getLatitude());
                    i.putExtra("startingLong", walk.getStartingLocation().getLongitude());
                    i.putExtra("endingLat", walk.getEndingLocation().getLatitude());
                    i.putExtra("endingLong", walk.getEndingLocation().getLongitude());
                    startActivity(i);
                }
            });

        }
    }

    public Fragment createFragment()
    {
        return new MainFragment();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        this.map = map;
        map.setMyLocationEnabled(true);
        LatLng startingLoc = new LatLng(42.3314 , -83.0458 );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(startingLoc, 10));

    }

}
