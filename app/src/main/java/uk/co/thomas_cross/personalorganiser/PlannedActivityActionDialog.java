package uk.co.thomas_cross.personalorganiser;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.tor.ActivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 19/03/18.
 */

public class PlannedActivityActionDialog extends DialogFragment implements View.OnClickListener {

    private PlannedActivity pa = null;
    private POModel poModel;


    private TextView descriptionView;

    private ImageButton startButton;
    private ImageButton pauseButton;
    private ImageButton doneButton;
    private ImageButton stopButton;
    private ImageButton closeButton;

    private RadioButton notNecessaryButton;
    private RadioButton lackOfTimeButton;
    private RadioButton adverseWeatherButton;
    private RadioButton noAccessButton;


    public PlannedActivityActionDialog() {
    }

    public static PlannedActivityActionDialog newInstance(PlannedActivity pa) {

        PlannedActivityActionDialog frag = new PlannedActivityActionDialog();
        Bundle args = new Bundle();
        args.putSerializable("plannedActivity", pa);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.pa = (PlannedActivity) getArguments().getSerializable("plannedActivity");
        poModel = POModel.getInstance(this.getContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.planned_activity_action, null);

        descriptionView = view.findViewById(R.id.paa_description);

        startButton = view.findViewById(R.id.paa_start);
        startButton.setOnClickListener(this);

        pauseButton = view.findViewById(R.id.paa_pause);
        pauseButton.setOnClickListener(this);

        doneButton = view.findViewById(R.id.paa_done);
        doneButton.setOnClickListener(this);

        stopButton = view.findViewById(R.id.paa_stop);
        stopButton.setOnClickListener(this);

        closeButton = view.findViewById(R.id.paa_close);
        closeButton.setOnClickListener(this);

        notNecessaryButton = view.findViewById(R.id.not_necessary);
        lackOfTimeButton = view.findViewById(R.id.lack_of_time);
        adverseWeatherButton = view.findViewById(R.id.adverse_weather);
        noAccessButton = view.findViewById(R.id.no_access);

        notNecessaryButton.setVisibility(View.GONE);
        lackOfTimeButton.setVisibility(View.GONE);
        adverseWeatherButton.setVisibility(View.GONE);
        noAccessButton.setVisibility(View.GONE);

        descriptionView.setText(pa.getDescription());

        switch (pa.getStatus()) {
            case PlannedActivity.PENDING:
                pauseButton.setVisibility(View.GONE);
                break;
            case PlannedActivity.EXECUTING:
                startButton.setVisibility(View.GONE);
                break;
            case PlannedActivity.PAUSED:
                pauseButton.setVisibility(View.GONE);
                break;
            case PlannedActivity.COMPLETED:
            case PlannedActivity.NOT_NECESSARY:
            case PlannedActivity.LACK_OF_TIME:
            case PlannedActivity.ADVERSE_WEATHER:
            case PlannedActivity.NO_ACCESS:
                // Planned Activitys with these status values are programatically prevented
                // from calling this dialog in Daily Schedule Fragment. Only planned activities
                // that are pending, started or paused are passed to this dialog.
                break;
        }

        builder.setView(view);

        return builder.create();
    }


    private SimpleDateFormat startDateStorageFormat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat startTimeStorageFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat endDateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:sss");

