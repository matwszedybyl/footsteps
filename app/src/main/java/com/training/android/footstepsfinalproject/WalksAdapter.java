package com.training.android.footstepsfinalproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.training.android.footstepsfinalproject.models.Walk;

import java.util.ArrayList;

/**
 * Created by mwszedybyl on 5/3/15.
 */
public class WalksAdapter extends CursorAdapter
{

    private ArrayList<Walk> walkList;

    public static class ViewHolder {
        public final TextView walkDistanceView;
        public final TextView tempTextView;

        public ViewHolder(View view) {
            walkDistanceView = (TextView) view.findViewById(R.id.walk_distance);
            tempTextView = (TextView) view.findViewById(R.id.temp_tv);
        }
    }

    public WalksAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        int layoutId = R.layout.walk_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String description = cursor.getString(MainFragment.COL_DISTANCE);
        String temp = cursor.getString(MainFragment.COL_TEMP);
        // Find TextView and set weather forecast on it
        viewHolder.walkDistanceView.setText(description + context.getString(R.string.meters));
        if(temp!=null)
        {
            viewHolder.tempTextView.setText(temp + context.getString(R.string.degree_far));
        }
        else {
            viewHolder.tempTextView.setText(context.getString(R.string.temp_was_not_recorded));

        }

    }

    public void setWalkList(Cursor data){
        walkList = new ArrayList<>();
        data.moveToFirst();
        while(!data.isAfterLast()){
            Walk walk = new Walk();
            walk.setId(data.getInt(MainFragment.COL_WALK_ID));
            walk.getStartingLocation().setLatitude(data.getFloat(MainFragment.COL_STARTING_LAT));
            walk.getStartingLocation().setLongitude(data.getFloat(MainFragment.COL_STARTING_LONG));
            walk.getEndingLocation().setLatitude(data.getFloat(MainFragment.COL_ENDING_LAT));
            walk.getEndingLocation().setLongitude(data.getFloat(MainFragment.COL_ENDING_LONG));
            walk.setDistanceInMeters(data.getFloat(MainFragment.COL_DISTANCE));
            walk.setTemperatureFormatted(data.getString(MainFragment.COL_TEMP));
            walkList.add(walk);
            data.moveToNext();
        }
    }

    @Override
    public Walk getItem(int position) {
        return walkList.get(position);
    }

}
