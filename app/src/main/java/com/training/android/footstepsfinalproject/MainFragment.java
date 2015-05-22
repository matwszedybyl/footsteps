package com.training.android.footstepsfinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.training.android.footstepsfinalproject.data.FootstepsContract;
import com.training.android.footstepsfinalproject.models.Walk;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by mwszedybyl on 4/25/15.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = "MainFragment";

    private View rootView;
    private Button startButton;
    private Button stopButton;
    private ListView walksListView;
    private Walk newWalk;
    private Location startingLocation;
    private Location endingLocation;
    private Uri mUri;
    private ArrayList<Walk> walkList;
    private WalksAdapter adapter;
    private Button getWeatherButton;
    private TextView weatherTextView;
    private String currentTempString;
    private double temperature;
    private String tempString;
    private int listViewIndex;
    private Walk currentWalk;

    public static final int COL_WALK_ID = 0;
    public static final int COL_STARTING_LAT = 1;
    public static final int COL_STARTING_LONG = 2;
    public static final int COL_ENDING_LAT = 3;
    public static final int COL_ENDING_LONG = 4;
    public static final int COL_DISTANCE = 5;
    public static final int COL_TEMP = 6;

    private static final String[] DETAIL_COLUMNS = {
            FootstepsContract.WalkEntry.TABLE_NAME + "." + FootstepsContract.WalkEntry._ID,
            FootstepsContract.WalkEntry.COLUMN_STARTING_LAT,
            FootstepsContract.WalkEntry.COLUMN_STARTING_LONG,
            FootstepsContract.WalkEntry.COLUMN_ENDING_LAT,
            FootstepsContract.WalkEntry.COLUMN_ENDING_LONG,
            FootstepsContract.WalkEntry.COLUMN_DISTANCE,
            FootstepsContract.WalkEntry.COLUMN_TEMP
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            currentTempString = savedInstanceState.getString("currentTemp");
            listViewIndex = savedInstanceState.getInt("listViewIndex");
            tempString = savedInstanceState.getString("tempString");
        } else {
            currentTempString = "What is the current temp?";
            listViewIndex = -1;
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView()");
        rootView = inflater.inflate(R.layout.main_fragment, parent, false);
        FootstepsApplication.getInstance();
        LocationHelper.getInstance();
        setupViews();
        return rootView;
    }

    private void setupViews()
    {
        startButton = (Button) rootView.findViewById(R.id.start_tracking_button);
        stopButton = (Button) rootView.findViewById(R.id.stop_tracking_button);
        walksListView = (ListView) rootView.findViewById(R.id.distances_listview);
        walksListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        weatherTextView = (TextView) rootView.findViewById(R.id.weather_textview);
        weatherTextView.setText(currentTempString);
        getWeatherButton = (Button) rootView.findViewById(R.id.find_weather_button);
        adapter = new WalksAdapter(getActivity(), null, 0);
        walksListView.setAdapter(adapter);

        getWeatherButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new GetWeatherTask().execute();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LocationHelper.getInstance().startLocationUpdates();
                newWalk = new Walk();
                Toast.makeText(getActivity().getBaseContext(), "Distance tracker started", Toast.LENGTH_SHORT).show();
                newWalk.setStartingLocation(LocationHelper.getLastLocation());
                startingLocation = newWalk.getStartingLocation();
                stopButton.setEnabled(true);
                startButton.setEnabled(false);

            }
        });
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LocationHelper.getInstance().stopLocationUpdates();
                Toast.makeText(getActivity().getBaseContext(), "Distance tracker stopped", Toast.LENGTH_SHORT).show();
                newWalk.setEndingLocation(LocationHelper.getLastLocation());
                endingLocation = newWalk.getEndingLocation();
                float distance = startingLocation.distanceTo(endingLocation);
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                distance = Float.parseFloat(df.format(distance));
                System.out.println(distance);
                newWalk.setDistanceInMeters(distance);
                saveWalkToDatabase();
                stopButton.setEnabled(false);
                startButton.setEnabled(true);
            }
        });


        mUri = FootstepsContract.WalkEntry.CONTENT_URI;
        getLoaderManager().restartLoader(0, null, this);

    }

    private void saveWalkToDatabase(){
        ContentValues walkValues = new ContentValues();
        walkValues.put(FootstepsContract.WalkEntry.COLUMN_STARTING_LAT, newWalk.getStartingLocation().getLatitude());
        walkValues.put(FootstepsContract.WalkEntry.COLUMN_STARTING_LONG, newWalk.getStartingLocation().getLongitude());
        walkValues.put(FootstepsContract.WalkEntry.COLUMN_ENDING_LAT, newWalk.getEndingLocation().getLatitude());
        walkValues.put(FootstepsContract.WalkEntry.COLUMN_ENDING_LONG, newWalk.getEndingLocation().getLongitude());
        walkValues.put(FootstepsContract.WalkEntry.COLUMN_DISTANCE, newWalk.getDistanceInMeters());
        walkValues.put(FootstepsContract.WalkEntry.COLUMN_TEMP, tempString);
        getActivity().getContentResolver().insert(FootstepsContract.WalkEntry.CONTENT_URI, walkValues);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
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
        adapter.swapCursor(data);
        adapter.setWalkList(data);
        walksListView.setAdapter(adapter);
        walksListView.smoothScrollToPosition(listViewIndex);
        walksListView.setItemChecked(listViewIndex, true);
        walksListView.setSelection(listViewIndex);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.swapCursor(null);
    }

    public Walk getCurrentWalk(){
        if(listViewIndex==-1){
            return null;
        }
        return currentWalk = adapter.getItem(listViewIndex);
    }

    public void setPosition(int position){
        listViewIndex = position;
    }

    public int getPosition(){
        return listViewIndex;
    }

    private class GetWeatherTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params) {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // fetch data
                try
                {
                    Log.d(TAG, "getWeather called");

                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="
                            +LocationHelper.getLastLocation().getLatitude()+"&lon="+LocationHelper.getLastLocation().getLongitude());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestMethod("POST");
                    // Starts the query
                    conn.connect();


                    // Send json as request
                    OutputStream os = conn.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                    osw.flush();
                    osw.close();

                    // Get response code
                    int responseCode = conn.getResponseCode();
                    Log.d(TAG, "The response code is: " + responseCode);

                    // Get response
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    temperature = mainObject.getDouble("temp");
                    double tempIntoFar= temperature*1.8;
                    temperature = tempIntoFar-459;
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    temperature = Double.parseDouble(df.format(temperature));
                    tempString = String.valueOf(temperature);


                } catch(Exception e) {
                    e.printStackTrace();
                }
            } else {
                // display error
                Log.e(TAG, "Must have internet connectivity to perform this action");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            currentTempString = "Current temp is: " + temperature + " F";
            weatherTextView.setText(currentTempString);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("currentTemp", currentTempString);
        savedInstanceState.putString("tempString", tempString);
        savedInstanceState.putInt("listViewIndex", listViewIndex);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onStop(){
        super.onStop();
        LocationHelper.getInstance().stopLocationUpdates();
    }

}
