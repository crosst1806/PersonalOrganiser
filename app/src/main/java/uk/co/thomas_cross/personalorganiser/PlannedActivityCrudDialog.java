package uk.co.thomas_cross.personalorganiser;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.entities.tor.ActivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 19/03/18.
 */

public class PlannedActivityCrudDialog extends DialogFragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private PlannedActivity pa = null;
    private POModel poModel;

    private Spinner roleSpinner;
    private ArrayAdapter roleAdapter;
    private ArrayList<Role> roles;

    private Spinner locationSpinner;
    private ArrayAdapter locationAdapter;

    private Spinner activitySpinner;
    private ArrayAdapter activityAdapter;

    private Spinner prioritySpinner;
    private ArrayAdapter priorityAdapter;

    private Spinner dataSensitivitySpinner;
    private ArrayAdapter dataSensitivityAdapter;
    private ArrayList<DataSensitivity> dataSensitivitys;

    private TextView recordNoLabel;
    private TextView recordNoView;
    private TextView ownerViewLabel;
    private TextView ownerView;
    private TextView ownerTypeLabel;
    private TextView ownerTypeView;

    private TextView roleLabel;
    private TextView roleView;

    private TextView locationLabel;
    private TextView locationView;

    private TextView activityLabel;
    private TextView activityView;

    private TextView codeLabel;
    private TextView codeView;

    private TextView descriptionLabel;
    private TextView descriptionView;
    private EditText descriptionEdit;

    private TextView medianMinutesLabel;
    private TextView medianMinutesView;

    private TextView priorityLabel;
    private TextView priorityView;

    private TextView generatedByLabel;
    private TextView generatedByView;

    private TextView generatorLabel;
    private TextView generatorView;

    private TextView startDateLabel;
    private TextView startDateView;
    private EditText startDateEdit;

    private TextView startTimeLabel;
    private TextView startTimeView;
    private EditText startTimeEdit;

    private TextView endDateTimeLabel;
    private TextView endDateTimeView;

    private TextView timeTakenLabel;
    private TextView timeTakenView;

    private TextView statusLabel;
    private TextView statusView;

    private TextView dataSensitivityLabel;
    private TextView dataSensitivityTextView;

    private TextView timeStampLabel;
    private TextView timeStampView;

    private TextView lastModifiedByLabel;
    private TextView lastModifiedBy;

    private DatePickerDialog startDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    private int oldYear = 0;
    private int oldMonth = 0;
    private int oldDay = 0;
    private int oldHour = 0;
    private int oldMinute = 0;

    public PlannedActivityCrudDialog() {
    }

    public static PlannedActivityCrudDialog newInstance(int mode, PlannedActivity pa) {

        PlannedActivityCrudDialog frag = new PlannedActivityCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("plannedActivity", pa);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.pa = (PlannedActivity) getArguments().getSerializable("plannedActivity");
        poModel = POModel.getInstance(this.getContext());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_planned_activity, null);

        recordNoLabel = view.findViewById(R.id.pa_record_no_label);
        recordNoView = view.findViewById(R.id.pa_record_no);
        ownerViewLabel = view.findViewById(R.id.pa_owner_label);
        ownerView = view.findViewById(R.id.pa_owner);
        ownerTypeLabel = view.findViewById(R.id.pa_owner_type_label);
        ownerTypeView = view.findViewById(R.id.pa_owner_type);

        roleLabel = view.findViewById(R.id.pa_role_label);
        roleView = view.findViewById(R.id.pa_role);
        roleSpinner = view.findViewById(R.id.pa_role_spinner);

        locationLabel = view.findViewById(R.id.pa_location_label);
        locationView = view.findViewById(R.id.pa_location);
        locationSpinner = view.findViewById(R.id.pa_location_spinner);

        activityLabel = view.findViewById(R.id.pa_activity_label);
        activityView = view.findViewById(R.id.pa_activity);
        activitySpinner = view.findViewById(R.id.pa_activity_spinner);

        codeLabel = view.findViewById(R.id.pa_code_label);
        codeView = view.findViewById(R.id.pa_code);

        descriptionLabel = view.findViewById(R.id.pa_description_label);
        descriptionView = view.findViewById(R.id.pa_description);
        descriptionEdit = view.findViewById(R.id.pa_description_edit);

        medianMinutesLabel = view.findViewById(R.id.pa_median_minutes_label);
        medianMinutesView = view.findViewById(R.id.pa_median_minutes);

        priorityLabel = view.findViewById(R.id.pa_priority_label);
        priorityView = view.findViewById(R.id.pa_priority);
        prioritySpinner = view.findViewById(R.id.pa_priority_spinner);

        generatedByLabel = view.findViewById(R.id.pa_generated_by_label);
        generatedByView = view.findViewById(R.id.pa_generated_by);

        generatorLabel = view.findViewById(R.id.pa_generator_id_label);
        generatorView = view.findViewById(R.id.pa_generator_id);

        startDateLabel = view.findViewById(R.id.pa_start_date_label);
        startDateView = view.findViewById(R.id.pa_start_date);
        startDateEdit = view.findViewById(R.id.pa_start_date_edit);
        startDateEdit.setInputType(InputType.TYPE_NULL);
        startDateEdit.setOnClickListener(this);

        startTimeLabel = view.findViewById(R.id.pa_start_time_label);
        startTimeView = view.findViewById(R.id.pa_start_time);
        startTimeEdit = view.findViewById(R.id.pa_start_time_edit);
        startTimeEdit.setInputType(InputType.TYPE_NULL);
        startTimeEdit.setOnClickListener(this);

        endDateTimeLabel = view.findViewById(R.id.pa_end_date_time_label);
        endDateTimeView = view.findViewById(R.id.pa_end_date_time);

        timeTakenLabel = view.findViewById(R.id.pa_time_taken_label);
        timeTakenView = view.findViewById(R.id.pa_time_taken);

        statusLabel = view.findViewById(R.id.pa_status_label);
        statusView = view.findViewById(R.id.pa_status);

        dataSensitivityLabel = view.findViewById(R.id.pa_data_sensitivity_label);
        dataSensitivityTextView = view.findViewById(R.id.pa_data_sensitivity);
        dataSensitivitySpinner = view.findViewById(R.id.pa_data_sensitivity_spinner);

        timeStampLabel = view.findViewById(R.id.pa_time_stamp_label);
        timeStampView = view.findViewById(R.id.pa_time_stamp);

        lastModifiedByLabel = view.findViewById(R.id.pa_last_modified_by_label);
        lastModifiedBy = view.findViewById(R.id.pa_last_modified_by);

        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);

        recordNoView.setText(String.valueOf(pa.getDatabaseRecordNo()));

        ownerView.setText(
                poModel.getUserTitle(
                        pa.getOwner(),
                        pa.getOwnerType()));

        ownerTypeView.setText(
                poModel.getOwnerType(pa.getOwnerType())
        );

        Role role = poModel.getRole(pa.getRole());
        if (role != null)
            roleView.setText(role.getTitle());

        roles = poModel.getRoles();
        roleAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                roles);
        roleSpinner.setAdapter(roleAdapter);
        roleAdapter.sort(new RoleTor());
        UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, role);

        Location location = poModel.getLocation(pa.getLocation());
        if (location != null)
            locationView.setText(location.getTitle());

        ArrayList<Location> locations = poModel.getLocations();
        locationAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                locations);
        locationSpinner.setAdapter(locationAdapter);
        locationAdapter.sort(new LocationTor());
        UtilityHelper.selectSpinnerIndex(locationSpinner, locationAdapter, location);

        Activity activity = poModel.getActivity(pa.getActivity());
        if ( activity != null )

            activityView.setText(activity.getDescription());

        ArrayList<Activity> activitys = poModel.getActivitysByRole(pa.getRole());
        activityAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item, activitys);
        activitySpinner.setAdapter(activityAdapter);
        activityAdapter.sort(new ActivityTor());
        UtilityHelper.selectSpinnerIndex(activitySpinner,activityAdapter,activity);

        codeView.setText(pa.getCode());
        descriptionView.setText(pa.getDescription());
        descriptionEdit.setText(pa.getDescription());
        int minutes = pa.getMedianMinutes();
        int hours = 0;
        if ( minutes > 59 )
            hours = minutes/60;
        minutes -= ( hours * 60 );
        StringBuffer timeTakenBuffer = new StringBuffer();
        if ( hours > 0 )
            timeTakenBuffer.append(hours+"hr ");
        timeTakenBuffer.append(minutes+"min");
        medianMinutesView.setText("Hello Dolly");

        priorityView.setText(String.valueOf(pa.getPriority()));
        ArrayList<Integer> priorities = new ArrayList<Integer>();
        priorities.add(new Integer(01));
        priorities.add(new Integer(02));
        priorities.add(new Integer(03));
        priorities.add(new Integer(04));
        priorities.add(new Integer(05));
        priorityAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                priorities);
        prioritySpinner.setAdapter(priorityAdapter);
        UtilityHelper.selectSpinnerIndex(prioritySpinner, priorityAdapter, new Integer(pa.getPriority()));

        generatorView.setText(String.valueOf(pa.getGeneratorType()));
        generatedByView.setText(String.valueOf(pa.getGeneratorId()));

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
        startDateEdit.setText(displayStartDate.format(calendar.getTime()));

        SimpleDateFormat displayStartTime = new SimpleDateFormat("HH:mm");
        startTimeView.setText(displayStartTime.format(calendar.getTime()));
        startTimeEdit.setText(displayStartTime.format(calendar.getTime()));

        endDateTimeView.setText(pa.getEndDateTime());
        timeTakenView.setText(String.valueOf(pa.getTimeTaken()));
        switch (pa.getStatus()){
            case PlannedActivity.PENDING:
                statusView.setText("Pending");
                endDateTimeLabel.setText("Estimated End Time");
                break;
            case PlannedActivity.EXECUTING:
                statusView.setText("Executing");
                break;
            case PlannedActivity.PAUSED:
                statusView.setText("Paused");
                break;
            case PlannedActivity.COMPLETED:
                statusView.setText("Completed");
                break;
            case PlannedActivity.NOT_NECESSARY:
                statusView.setText("Not Necessary");
                break;
            case PlannedActivity.LACK_OF_TIME:
                statusView.setText("Lack of Time");
                break;
            case PlannedActivity.ADVERSE_WEATHER:
                statusView.setText("Adverse Weather");
                break;
            case PlannedActivity.NO_ACCESS:
                statusView.setText("No Access");
                break;
        }
        DataSensitivity dataSensitivity = poModel.getDataSensitivity(pa.getDataSensitivity());
        if (dataSensitivity != null)
            dataSensitivityTextView.setText(dataSensitivity.getTitle());

        dataSensitivitys = poModel.getDataSensitivitys();
        dataSensitivityAdapter = new ArrayAdapter(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                dataSensitivitys);
        dataSensitivitySpinner.setAdapter(dataSensitivityAdapter);
        dataSensitivityAdapter.sort(new DataSensitivityTor());
        UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dataSensitivityAdapter, dataSensitivity);


        timeStampView.setText(pa.getTimeStamp());
        lastModifiedBy.setText(poModel.getUserName(pa.getLastModifiedBy()));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Delete");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.VISIBLE);

                descriptionView.setVisibility(View.GONE);
                roleSpinner.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.GONE);

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pa.getDatabaseRecordNo() != 0)
                    poModel.deletePlannedActivity(pa.getDatabaseRecordNo());
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pa.getDatabaseRecordNo() == 0)
                    return;

                Role r = (Role) roleSpinner.getSelectedItem();
                pa.setRole(r.getDatabaseRecordNo());

                Location l = (Location) locationSpinner.getSelectedItem();
                pa.setLocation(l.getDatabaseRecordNo());

                String description = descriptionView.getText().toString();
                if (description.length() == 0)
                    return;
                pa.setDescription(description);

                Integer priority = (Integer) prioritySpinner.getSelectedItem();
                pa.setPriority(priority);


                DataSensitivity dataSensitivity =
                        (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                pa.setDataSensitivity(dataSensitivity.getDatabaseRecordNo());

                poModel.updatePlannedActivity(pa);
                getDialog().dismiss();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Update");
                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);
                ownerViewLabel.setVisibility(View.GONE);
                ownerView.setVisibility(View.GONE);
                ownerTypeLabel.setVisibility(View.GONE);
                ownerTypeView.setVisibility(View.GONE);
                roleView.setVisibility(View.GONE);
                roleSpinner.setVisibility(View.VISIBLE);
                locationView.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.VISIBLE);
                descriptionView.setVisibility(View.GONE);
                priorityView.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.VISIBLE);
                dataSensitivityTextView.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.VISIBLE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);

                recordNoView.setText(String.valueOf(pa.getDatabaseRecordNo()));
                ownerView.setText(
                        poModel.getUserTitle(
                                pa.getOwner(),
                                pa.getOwnerType()));

                ownerTypeView.setText(
                        poModel.getOwnerType(pa.getOwnerType())
                );

                Role role = poModel.getRole(pa.getRole());
                if (role != null)
                    roleView.setText(role.getTitle());

                final ArrayList<Role> roles = poModel.getRoles();
                roleAdapter.clear();
                roleAdapter.addAll(poModel.getRoles());
                roleAdapter.sort(new RoleTor());
                UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, role);

                Location location = poModel.getLocation(pa.getLocation());
                if (location != null)
                    locationView.setText(location.getTitle());

                ArrayList<Location> locations = poModel.getLocations();
                locationAdapter = new ArrayAdapter(getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        locations);
                locationSpinner.setAdapter(locationAdapter);
                locationAdapter.sort(new LocationTor());
                UtilityHelper.selectSpinnerIndex(locationSpinner, locationAdapter, location);

                descriptionView.setText(pa.getDescription());
                descriptionEdit.setText(pa.getDescription());

                priorityView.setText(String.valueOf(pa.getPriority()));
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
                UtilityHelper.selectSpinnerIndex(prioritySpinner, priorityAdapter, Integer.valueOf(pa.getPriority()));

                DataSensitivity dataSensitivity = poModel.getDataSensitivity(pa.getDataSensitivity());
                if (dataSensitivity != null)
                    dataSensitivityTextView.setText(dataSensitivity.getTitle());
                ArrayList<DataSensitivity> dataSensitivities = poModel.getDataSensitivitys();
                dataSensitivityAdapter = new ArrayAdapter(getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        dataSensitivities);
                dataSensitivitySpinner.setAdapter(dataSensitivityAdapter);
                dataSensitivityAdapter.sort(new DataSensitivityTor());
                UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner,
                                                    dataSensitivityAdapter, dataSensitivity);


                timeStampView.setText(pa.getTimeStamp());
                lastModifiedBy.setText(poModel.getUserName(pa.getLastModifiedBy()));
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

                descriptionView.setVisibility(View.GONE);
                priorityView.setVisibility(View.GONE);
                startDateView.setVisibility(View.GONE);
                startTimeView.setVisibility(View.GONE);
                dataSensitivityTextView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Role r = (Role) roleSpinner.getSelectedItem();
                        pa.setRole(r.getDatabaseRecordNo());

                        Location l = (Location) locationSpinner.getSelectedItem();
                        pa.setLocation(l.getDatabaseRecordNo());

                        Activity a = (Activity) activitySpinner.getSelectedItem();
                        pa.setActivity(a.getDatabaseRecordNo());

                        String description = descriptionEdit.getText().toString();
                        if (description.length() == 0)
                            return;
                        pa.setDescription(description);

                        Integer priority = (Integer) prioritySpinner.getSelectedItem();
                        pa.setPriority(priority);

                        String startDateDisplay = startDateEdit.getText().toString();
                        String startDateStorage =
                                startDateDisplay.substring(6) +
                                        startDateDisplay.substring(3,5) +
                                            startDateDisplay.substring(0,2);
                        pa.setStartDate(startDateStorage);

                        pa.setStartTime(startTimeEdit.getText().toString());
                        pa.setEndDateTime(endDateTimeView.getText().toString());

                        DataSensitivity dataSensitivity =
                                (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                        pa.setDataSensitivity(dataSensitivity.getDatabaseRecordNo());

                        poModel.addPlannedActivity(pa);

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
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);

                roleSpinner.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.GONE);
                activitySpinner.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);

                startTimeEdit.setVisibility(View.GONE);
                startDateEdit.setVisibility(View.GONE);
                descriptionEdit.setVisibility(View.GONE);

                descriptionView.setVisibility(View.VISIBLE);
                activityView.setVisibility(View.VISIBLE);

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                break;
        }
        builder.setCustomTitle(dialogHeader);
        builder.setView(view);

        roleSpinner.setOnItemSelectedListener(this);
        locationSpinner.setOnItemSelectedListener(this);
        activitySpinner.setOnItemSelectedListener(this);
        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent == roleSpinner) {

            Role r = (Role) roleSpinner.getSelectedItem();
            DataSensitivity ds = poModel.getDataSensitivity(r.getDataSensitivity());
            UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner,
                                                dataSensitivityAdapter, ds);

            ArrayList acts = poModel.getActivitysByRoleAndLocation(
                                                r.getDatabaseRecordNo(),0);
            activityAdapter.clear();
            activityAdapter.addAll(acts);
            activityAdapter.sort(new ActivityTor());
            activityAdapter.notifyDataSetChanged();

            locationSpinner.setSelection(0);

        } else if ( parent == locationSpinner ){

            Role r = (Role) roleSpinner.getSelectedItem();
            Location l = (Location) locationSpinner.getSelectedItem();
            ArrayList acts =
                    poModel.getActivitysByRoleAndLocation(
                                        r.getDatabaseRecordNo(),
                                            l.getDatabaseRecordNo());
            activityAdapter.clear();
            activityAdapter.addAll(acts);
            activityAdapter.sort(new ActivityTor());
            activityAdapter.notifyDataSetChanged();

        } else if ( parent == activitySpinner ){

            Activity activity = (Activity) activitySpinner.getSelectedItem();
            codeView.setText("NYI");
            descriptionView.setText(activity.getDescription());
            descriptionEdit.setText(activity.getDescription());
            medianMinutesView.setText(String.valueOf(activity.getMedianMinutes()));


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {

        if (v == startDateEdit) {

            Calendar theDate = Calendar.getInstance();
            String oldDateX = startDateEdit.getText().toString();
            try {

                if (oldDateX.length() >= 10) { //dd-mm-yyyy 10 chars
                    oldYear = Integer.parseInt(oldDateX.substring(6, 10));
                    oldMonth = Integer.parseInt(oldDateX.substring(3, 5));
                    oldMonth--;
                    if ( oldMonth == -1)
                        oldMonth = 11;
                    oldDay = Integer.parseInt(oldDateX.substring(0, 2));
                } else {
                    oldYear = theDate.get(Calendar.YEAR);
                    oldMonth = theDate.get(Calendar.MONTH);
                    oldDay = theDate.get(Calendar.DAY_OF_MONTH);
                }

            } catch (Exception e) {

                oldYear = theDate.get(Calendar.YEAR);
                oldMonth = theDate.get(Calendar.MONTH);
                oldDay = theDate.get(Calendar.DAY_OF_MONTH);

            }
            initialiseDatePicker();
            startDatePickerDialog.show();
            
        } else if ( v == startTimeEdit ){

            Calendar theDate = Calendar.getInstance();
            String oldTimeX = startTimeEdit.getText().toString();
            try {

                if (oldTimeX.length() >= 5) { //HH:mm 5 chars
                    oldHour = Integer.parseInt(oldTimeX.substring(0,2));
                    oldMinute = Integer.parseInt(oldTimeX.substring(3, 5));
                } else {
                    oldHour = theDate.get(Calendar.HOUR_OF_DAY);
                    oldMinute = theDate.get(Calendar.MINUTE);
                }

            } catch (Exception e) {

                oldHour = theDate.get(Calendar.HOUR_OF_DAY);
                oldMinute = theDate.get(Calendar.MINUTE);

            }
            initialiseTimePicker();
            startTimePickerDialog.show();
        }
    }

    private void initialiseDatePicker() {

        startDatePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateTime.set(year, monthOfYear, dayOfMonth);
                        startDateView.setText(dateFormatter.format(endDateTime.getTime()));
                        startDateEdit.setText(dateFormatter.format(endDateTime.getTime()));
                        calculateEndTime();
                    }

                }, oldYear, oldMonth, oldDay);

    }

    private void initialiseTimePicker(){

        startTimePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endDateTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        endDateTime.set(Calendar.MINUTE,minute);
                        startTimeView.setText(timeFormatter.format(endDateTime.getTime()));
                        startTimeEdit.setText(timeFormatter.format(endDateTime.getTime()));
                        calculateEndTime();
                    }
                },oldHour,oldMinute,true);
    }
    private SimpleDateFormat displayEndDate = new SimpleDateFormat("dd-MMM-yyyy   HH:mm");

    private Calendar endDateTime = Calendar.getInstance();

    private void calculateEndTime(){
        int timeTaken = pa.getTimeTaken();
        if ( timeTaken == 0 )
            timeTaken = 60; // mins
        endDateTime.add(Calendar.MINUTE,timeTaken);
        endDateTimeView.setText(displayEndDate.format(endDateTime.getTime()));
    }


}




