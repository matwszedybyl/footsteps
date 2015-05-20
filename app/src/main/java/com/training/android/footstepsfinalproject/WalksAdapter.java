package com.training.android.footstepsfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.training.android.footstepsfinalproject.models.Walk;

import java.util.List;

/**
 * Created by mwszedybyl on 5/3/15.
 */
public class WalksAdapter extends ArrayAdapter<Walk>
{

    private Context context;
    private List<Walk> walkList;

    public WalksAdapter(Context context, int resource, List<Walk> walkList) {
        super(context, resource);
        this.context = context;
        this.walkList = walkList;
    }

    @Override
    public Walk getItem(int position) {
        return walkList.get(position);
    }

    @Override
    public int getCount() {
        return walkList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.walk_list_item, null);
        }

        Walk walk = getItem(position);
        populateView(convertView, walk);

        return convertView;
    }

    private void populateView(View v, final Walk walk) {
        final View rootView = v;
        TextView distanceTextView = (TextView) rootView.findViewById(R.id.walk_distance);
        TextView tempTextView = (TextView) rootView.findViewById(R.id.temp_tv);
        distanceTextView.setText(walk.getDistanceInMeters()+ " meters ");
        if(walk.getTemperatureFormatted()!=null)
        {
            tempTextView.setText("Temperature was: " + walk.getTemperatureFormatted() + " F");
        } else {
            tempTextView.setText(context.getString(R.string.temp_was_not_recorded));

        }


    }

}
