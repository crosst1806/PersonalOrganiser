package uk.co.thomas_cross.personalorganiser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.R;
import uk.co.thomas_cross.personalorganiser.entities.Location;

/**
 * Created by thomas on 10/03/18.
 */

public class LocationsArrayAdapter extends ArrayAdapter {


    public LocationsArrayAdapter(@NonNull Context context, ArrayList<Location> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Location location = (Location) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_location, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.l_record_no);
        TextView titleView = (TextView) convertView.findViewById(R.id.l_title);

        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(location.getDatabaseRecordNo()));
        titleView.setText(location.getTitle());

        // Return the completed view to render on screen
        return convertView;
    }

}

