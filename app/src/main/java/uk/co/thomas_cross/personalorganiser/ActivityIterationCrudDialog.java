package uk.co.thomas_cross.personalorganiser;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import uk.co.thomas_cross.personalorganiser.adapters.ActivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivityIteration;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.Location;
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

public class ActivityIterationCrudDialog extends DialogFragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private Spinner roleSpinner;
    private Spinner locationSpinner;
    private Spinner activitySpinner;
    private Spinner prioritySpinner;
    private Spinner frequencyIntervalSpinner;
    private Spinner dataSensitivitySpinner;

    private ActivityIteration activityIteration = null;

    private ArrayAdapter dsAdapter;
    private ArrayAdapter roleAdapter;
    private ArrayAdapter locationsArrayAdapter;
    private ArrayAdapter activitysArrayAdapter;
    private ArrayAdapter<Integer> priorityAdapter;
    private ArrayAdapter<Integer> frequencyIntervalAdapter;

    private ArrayList<DataSensitivity> dataSensitivities = new ArrayList<DataSensitivity>();
    private ArrayList<Role> roles = new ArrayList<Role>();
    private ArrayList<Location> locations = new ArrayList<Location>();
    private ArrayList<Activity> activitys = new ArrayList<Activity>();

    private Activity activity = null;

    private DataSensitivityTor dsTor = new DataSensitivityTor();
    private RoleTor roleTor = new RoleTor();
    private LocationTor locationTor = new LocationTor();
    private ActivityTor activityTor = new ActivityTor();


    private POModel model = null;

    private TextView startDateView = null;
    private TextView startTimeView = null;
    private TextView endDateView = null;
    private TextView endTimeView = null;


    private EditText startDateInput = null;
    private EditText startTimeInput = null;
    private EditText endDateInput = null;
    private EditText endTimeInput = null;

    private DatePickerDialog startDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog endTimePickerDialog;

    private int oldYear = 0;
    private int oldMonth = 0;
    private int oldDay = 0;
    private int oldHour = 0;
    private int oldMinute = 0;

    private CheckBox mondayCheckBox = null;
    private CheckBox tuesdayCheckBox = null;
    private CheckBox wednesdayCheckBox = null;
    private CheckBox thursdayCheckBox = null;
    private CheckBox fridayCheckBox = null;
    private CheckBox saturdayCheckBox = null;
    private CheckBox sundayCheckBox = null;


    public ActivityIterationCrudDialog() {
    }

    public static ActivityIterationCrudDialog newInstance(int mode, ActivityIteration activityIteration) {

        ActivityIterationCrudDialog frag = new ActivityIterationCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("activityIteration", activityIteration);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.activityIteration = (ActivityIteration) getArguments().getSerializable("activityIteration");
        this.model = POModel.getInstance(this.getContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_activity_iteration, null);

        final TextView recordNoLabel = view.findViewById(R.id.ai_id_label);
        final TextView recordNoView = view.findViewById(R.id.ai_id_view);

        final TextView ownerViewLabel = view.findViewById(R.id.ai_owner_label);
        final TextView ownerView = view.findViewById(R.id.ai_owner_view);

        final TextView ownerTypeLabel = view.findViewById(R.id.ai_owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.ai_owner_type_view);

        final TextView roleLabel = view.findViewById(R.id.ai_role_label);
        final TextView roleView = view.findViewById(R.id.ai_role_view);
        roleSpinner = view.findViewById(R.id.ai_role_spinner);

        final TextView locationView = view.findViewById(R.id.ai_location_view);
        locationSpinner = view.findViewById(R.id.ai_location_spinner);

        final TextView activityView = view.findViewById(R.id.ai_activity_view);
        activitySpinner = view.findViewById(R.id.ai_activity_spinner);

        final TextView priorityView = view.findViewById(R.id.ai_priority_view);
        prioritySpinner = view.findViewById(R.id.ai_priority_spinner);

        final TextView frequencyIntervalView = view.findViewById(R.id.ai_frequency_interval_view);
        frequencyIntervalSpinner = view.findViewById(R.id.ai_frequency_interval_spinner);

        final TextView frequencyView = view.findViewById(R.id.ai_frequency_view);
        final EditText frequencyInput = view.findViewById(R.id.ai_frequency_input);

        final TextView exemptedDaysView = view.findViewById(R.id.ai_exempted_days_view);

        mondayCheckBox = view.findViewById(R.id.ai_monday);
        tuesdayCheckBox = view.findViewById(R.id.ai_tuesday);
        wednesdayCheckBox = view.findViewById(R.id.ai_wednesday);
        thursdayCheckBox = view.findViewById(R.id.ai_thursday);
        fridayCheckBox = view.findViewById(R.id.ai_friday);
        saturdayCheckBox = view.findViewById(R.id.ai_saturday);
        sundayCheckBox = view.findViewById(R.id.ai_sunday);

        startDateView = view.findViewById(R.id.ai_start_date_view);
        startDateInput = view.findViewById(R.id.ai_start_date_input);
        startDateInput.setInputType(InputType.TYPE_NULL);
        startDateInput.setOnClickListener(this);

        endDateView = view.findViewById(R.id.ai_end_date_view);
        endDateInput = view.findViewById(R.id.ai_end_date_input);
        endDateInput.setInputType(InputType.TYPE_NULL);
        endDateInput.setOnClickListener(this);

        final TextView startTimeLabel = view.findViewById(R.id.ai_start_time_label);
        startTimeView = view.findViewById(R.id.ai_start_time_view);
        startTimeInput = view.findViewById(R.id.ai_start_time_input);
        startTimeInput.setInputType(InputType.TYPE_NULL);
        startTimeInput.setOnClickListener(this);

        final TextView endTimeLabel = view.findViewById(R.id.ai_end_time_label);
        endTimeView = view.findViewById(R.id.ai_end_time_view);
        endTimeInput = view.findViewById(R.id.ai_end_time_input);
        endTimeInput.setInputType(InputType.TYPE_NULL);
        endTimeInput.setOnClickListener(this);

        final TextView statusLabel = view.findViewById(R.id.ai_status_label);
        final TextView statusView = view.findViewById(R.id.ai_status_view);

        final TextView dataSensitivityTextView = view.findViewById(R.id.ai_data_sensitivity_view);
        dataSensitivitySpinner = view.findViewById(R.id.ai_data_sensitivity_spinner);

        final TextView timeStampLabel = view.findViewById(R.id.ai_time_stamp_label);
        final TextView timeStampView = view.findViewById(R.id.ai_time_stamp_view);

        final TextView lastModifiedByLabel = view.findViewById(R.id.ai_lmb_label);
        final TextView lastModifiedBy = view.findViewById(R.id.ai_lmb_view);

        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);

        recordNoView.setText(String.valueOf(activityIteration.getDatabaseRecordNo()));

        ownerView.setText(
                model.getUserTitle(
                        activityIteration.getOwner(),
                        activityIteration.getOwnerType()));

        ownerTypeView.setText(
                model.getOwnerType(activityIteration.getOwnerType())
        );

        Role role = model.getRole(activityIteration.getRole());
        if (role != null)
            roleView.setText(role.getTitle());

        roles = model.getRoles();

        roleAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                roles);
        roleSpinner.setAdapter(roleAdapter);
        roleAdapter.sort(new RoleTor());
        roleSpinner.setOnItemSelectedListener(this);
        UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, role);

        Location location = model.getLocation(activityIteration.getLocation());
        if (location != null)
            locationView.setText(location.getTitle());
        locations = model.getLocations();

        locationsArrayAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                locations);
        locationSpinner.setAdapter(locationsArrayAdapter);
        locationsArrayAdapter.sort(new LocationTor());
        locationSpinner.setOnItemSelectedListener(this);
        UtilityHelper.selectSpinnerIndex(locationSpinner, locationsArrayAdapter, location);

        activity = model.getActivity(activityIteration.getActivity());
        if (activity != null)
            activityView.setText(activity.getDescription());
        activitys = model.getActivitysByRoleAndLocation(
                role.getDatabaseRecordNo(), location.getDatabaseRecordNo());
        activitysArrayAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                activitys);
        activitySpinner.setAdapter(activitysArrayAdapter);
        activitysArrayAdapter.sort(activityTor);
        UtilityHelper.selectSpinnerIndex(activitySpinner, activitysArrayAdapter, activity);

        priorityView.setText(String.valueOf(activity.getPriority()));
        ArrayList<Integer> priorities = new ArrayList<Integer>();
        priorities.add(new Integer(01));
        priorities.add(new Integer(02));
        priorities.add(new Integer(03));
        priorities.add(new Integer(04));
        priorities.add(new Integer(05));
        priorityAdapter = new ArrayAdapter(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                priorities);
        prioritySpinner.setAdapter(priorityAdapter);
        UtilityHelper.selectSpinnerIndex(prioritySpinner, priorityAdapter,
                Integer.valueOf(activityIteration.getPriority()));

        ArrayList<String> frequencyIntervals = new ArrayList<String>();
        frequencyIntervals.add("Every n Minutes");
        frequencyIntervals.add("Every n Hours");
        frequencyIntervals.add("Every n Days");
        frequencyIntervals.add("Every n Weeks");
        frequencyIntervals.add("Every n Months");
        frequencyIntervals.add("Every n Years");
        frequencyIntervalAdapter = new ArrayAdapter(getContext(),
                R.layout.support_simple_spinner_dropdown_item, frequencyIntervals);
        frequencyIntervalSpinner.setAdapter(frequencyIntervalAdapter);

        String fq = "???";
        switch (activityIteration.getFrequencyInterval()) {
            case ActivityIteration.MINUTES:
            case ActivityIteration.HOURS:
            case ActivityIteration.DAYS:
            case ActivityIteration.WEEKS:
            case ActivityIteration.MONTHS:
            case ActivityIteration.YEARS:
                int y = activityIteration.getFrequencyInterval();
                frequencyIntervalView.setText(frequencyIntervals.get(y));
                break;
        }

        frequencyView.setText("" + activityIteration.getFrequency());
        frequencyInput.setText("" + activityIteration.getFrequency());

        String exemptedDays = activityIteration.getExemptedDays();
        if (exemptedDays == null || exemptedDays.length() < 7)
            exemptedDays = "[-------]";
        exemptedDaysView.setText("["+exemptedDays+"]");

        if (exemptedDays.substring(0, 1).equals("M")) {
            mondayCheckBox.setChecked(true);
        }
        if (exemptedDays.substring(1, 2).equals("T")) {
            tuesdayCheckBox.setChecked(true);
        }
        if (exemptedDays.substring(2, 3).equals("W")) {
            wednesdayCheckBox.setChecked(true);
        }
        if (exemptedDays.substring(3, 4).equals("T")) {
            thursdayCheckBox.setChecked(true);
        }
        if (exemptedDays.substring(4, 5).equals("F")) {
            fridayCheckBox.setChecked(true);
        }
        if (exemptedDays.substring(5, 6).equals("S")) {
            saturdayCheckBox.setChecked(true);
        }
        if (exemptedDays.substring(6).equals("S")) {
            sundayCheckBox.setChecked(true);
        }


        Calendar calendar = Calendar.getInstance();
        if (activityIteration.getStartDate().length() < 8)
            activityIteration.setStartDate("19620618");
        if (activityIteration.getStartTime().length() < 5)
            activityIteration.setStartTime("01:01");

        String yearX = activityIteration.getStartDate().substring(0, 4);
        String monthX = activityIteration.getStartDate().substring(4, 6);
        String dayX = activityIteration.getStartDate().substring(6);
        String hourX = activityIteration.getStartTime().substring(0, 2);
        String minuteX = activityIteration.getStartTime().substring(3);

        calendar.set(Calendar.YEAR, Integer.parseInt(yearX));
        calendar.set(Calendar.MONTH, Integer.parseInt(monthX) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayX));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourX));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minuteX));

        SimpleDateFormat date_format_dd_MM_yyyy = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_25_12_2018);
        SimpleDateFormat time_format_HHmm = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_21_59);

        startDateView.setText(date_format_dd_MM_yyyy.format(calendar.getTime()));
        startDateInput.setText(date_format_dd_MM_yyyy.format(calendar.getTime()));

        startTimeView.setText(time_format_HHmm.format(calendar.getTime()));
        startTimeInput.setText(time_format_HHmm.format(calendar.getTime()));

        yearX = activityIteration.getEndDate().substring(0, 4);
        monthX = activityIteration.getEndDate().substring(4, 6);
        dayX = activityIteration.getEndDate().substring(6);
        hourX = activityIteration.getEndTime().substring(0, 2);
        minuteX = activityIteration.getEndTime().substring(3);

        calendar.set(Calendar.YEAR, Integer.parseInt(yearX));
        calendar.set(Calendar.MONTH, Integer.parseInt(monthX) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayX));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourX));
        calendar.set(Calendar.MINUTE, Integer.parseInt(minuteX));

        endDateView.setText(date_format_dd_MM_yyyy.format(calendar.getTime()));
        endDateInput.setText(date_format_dd_MM_yyyy.format(calendar.getTime()));

        endTimeView.setText(time_format_HHmm.format(calendar.getTime()));
        endTimeInput.setText(time_format_HHmm.format(calendar.getTime()));

        switch (activityIteration.getStatus()){
            case ActivityIteration.ENABLED:
                statusView.setText("Enabled");
                break;
            case ActivityIteration.DISABLED:
                statusView.setText("Disabled");
                break;
        }

        DataSensitivity ds = model.getDataSensitivity(activityIteration.getDataSensitivity());
        if (ds != null)
            dataSensitivityTextView.setText(ds.getTitle());

        dataSensitivities = model.getDataSensitivitys();
        dsAdapter = new ArrayAdapter(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                dataSensitivities);
        dataSensitivitySpinner.setAdapter(dsAdapter);
        dsAdapter.sort(new DataSensitivityTor());
        UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dsAdapter, ds);
        Log.i(TAG,"timeStamp is "+activityIteration.getTimeStamp());
        Calendar cal = UtilityHelper.getCalendarFromTimeStamp(activityIteration.getTimeStamp());
        SimpleDateFormat sdf = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_Tue_25_Dec_2018_23_59);
        timeStampView.setText(sdf.format(cal.getTime()));

        lastModifiedBy.setText(
                model.getUserName(activityIteration.getLastModifiedBy())
        );

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Delete");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.VISIBLE);

                roleSpinner.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);


            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.deleteActivityIteration(activityIteration.getDatabaseRecordNo());
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Role role1 = (Role) roleSpinner.getSelectedItem();
                if (role1 != null)
                    activityIteration.setRole(role1.getDatabaseRecordNo());

                Location location1 = (Location) locationSpinner.getSelectedItem();
                if (location1 != null)
                    activityIteration.setLocation(location1.getDatabaseRecordNo());

                Activity activity1 = (Activity) activitySpinner.getSelectedItem();
                if (activity1 != null)
                    activityIteration.setActivity(activity1.getDatabaseRecordNo());

                Integer priority = (Integer) prioritySpinner.getSelectedItem();
                if (priority != null)
                    activityIteration.setPriority(priority.intValue());

                int frequency = Integer.parseInt(frequencyInput.getText().toString());
                activityIteration.setFrequency(frequency);

                int frequencyInterval = frequencyIntervalSpinner.getSelectedItemPosition();
                activityIteration.setFrequencyInterval(frequencyInterval);

                StringBuffer sb = new StringBuffer();
                if (mondayCheckBox.isChecked()) sb.append("M"); else sb.append("-");
                if (tuesdayCheckBox.isChecked()) sb.append("T"); else sb.append("-");
                if (wednesdayCheckBox.isChecked()) sb.append("W"); else sb.append("-");
                if (thursdayCheckBox.isChecked()) sb.append("T"); else sb.append("-");
                if (fridayCheckBox.isChecked()) sb.append("F"); else sb.append("-");
                if (saturdayCheckBox.isChecked()) sb.append("S"); else sb.append("-");
                if (sundayCheckBox.isChecked()) sb.append("S"); else sb.append("-");

                activityIteration.setExemptedDays(sb.toString());

                String startDateDisplay = startDateInput.getText().toString();
                String startDateStorage =
                        startDateDisplay.substring(6) +
                                startDateDisplay.substring(3, 5) +
                                startDateDisplay.substring(0, 2);
                Log.i(TAG,"startDateStorage = "+startDateStorage);
                activityIteration.setStartDate(startDateStorage);

                activityIteration.setStartTime(startTimeInput.getText().toString());

                String endDateDisplay = endDateInput.getText().toString();
                String endDateStorage =
                        endDateDisplay.substring(6) +
                                endDateDisplay.substring(3, 5) +
                                endDateDisplay.substring(0, 2);
                Log.i(TAG,"endDateStorage = " + endDateStorage);
                activityIteration.setEndDate(endDateStorage);

                activityIteration.setEndTime(endTimeInput.getText().toString());


                DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                if (ds != null)
                    activityIteration.setDataSensitivity(ds.getDatabaseRecordNo());

                model.updateActivityIteration(activityIteration);
                getDialog().dismiss();

            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Update");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);
                ownerViewLabel.setVisibility(View.GONE);
                ownerView.setVisibility(View.GONE);
                ownerTypeLabel.setVisibility(View.GONE);
                ownerTypeView.setVisibility(View.GONE);
                roleView.setVisibility(View.GONE);
                locationView.setVisibility(View.GONE);
                activityView.setVisibility(View.GONE);
                frequencyView.setVisibility(View.GONE);
                frequencyIntervalView.setVisibility(View.GONE);
                exemptedDaysView.setVisibility(View.GONE);
                priorityView.setVisibility(View.GONE);
                startDateView.setVisibility(View.GONE);
                startTimeView.setVisibility(View.GONE);
                endDateView.setVisibility(View.GONE);
                endTimeView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);


                roleSpinner.setVisibility(View.VISIBLE);
                locationSpinner.setVisibility(View.VISIBLE);
                activitySpinner.setVisibility(View.VISIBLE);

                UtilityHelper.selectSpinnerIndex(activitySpinner, activitysArrayAdapter, activity);


                prioritySpinner.setVisibility(View.VISIBLE);
                frequencyIntervalSpinner.setVisibility(View.VISIBLE);
                frequencyIntervalSpinner.setSelection(activityIteration.getFrequencyInterval());

                frequencyInput.setVisibility(View.VISIBLE);
                mondayCheckBox.setVisibility(View.VISIBLE);
                tuesdayCheckBox.setVisibility(View.VISIBLE);
                wednesdayCheckBox.setVisibility(View.VISIBLE);
                thursdayCheckBox.setVisibility(View.VISIBLE);
                fridayCheckBox.setVisibility(View.VISIBLE);
                saturdayCheckBox.setVisibility(View.VISIBLE);
                sundayCheckBox.setVisibility(View.VISIBLE);

                startDateInput.setVisibility(View.VISIBLE);
                startTimeInput.setVisibility(View.VISIBLE);
                endDateInput.setVisibility(View.VISIBLE);
                endTimeInput.setVisibility(View.VISIBLE);

                dataSensitivitySpinner.setVisibility(View.VISIBLE);
                dataSensitivityTextView.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);
            }

        });

        switch (mode) {
            case CREATE_MODE:

                dialogTitle.setText("Create");

                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);
                ownerViewLabel.setVisibility(View.GONE);
                ownerView.setVisibility(View.GONE);
                ownerTypeLabel.setVisibility(View.GONE);
                ownerTypeView.setVisibility(View.GONE);
                roleView.setVisibility(View.GONE);
                locationView.setVisibility(View.GONE);
                activityView.setVisibility(View.GONE);
                priorityView.setVisibility(View.GONE);
                frequencyIntervalView.setVisibility(View.GONE);
                frequencyView.setVisibility(View.GONE);
                exemptedDaysView.setVisibility(View.GONE);
                startDateView.setVisibility(View.GONE);
                startTimeView.setVisibility(View.GONE);
                endDateView.setVisibility(View.GONE);
                endTimeView.setVisibility(View.GONE);
                dataSensitivityTextView.setVisibility(View.GONE);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Role role1 = (Role) roleSpinner.getSelectedItem();
                        if (role1 != null)
                            activityIteration.setRole(role1.getDatabaseRecordNo());

                        Location location1 = (Location) locationSpinner.getSelectedItem();
                        if (location1 != null)
                            activityIteration.setLocation(location1.getDatabaseRecordNo());

                        Activity activity1 = (Activity) activitySpinner.getSelectedItem();
                        if (activity1 != null)
                            activityIteration.setActivity(activity1.getDatabaseRecordNo());

                        Integer priority = (Integer) prioritySpinner.getSelectedItem();
                        if (priority != null)
                            activityIteration.setPriority(priority.intValue());

                        int frequency = Integer.parseInt(frequencyInput.getText().toString());
                        activityIteration.setFrequency(frequency);

                        int frequencyInterval = frequencyIntervalSpinner.getSelectedItemPosition();
                        activityIteration.setFrequencyInterval(frequencyInterval);

                        StringBuffer sb = new StringBuffer();
                        if (mondayCheckBox.isChecked()) sb.append("M"); else sb.append("-");
                        if (tuesdayCheckBox.isChecked()) sb.append("T"); else sb.append("-");
                        if (wednesdayCheckBox.isChecked()) sb.append("W"); else sb.append("-");
                        if (thursdayCheckBox.isChecked()) sb.append("T"); else sb.append("-");
                        if (fridayCheckBox.isChecked()) sb.append("F"); else sb.append("-");
                        if (saturdayCheckBox.isChecked()) sb.append("S"); else sb.append("-");
                        if (sundayCheckBox.isChecked()) sb.append("S"); else sb.append("-");

                        activityIteration.setExemptedDays(sb.toString());

                        String startDateDisplay = startDateInput.getText().toString();
                        String startDateStorage =
                                startDateDisplay.substring(6) +
                                        startDateDisplay.substring(3, 5) +
                                        startDateDisplay.substring(0, 2);
                        activityIteration.setStartDate(startDateStorage);

                        activityIteration.setStartTime(startTimeInput.getText().toString());

                        String endDateDisplay = endDateInput.getText().toString();
                        String endDateStorage =
                                endDateDisplay.substring(6) +
                                        endDateDisplay.substring(3, 5) +
                                        endDateDisplay.substring(0, 2);
                        activityIteration.setEndDate(endDateStorage);

                        activityIteration.setEndTime(endTimeInput.getText().toString());


                        DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                        if (ds != null)
                            activityIteration.setDataSensitivity(ds.getDatabaseRecordNo());

                        model.addActivityIteration(activityIteration);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);


                break;

            case READ_MODE:

                dialogTitle.setText("View");
                dataSensitivitySpinner.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);

                roleSpinner.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.GONE);
                activitySpinner.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);
                frequencyIntervalSpinner.setVisibility(View.GONE);

                frequencyInput.setVisibility(View.GONE);
                startDateInput.setVisibility(View.GONE);
                startTimeInput.setVisibility(View.GONE);
                endDateInput.setVisibility(View.GONE);
                endTimeInput.setVisibility(View.GONE);

                exemptedDaysView.setVisibility(View.VISIBLE);
                mondayCheckBox.setVisibility(View.GONE);
                tuesdayCheckBox.setVisibility(View.GONE);
                wednesdayCheckBox.setVisibility(View.GONE);
                thursdayCheckBox.setVisibility(View.GONE);
                fridayCheckBox.setVisibility(View.GONE);
                saturdayCheckBox.setVisibility(View.GONE);
                sundayCheckBox.setVisibility(View.GONE);

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
        }
        builder.setCustomTitle(dialogHeader);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Role r = null;
        Location l = null;

        if (parent == roleSpinner) {

            r = (Role) roleSpinner.getSelectedItem();
            DataSensitivity ds = model.getDataSensitivity(r.getDataSensitivity());
            UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dsAdapter, ds);

            l = (Location) locationSpinner.getSelectedItem();
            if (l == null)
                l = new Location();


        } else if (parent == locationSpinner) {

            l = (Location) locationSpinner.getSelectedItem();
            r = (Role) roleSpinner.getSelectedItem();
            if (r == null)
                r = new Role();

        }

        activitys = model.getActivitysByRoleAndLocation(r.getDatabaseRecordNo(),
                l.getDatabaseRecordNo());
        activitysArrayAdapter.clear();
        activitysArrayAdapter.addAll(activitys);
        activitysArrayAdapter.sort(activityTor);
        activitysArrayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {

        if (v == startDateInput) {

            calcDate = Calendar.getInstance();
            String oldDateX = startDateInput.getText().toString();
            try {

                if (oldDateX.length() >= 10) { //dd-mm-yyyy 10 chars
                    oldYear = Integer.parseInt(oldDateX.substring(6, 10));
                    oldMonth = Integer.parseInt(oldDateX.substring(3, 5));
                    oldMonth--;
                    if (oldMonth == -1)
                        oldMonth = 11;
                    oldDay = Integer.parseInt(oldDateX.substring(0, 2));
                } else {
                    oldYear = calcDate.get(Calendar.YEAR);
                    oldMonth = calcDate.get(Calendar.MONTH);
                    oldDay = calcDate.get(Calendar.DAY_OF_MONTH);
                }

            } catch (Exception e) {

                oldYear = calcDate.get(Calendar.YEAR);
                oldMonth = calcDate.get(Calendar.MONTH);
                oldDay = calcDate.get(Calendar.DAY_OF_MONTH);

            }
            initialiseStartDatePicker();
            startDatePickerDialog.show();


        } else if (v == endDateInput) {

            calcDate = Calendar.getInstance();
            String oldDateX = endDateInput.getText().toString();
            try {

                if (oldDateX.length() >= 10) { //dd-mm-yyyy 10 chars
                    oldYear = Integer.parseInt(oldDateX.substring(6, 10));
                    oldMonth = Integer.parseInt(oldDateX.substring(3, 5));
                    oldMonth--;
                    if (oldMonth == -1)
                        oldMonth = 11;
                    oldDay = Integer.parseInt(oldDateX.substring(0, 2));
                } else {
                    oldYear = calcDate.get(Calendar.YEAR);
                    oldMonth = calcDate.get(Calendar.MONTH);
                    oldDay = calcDate.get(Calendar.DAY_OF_MONTH);
                }

            } catch (Exception e) {

                oldYear = calcDate.get(Calendar.YEAR);
                oldMonth = calcDate.get(Calendar.MONTH);
                oldDay = calcDate.get(Calendar.DAY_OF_MONTH);

            }
            initialiseEndDatePicker();
            endDatePickerDialog.show();


        } else if (v == startTimeInput) {

            calcDate = Calendar.getInstance();
            String oldTimeX = startTimeInput.getText().toString();
            try {

                if (oldTimeX.length() >= 5) { //HH:mm 5 chars
                    oldHour = Integer.parseInt(oldTimeX.substring(0, 2));
                    oldMinute = Integer.parseInt(oldTimeX.substring(3, 5));
                } else {
                    oldHour = calcDate.get(Calendar.HOUR_OF_DAY);
                    oldMinute = calcDate.get(Calendar.MINUTE);
                }

            } catch (Exception e) {

                oldHour = calcDate.get(Calendar.HOUR_OF_DAY);
                oldMinute = calcDate.get(Calendar.MINUTE);

            }
            initialiseStartTimePicker();
            startTimePickerDialog.show();

        } else if (v == endTimeInput) {

            calcDate = Calendar.getInstance();
            String oldTimeX = endTimeInput.getText().toString();
            try {

                if (oldTimeX.length() >= 5) { //HH:mm 5 chars
                    oldHour = Integer.parseInt(oldTimeX.substring(0, 2));
                    oldMinute = Integer.parseInt(oldTimeX.substring(3, 5));
                } else {
                    oldHour = calcDate.get(Calendar.HOUR_OF_DAY);
                    oldMinute = calcDate.get(Calendar.MINUTE);
                }

            } catch (Exception e) {

                oldHour = calcDate.get(Calendar.HOUR_OF_DAY);
                oldMinute = calcDate.get(Calendar.MINUTE);

            }
            initialiseEndTimePicker();
            endTimePickerDialog.show();
        }

    }

    private Calendar calcDate = Calendar.getInstance();
    SimpleDateFormat dateDisplayFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    SimpleDateFormat timeDisplayFormat = new SimpleDateFormat("HH:mm");


    private void initialiseStartDatePicker() {

        startDatePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calcDate.set(year, monthOfYear, dayOfMonth);
                        startDateView.setText(dateDisplayFormat.format(calcDate.getTime()));
                        startDateInput.setText(dateDisplayFormat.format(calcDate.getTime()));
                    }

                }, oldYear, oldMonth, oldDay);

    }

    private void initialiseStartTimePicker() {

        startTimePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calcDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calcDate.set(Calendar.MINUTE, minute);
                        startTimeView.setText(timeDisplayFormat.format(calcDate.getTime()));
                        startTimeInput.setText(timeDisplayFormat.format(calcDate.getTime()));
                    }
                }, oldHour, oldMinute, true);
    }

    private void initialiseEndDatePicker() {

        endDatePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calcDate.set(year, monthOfYear, dayOfMonth);
                        endDateView.setText(dateDisplayFormat.format(calcDate.getTime()));
                        endDateInput.setText(dateDisplayFormat.format(calcDate.getTime()));
                    }

                }, oldYear, oldMonth, oldDay);

    }

    private void initialiseEndTimePicker() {

        endTimePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calcDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calcDate.set(Calendar.MINUTE, minute);
                        endTimeView.setText(timeDisplayFormat.format(calcDate.getTime()));
                        endTimeInput.setText(timeDisplayFormat.format(calcDate.getTime()));
                    }
                }, oldHour, oldMinute, true);
    }

}
