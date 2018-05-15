package uk.co.thomas_cross.personalorganiser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uk.co.thomas_cross.personalorganiser.R;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivityIteration;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 10/03/18.
 */

public class ActivityIterationArrayAdapter extends ArrayAdapter {


    public ActivityIterationArrayAdapter(@NonNull Context context, ArrayList<ActivityIteration> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ActivityIteration activityIteration = (ActivityIteration) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_activity_iteration, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.ait_id);
        TextView activityView = (TextView) convertView.findViewById(R.id.ait_activity);
        TextView frequencyView = (TextView) convertView.findViewById(R.id.ait_frequency);
        TextView startDateView = (TextView) convertView.findViewById(R.id.ait_start_date);
        TextView endDateView = (TextView) convertView.findViewById(R.id.ait_end_date);

        switch (activityIteration.getStatus()) {
            case ActivityIteration.ENABLED:
                idView.setTextColor(0xFF00CC00);
                activityView.setTextColor(0xFF00CC00);
                frequencyView.setTextColor(0xFF00CC00);
                startDateView.setTextColor(0xFF00CC00);
                endDateView.setTextColor(0xFF00CC00);
                break;
            case ActivityIteration.DISABLED:
                idView.setTextColor(0xFFFF0000);
                activityView.setTextColor(0xFFFF0000);
                frequencyView.setTextColor(0xFFFF0000);
                startDateView.setTextColor(0xFFFF0000);
                endDateView.setTextColor(0xFFFF0000);
                break;
        }

        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(activityIteration.getDatabaseRecordNo()));


        POModel poModel = POModel.getInstance(getContext());

        Activity activity = poModel.getActivity(activityIteration.getActivity());
        activityView.setText(activity.getDescription());
        int frequency = activityIteration.getFrequency();
        String frequencyLegend = "???";
        switch (activityIteration.getFrequencyInterval()) {
            case ActivityIteration.MINUTES:
                frequencyLegend = "Every " + frequency + " Minutes";
                break;
            case ActivityIteration.HOURS:
                frequencyLegend = "Every " + frequency + " Hours";
                break;
            case ActivityIteration.DAYS:
                frequencyLegend = "Every " + frequency + " Days";
                break;
            case ActivityIteration.WEEKS:
                frequencyLegend = "Every " + frequency + " Weeks";
                break;
            case ActivityIteration.MONTHS:
                frequencyLegend = "Every " + frequency + " Months";
                break;
            case ActivityIteration.YEARS:
                frequencyLegend = "Every " + frequency + " Years";
                break;
        }
        frequencyView.setText(frequencyLegend);
        Calendar cal = UtilityHelper.getCalendarFromStartDate(activityIteration.getStartDate());
        SimpleDateFormat sdf = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_Tue_25_Dec_2018);
        startDateView.setText(sdf.format(cal.getTime()));
        cal = UtilityHelper.getCalendarFromStartDate(activityIteration.getEndDate());
        endDateView.setText(sdf.format(cal.getTime()));

        // Return the completed view to render on screen
        return convertView;
    }

}

