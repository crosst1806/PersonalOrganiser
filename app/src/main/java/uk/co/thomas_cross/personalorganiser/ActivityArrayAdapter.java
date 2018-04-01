package uk.co.thomas_cross.personalorganiser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.model.POModel;

/**
 * Created by thomas on 10/03/18.
 */

public class ActivityArrayAdapter extends ArrayAdapter {


    public ActivityArrayAdapter(@NonNull Context context, ArrayList<Activity> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Activity activity = (Activity) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list_item, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.act_id);
        TextView roleView = (TextView) convertView.findViewById(R.id.act_role);
        TextView locationView = (TextView) convertView.findViewById(R.id.act_location);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.act_description);
        TextView priorityView = (TextView) convertView.findViewById(R.id.act_priority);
        TextView medianMinutesview = convertView.findViewById(R.id.act_median_minutes);

        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(activity.getDatabaseRecordNo()));


        POModel poModel = new POModel(getContext());
        Role r = poModel.getRole(activity.getRole());
        roleView.setText(r.getTitle());

        Location location = poModel.getLocation(activity.getLocation());
        locationView.setText(location.getTitle());

        descriptionView.setText(activity.getDescription());

        priorityView.setText(String.valueOf(activity.getPriority()));

        medianMinutesview.setText(String.valueOf(activity.getMedianMinutes()));
        // Return the completed view to render on screen
        return convertView;
    }

}