    @Override
    public void onClick(View v) {

        if (v == startButton) {

            // The activity may be pending or paused, in both cases
            // we set the new start date and time to now and update the
            // status to executing

            Calendar timeNow = Calendar.getInstance();
            pa.setStartDate(startDateStorageFormat.format(timeNow.getTime()));
            pa.setStartTime(startTimeStorageFormat.format(timeNow.getTime()));
            pa.setStatus(PlannedActivity.EXECUTING);
            poModel.updatePlannedActivity(pa);
            getDialog().dismiss();

        } else if (v == pauseButton) {

            // The activity is executing therefore we need to calculate how
            // long is has been running in this session and add this to the time taken
            // running total.

            Calendar newStartDateTime = Calendar.getInstance();
            String originalStartDateX = pa.getStartDate();
            String originalYearX = originalStartDateX.substring(0, 4);
            String originalMonthX = originalStartDateX.substring(4, 6);
            String originalDayX = originalStartDateX.substring(6);
            String originalStartTimeX = pa.getStartTime();
            String originalHourX = originalStartTimeX.substring(0, 2);
            String originalMinuteX = originalStartTimeX.substring(3);
            Calendar originalStartDateTime = Calendar.getInstance();
            originalStartDateTime.set(Calendar.YEAR, Integer.parseInt(originalYearX));
            originalStartDateTime.set(Calendar.MONTH, Integer.parseInt(originalMonthX) - 1);
            originalStartDateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(originalDayX));
            originalStartDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(originalHourX));
            originalStartDateTime.set(Calendar.MINUTE, Integer.parseInt(originalMinuteX));
            long duration = newStartDateTime.getTimeInMillis() - originalStartDateTime.getTimeInMillis();
            int timeTaken = pa.getTimeTaken();
            timeTaken += duration / (60 * 1000);  // in minutes
            pa.setTimeTaken(timeTaken);
            pa.setStatus(PlannedActivity.PAUSED);
            poModel.updatePlannedActivity(pa);
            getDialog().dismiss();

        } else if (v == doneButton) {

            Calendar endDateTime = Calendar.getInstance();
            // the activity can be pending, paused, executing
            switch (pa.getStatus()) {
                case PlannedActivity.PENDING:
                    // The activity has not been started. If it is to be flagged as done we assume two things.
                    // Firstly, that it has taken median minutes to complete. Secondly, that the end time
                    // can be considered as the time at which the done button was pressed. From these two
                    // asumptions we recalculate the start date and time as being the end date time minus the
                    // median minutes.
                    int medianMinutes = pa.getMedianMinutes();
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.MINUTE,-medianMinutes);
                    SimpleDateFormat dateFormat = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_yyyyMMdd);
                    pa.setStartDate(dateFormat.format(calendar.getTime()));
                    SimpleDateFormat timeFormat = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_21_59);
                    pa.setStartTime(timeFormat.format(calendar.getTime()));
                    pa.setTimeTaken(medianMinutes);
                    break;
                case PlannedActivity.EXECUTING:
                    String originalStartDateX = pa.getStartDate();
                    String originalYearX = originalStartDateX.substring(0, 4);
                    String originalMonthX = originalStartDateX.substring(4, 6);
                    String originalDayX = originalStartDateX.substring(6);
                    String originalStartTimeX = pa.getStartTime();
                    String originalHourX = originalStartTimeX.substring(0, 2);
                    String originalMinuteX = originalStartTimeX.substring(3);
                    Calendar originalStartDateTime = Calendar.getInstance();
                    originalStartDateTime.set(Calendar.YEAR, Integer.parseInt(originalYearX));
                    originalStartDateTime.set(Calendar.MONTH, Integer.parseInt(originalMonthX) - 1);
                    originalStartDateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(originalDayX));
                    originalStartDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(originalHourX));
                    originalStartDateTime.set(Calendar.MINUTE, Integer.parseInt(originalMinuteX));
                    long duration = endDateTime.getTimeInMillis() - originalStartDateTime.getTimeInMillis();
                    int timeTaken = pa.getTimeTaken();
                    timeTaken += duration / (60 * 1000);  // in minutes
                    pa.setTimeTaken(timeTaken);
                    break;
                case PlannedActivity.PAUSED:
                    break;
            }

            pa.setEndDateTime(endDateTimeFormat.format(endDateTime.getTime()));

            if (notNecessaryButton.isChecked()) {
                pa.setStatus(PlannedActivity.NOT_NECESSARY);
            } else if (lackOfTimeButton.isChecked()) {
                pa.setStatus(PlannedActivity.LACK_OF_TIME);
            } else if (adverseWeatherButton.isChecked()) {
                pa.setStatus(PlannedActivity.ADVERSE_WEATHER);
            } else if (noAccessButton.isChecked()) {
                pa.setStatus(PlannedActivity.NO_ACCESS);
            } else {
                pa.setStatus(PlannedActivity.COMPLETED);
            }
            poModel.updatePlannedActivity(pa);
            getDialog().dismiss();

        } else if (v == stopButton) {

            notNecessaryButton.setVisibility(View.VISIBLE);
            lackOfTimeButton.setVisibility(View.VISIBLE);
            adverseWeatherButton.setVisibility(View.VISIBLE);
            noAccessButton.setVisibility(View.VISIBLE);

            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.GONE);


        } else if (v == closeButton) {
            getDialog().dismiss();
        }
    }


}




