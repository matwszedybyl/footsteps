package com.training.android.footstepsfinalproject;

import android.content.Intent;
import android.location.Location;
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

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    protected FragmentManager fm;
    private boolean mTwoPane;
    private ListView walksListView;
    private MapFragment mapFragment;
    private GoogleMap map;
    private Walk currentWalk;
    private Location startingLoc;
    private Location endingLoc;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.map) != null)
        {
            mTwoPane = true;
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

        currentWalk = new Walk();
        if (savedInstanceState != null)
        {
            startingLoc = new Location("");
            startingLoc.setLatitude(savedInstanceState.getDouble("startingLat"));
            startingLoc.setLongitude(savedInstanceState.getDouble("startingLng"));


            endingLoc = new Location("");
            endingLoc.setLatitude(savedInstanceState.getDouble("endingLat"));
            endingLoc.setLongitude(savedInstanceState.getDouble("endingLng"));

            currentWalk.setStartingLocation(startingLoc);
            currentWalk.setEndingLocation(endingLoc);
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        setListItemOnClickListener();

    }

    private void setListItemOnClickListener()
    {
        walksListView = (ListView) findViewById(R.id.distances_listview);

        if (mTwoPane)
        {

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
                    MainFragment mainFrag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    mainFrag.setPosition(position);

                }
            });
            MainFragment mainFrag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            walksListView.setSelection(mainFrag.getPosition());


        } else
        {
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        MainFragment mainFrag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        currentWalk = mainFrag.getCurrentWalk();
        if(currentWalk!=null)
        {
            savedInstanceState.putDouble("startingLat", currentWalk.getStartingLocation().getLatitude());
            savedInstanceState.putDouble("startingLng", currentWalk.getStartingLocation().getLongitude());
            savedInstanceState.putDouble("endingLat", currentWalk.getEndingLocation().getLatitude());
            savedInstanceState.putDouble("endingLng", currentWalk.getEndingLocation().getLongitude());
            super.onSaveInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        this.map = map;
        map.setMyLocationEnabled(true);
        LatLng startLatLng;
        if(currentWalk.getStartingLocation().getLatitude()!=0)
        {
            startLatLng = new LatLng(currentWalk.getStartingLocation().getLatitude(),
                    currentWalk.getStartingLocation().getLongitude());

            LatLng endingLatLng = new LatLng(currentWalk.getEndingLocation().getLatitude(),
                    currentWalk.getEndingLocation().getLongitude());

            map.addMarker(new MarkerOptions().position(startLatLng).icon(BitmapDescriptorFactory.defaultMarker()));
            map.addMarker(new MarkerOptions().position(endingLatLng).icon(BitmapDescriptorFactory.defaultMarker()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 12));

        } else
        {
            startLatLng = new LatLng(0,0);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 2));
        }

    }

}
