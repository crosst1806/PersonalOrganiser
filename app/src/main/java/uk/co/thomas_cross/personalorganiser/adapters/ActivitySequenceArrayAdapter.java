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
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.model.POModel;

/**
 * Created by thomas on 10/03/18.
 */

public class ActivitySequenceArrayAdapter extends ArrayAdapter {


    public ActivitySequenceArrayAdapter(@NonNull Context context, ArrayList<ActivitySequenceList> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ActivitySequenceList as = (ActivitySequenceList) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_activity_sequence, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.as_id);
        TextView roleView = (TextView) convertView.findViewById(R.id.as_role);
        TextView locationView = (TextView) convertView.findViewById(R.id.as_location);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.as_description);

        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(as.getDatabaseRecordNo()));


        POModel poModel = POModel.getInstance(getContext());
        Role r = poModel.getRole(as.getRole());
        roleView.setText(r.getTitle());

        Location location = poModel.getLocation(as.getLocation());
        locationView.setText(location.getTitle());

        descriptionView.setText(as.getDescription());

        // Return the completed view to render on screen
        return convertView;
    }

}

