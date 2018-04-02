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
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.model.POModel;

/**
 * Created by thomas on 10/03/18.
 */

public class PlannedActivityArrayAdapter extends ArrayAdapter {


    public PlannedActivityArrayAdapter(@NonNull Context context, ArrayList<PlannedActivity> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        PlannedActivity pa = (PlannedActivity) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.planned_activity_list_item, parent, false);
        }
        // Lookup view for data population
        TextView startDateView = (TextView) convertView.findViewById(R.id.pa_start_date);
        TextView startTimeView = (TextView) convertView.findViewById(R.id.pa_start_time);
        TextView codeView = (TextView) convertView.findViewById(R.id.pa_code);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.pa_description);
        TextView statusView = (TextView) convertView.findViewById(R.id.pa_status);

        POModel poModel = new POModel(getContext());
        // Populate the data into the template view using the data object
        startDateView.setText(pa.getStartDate());
        startTimeView.setText(pa.getStartTime());
        codeView.setText(pa.getCode());
        descriptionView.setText(pa.getDescription());
        String status = "?????";
        switch (pa.getStatus()){
            case PlannedActivity.PENDING:
                status = "";
                break;
            case PlannedActivity.EXECUTING:
                status = "";
                break;
            case PlannedActivity.PAUSED:
                status = "";
                break;
            case PlannedActivity.COMPLETED:
                status = "";
                break;
            case PlannedActivity.NOT_NECESSARY:
                status = "";
                break;
            case PlannedActivity.LACK_OF_TIME:
                status = "";
                break;
            case PlannedActivity.ADVERSE_WEATHER:
                status = "";
                break;
            case PlannedActivity.NO_ACCESS:
                status = "";
                break;
        }
        statusView.setText(status);

        // Return the completed view to render on screen
        return convertView;
    }

}

