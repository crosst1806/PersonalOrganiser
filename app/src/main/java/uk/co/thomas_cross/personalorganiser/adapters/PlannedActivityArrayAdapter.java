package uk.co.thomas_cross.personalorganiser.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uk.co.thomas_cross.personalorganiser.R;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_planned_activity, parent, false);
        }
        // Lookup view for data population
        TextView startDateView = (TextView) convertView.findViewById(R.id.pa_start_date);
        TextView startTimeView = (TextView) convertView.findViewById(R.id.pa_start_time);
//        TextView codeView = (TextView) convertView.findViewById(R.id.pa_code);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.pa_description);
        TextView statusView = (TextView) convertView.findViewById(R.id.pa_status);

        POModel poModel = POModel.getInstance(getContext());
        // Populate the data into the template view using the data object

        Calendar calendar  = Calendar.getInstance();

        String yearX = pa.getStartDate().substring(0,4);
        String monthX = pa.getStartDate().substring(4,6);
        String dayX = pa.getStartDate().substring(6);
        String hourX = pa.getStartTime().substring(0,2);
        String minuteX = pa.getStartTime().substring(3);

        calendar.set(Calendar.YEAR,Integer.parseInt(yearX));
        calendar.set(Calendar.MONTH,Integer.parseInt(monthX)-1);
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dayX));
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hourX));
        calendar.set(Calendar.MINUTE,Integer.parseInt(minuteX));

        SimpleDateFormat displayStartDate = new SimpleDateFormat("dd-MM-yyyy");
        startDateView.setText(displayStartDate.format(calendar.getTime()));

        SimpleDateFormat displayStartTime = new SimpleDateFormat("HH:mm");
        startTimeView.setText(displayStartTime.format(calendar.getTime()));

//        codeView.setText(pa.getCode());
        String time = " 0min";
        String status = "?????";
        switch (pa.getStatus()){
            case PlannedActivity.PENDING:
                startDateView.setTextColor(0xFF00BB00);  // MEDIUM GREEN
                startTimeView.setTextColor(0xFF00BB00);
//                codeView.setTextColor(0xFF00BB00);
                descriptionView.setTextColor(0xFF00BB00);
                statusView.setTextColor(0xFF00BB00);
                status = "Pending";
                time = poModel.formatMedianMinutes(pa.getMedianMinutes());
                break;
            case PlannedActivity.EXECUTING:
                startDateView.setTextColor(Color.BLUE);
                startTimeView.setTextColor(Color.BLUE);
//                codeView.setTextColor(Color.BLUE);
                descriptionView.setTextColor(Color.BLUE);
                statusView.setTextColor(Color.BLUE);
                status = "Executing";
                int timeSoFar = pa.getTimeTaken();
                String startDateAndTime = pa.getStartDate()+pa.getStartTime();
                Calendar startPoint = UtilityHelper.getCalendarFromStartDateAndTime(startDateAndTime);
                long runningTime = Calendar.getInstance().getTimeInMillis()-startPoint.getTimeInMillis();
                timeSoFar += (runningTime/60000);
                time = poModel.formatMedianMinutes(timeSoFar);
                break;
            case PlannedActivity.PAUSED:
                startDateView.setTextColor(0xFFFFA500);
                startTimeView.setTextColor(0xFFFFA500);
//                codeView.setTextColor(Color.CYAN);
                descriptionView.setTextColor(0xFFFFA500);
                statusView.setTextColor(0xFFFFA500);
                status = "Paused";
                time = poModel.formatMedianMinutes(pa.getTimeTaken());
                break;
            case PlannedActivity.COMPLETED:
                startDateView.setTextColor(Color.BLACK);
                startTimeView.setTextColor(Color.BLACK);
//                codeView.setTextColor(Color.BLACK);
                descriptionView.setTextColor(Color.BLACK);
                statusView.setTextColor(Color.BLACK);
                status = "Completed";
                time = poModel.formatMedianMinutes(pa.getTimeTaken());
                break;
            case PlannedActivity.NOT_NECESSARY:
                startDateView.setTextColor(Color.DKGRAY);
                startTimeView.setTextColor(Color.DKGRAY);
//                codeView.setTextColor(Color.DKGRAY);
                descriptionView.setTextColor(Color.DKGRAY);
                statusView.setTextColor(Color.DKGRAY);
                status = "Not Necessary";
                time = poModel.formatMedianMinutes(pa.getTimeTaken());
                break;
            case PlannedActivity.LACK_OF_TIME:
                startDateView.setTextColor(Color.RED);
                startTimeView.setTextColor(Color.RED);
//                codeView.setTextColor(Color.RED);
                descriptionView.setTextColor(Color.RED);
                statusView.setTextColor(Color.RED);
                status = "lack of Time";
                time = poModel.formatMedianMinutes(pa.getTimeTaken());
                break;
            case PlannedActivity.ADVERSE_WEATHER:
                startDateView.setTextColor(Color.RED);
                startTimeView.setTextColor(Color.RED);
//                codeView.setTextColor(Color.RED);
                descriptionView.setTextColor(Color.RED);
                statusView.setTextColor(Color.RED);
                status = "Adverse Weather";
                time = poModel.formatMedianMinutes(pa.getTimeTaken());
                break;
            case PlannedActivity.NO_ACCESS:
                startDateView.setTextColor(Color.RED);
                startTimeView.setTextColor(Color.RED);
//                codeView.setTextColor(Color.RED);
                descriptionView.setTextColor(Color.RED);
                statusView.setTextColor(Color.RED);
                status = "No Access";
                time = poModel.formatMedianMinutes(pa.getTimeTaken());
                break;
        }
        descriptionView.setText(pa.getDescription()+" ("+time+")");
        statusView.setText(status);

        // Return the completed view to render on screen
        return convertView;
    }

}

