package uk.co.thomas_cross.personalorganiser;


import android.app.DatePickerDialog;
import android.app.Dialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 19/03/18.
 */

public class ToDoCrudDialog extends DialogFragment
        implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private ToDo toDo = null;
    private POModel poModel;

    private Spinner roleSpinner;
    private ArrayAdapter roleAdapter;
    private ArrayList<Role> roles;

    private Spinner locationSpinner;
    private ArrayAdapter locationAdapter;

    private Spinner prioritySpinner;
    private ArrayAdapter priorityAdapter;

    private Spinner dataSensitivitySpinner;
    private ArrayAdapter dataSensitivityAdapter;
    private ArrayList<DataSensitivity> dataSensitivitys;


    private EditText targetDateEditView;
    private DatePickerDialog targetDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private int oldYear = 0;
    private int oldMonth = 0;
    private int oldDay = 0;

    @Override
    public void onClick(View v) {

        if (v == targetDateEditView) {

            Calendar theDate = Calendar.getInstance();
            String oldDateX = targetDateEditView.getText().toString();
            try {

                if (oldDateX.length() >= 10) { //dd-mm-yyyy 10 chars
                    oldYear = Integer.parseInt(oldDateX.substring(6, 10));
                    oldMonth = Integer.parseInt(oldDateX.substring(3, 5));
                    oldMonth--;
                    if ( oldMonth == -1)
                        oldMonth = 11;
                    oldDay = Integer.parseInt(oldDateX.substring(0, 2));
                    Log.i(TAG, "" + oldDay + "-" + oldMonth + "-" + oldYear);
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
            targetDatePickerDialog.show();
        }
    }

    public ToDoCrudDialog() {
    }

    public static ToDoCrudDialog newInstance(int mode, ToDo toDo) {

        ToDoCrudDialog frag = new ToDoCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("toDo", toDo);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.toDo = (ToDo) getArguments().getSerializable("toDo");
        poModel = POModel.getInstance(this.getContext());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_to_do, null);

        final TextView recordNoLabel = view.findViewById(R.id.record_no_label);
        final TextView recordNoView = view.findViewById(R.id.record_no);
        final TextView ownerViewLabel = view.findViewById(R.id.owner_label);
        final TextView ownerView = view.findViewById(R.id.owner);
        final TextView ownerTypeLabel = view.findViewById(R.id.owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.owner_type);

        final TextView roleLabel = view.findViewById(R.id.role_label);
        final TextView roleView = view.findViewById(R.id.role_text_view);
        roleSpinner = view.findViewById(R.id.role_spinner);
        roleSpinner.setOnItemSelectedListener(this);

        final TextView locationLabel = view.findViewById(R.id.location_label);
        final TextView locationView = view.findViewById(R.id.location_text_view);
        locationSpinner = view.findViewById(R.id.location_spinner);

        final TextView descriptionTextView = view.findViewById(R.id.description_text_view);
        final EditText descriptionEditView = view.findViewById(R.id.description_edit_text);

        final TextView priorityLabel = view.findViewById(R.id.priority_label);
        final TextView priorityView = view.findViewById(R.id.priority_text);
        prioritySpinner = view.findViewById(R.id.priority_spinner);

        final TextView targetDateTimeLabel = view.findViewById(R.id.target_date_label);
        final TextView targetDateView = view.findViewById(R.id.target_date_text_view);
        targetDateEditView = view.findViewById(R.id.target_date_edit_text);
        targetDateEditView.setInputType(InputType.TYPE_NULL);
        targetDateEditView.setOnClickListener(this);

        final TextView dataSensitivityLabel = view.findViewById(R.id.data_sensitivity_label);
        final TextView dataSensitivityTextView = view.findViewById(R.id.data_sensitivity_text);
        dataSensitivitySpinner = view.findViewById(R.id.data_sensitivity_spinner);

        final TextView timeStampLabel = view.findViewById(R.id.time_stamp_label);
        final TextView timeStampView = view.findViewById(R.id.time_stamp);
        final TextView lastModifiedByLabel = view.findViewById(R.id.last_modified_by_label);
        final TextView lastModifiedBy = view.findViewById(R.id.last_modified_by);


        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);

        recordNoView.setText(String.valueOf(toDo.getDatabaseRecordNo()));
        ownerView.setText(
                poModel.getUserTitle(
                        toDo.getOwner(),
                        toDo.getOwnerType()));

        ownerTypeView.setText(
                poModel.getOwnerType(toDo.getOwnerType())
        );

        Role role = poModel.getRole(toDo.getRole());
        if (role != null)
            roleView.setText(role.getTitle());

        roles = poModel.getRoles();
        roleAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                roles);
        roleSpinner.setAdapter(roleAdapter);
        roleAdapter.sort(new RoleTor());
        UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, role);

        Location location = poModel.getLocation(toDo.getLocation());
        if (location != null)
            locationView.setText(location.getTitle());

        ArrayList<Location> locations = poModel.getLocations();
        locationAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                locations);
        locationSpinner.setAdapter(locationAdapter);
        locationAdapter.sort(new LocationTor());
        UtilityHelper.selectSpinnerIndex(locationSpinner, locationAdapter, location);

        descriptionTextView.setText(toDo.getDescription());
        descriptionEditView.setText(toDo.getDescription());

        priorityView.setText(String.valueOf(toDo.getPriority()));
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
        UtilityHelper.selectSpinnerIndex(prioritySpinner, priorityAdapter, new Integer(toDo.getPriority()));

        targetDateView.setText(toDo.getTargetDate());
        targetDateEditView.setText(toDo.getTargetDate());

        DataSensitivity dataSensitivity = poModel.getDataSensitivity(toDo.getDataSensitivity());
        if (dataSensitivity != null)
            dataSensitivityTextView.setText(dataSensitivity.getTitle());

        dataSensitivitys = poModel.getDataSensitivitys();
        dataSensitivityAdapter = new ArrayAdapter(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                dataSensitivitys);
        dataSensitivitySpinner.setAdapter(dataSensitivityAdapter);
        dataSensitivityAdapter.sort(new DataSensitivityTor());
        UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dataSensitivityAdapter, dataSensitivity);


        timeStampView.setText(toDo.getTimeStamp());
        lastModifiedBy.setText(poModel.getUserName(toDo.getLastModifiedBy()));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Delete");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.VISIBLE);

                descriptionEditView.setVisibility(View.GONE);
                targetDateEditView.setVisibility(View.GONE);
                roleSpinner.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.GONE);

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toDo.getDatabaseRecordNo() != 0)
                    poModel.deleteToDo(toDo.getDatabaseRecordNo());
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toDo.getDatabaseRecordNo() == 0)
                    return;

                Role r = (Role) roleSpinner.getSelectedItem();
                toDo.setRole(r.getDatabaseRecordNo());

                Location l = (Location) locationSpinner.getSelectedItem();
                toDo.setLocation(l.getDatabaseRecordNo());

                String description = descriptionEditView.getText().toString();
                if (description.length() == 0)
                    return;
                toDo.setDescription(description);

                Integer priority = (Integer) prioritySpinner.getSelectedItem();
                toDo.setPriority(priority);

                String targetDate = targetDateEditView.getText().toString();
                if (targetDate.length() == 0)
                    return;
                toDo.setTargetDate(targetDate);

                DataSensitivity dataSensitivity =
                        (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                toDo.setDataSensitivity(dataSensitivity.getDatabaseRecordNo());

                poModel.updateToDo(toDo);
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
                descriptionTextView.setVisibility(View.GONE);
                descriptionEditView.setVisibility(View.VISIBLE);
                priorityView.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.VISIBLE);
                targetDateView.setVisibility(View.GONE);
                targetDateEditView.setVisibility(View.VISIBLE);
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

                recordNoView.setText(String.valueOf(toDo.getDatabaseRecordNo()));
                ownerView.setText(
                        poModel.getUserTitle(
                                toDo.getOwner(),
                                toDo.getOwnerType()));

                ownerTypeView.setText(
                        poModel.getOwnerType(toDo.getOwnerType())
                );

                Role role = poModel.getRole(toDo.getRole());
                if (role != null)
                    roleView.setText(role.getTitle());

                final ArrayList<Role> roles = poModel.getRoles();
                roleAdapter.clear();
                roleAdapter.addAll(poModel.getRoles());
                roleAdapter.sort(new RoleTor());
                UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, role);

                Location location = poModel.getLocation(toDo.getLocation());
                if (location != null)
                    locationView.setText(location.getTitle());

                ArrayList<Location> locations = poModel.getLocations();
                locationAdapter = new ArrayAdapter(getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        locations);
                locationSpinner.setAdapter(locationAdapter);
                locationAdapter.sort(new LocationTor());
                UtilityHelper.selectSpinnerIndex(locationSpinner, locationAdapter, location);

                descriptionTextView.setText(toDo.getDescription());
                descriptionEditView.setText(toDo.getDescription());

                priorityView.setText(String.valueOf(toDo.getPriority()));
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
                                                    Integer.valueOf(toDo.getPriority()));

                targetDateView.setText(toDo.getTargetDate());
                targetDateEditView.setText(toDo.getTargetDate());

                DataSensitivity dataSensitivity = poModel.getDataSensitivity(toDo.getDataSensitivity());
                if (dataSensitivity != null)
                    dataSensitivityTextView.setText(dataSensitivity.getTitle());
                ArrayList<DataSensitivity> dataSensitivities = poModel.getDataSensitivitys();
                dataSensitivityAdapter = new ArrayAdapter(getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        dataSensitivities);
                dataSensitivitySpinner.setAdapter(dataSensitivityAdapter);
                dataSensitivityAdapter.sort(new DataSensitivityTor());
                UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dataSensitivityAdapter,
                                                        dataSensitivity);


                timeStampView.setText(toDo.getTimeStamp());
                lastModifiedBy.setText(poModel.getUserName(toDo.getLastModifiedBy()));
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
                descriptionTextView.setVisibility(View.GONE);
                priorityView.setVisibility(View.GONE);
                targetDateView.setVisibility(View.GONE);
                dataSensitivityTextView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Role r = (Role) roleSpinner.getSelectedItem();
                        toDo.setRole(r.getDatabaseRecordNo());

                        Location l = (Location) locationSpinner.getSelectedItem();
                        toDo.setLocation(l.getDatabaseRecordNo());

                        String description = descriptionEditView.getText().toString();
                        if (description.length() == 0)
                            return;
                        toDo.setDescription(description);

                        Integer priority = (Integer) prioritySpinner.getSelectedItem();
                        toDo.setPriority(priority);

                        String targetDate = targetDateEditView.getText().toString();
                        if (targetDate.length() == 0)
                            return;
                        toDo.setTargetDate(targetDate);

                        DataSensitivity dataSensitivity =
                                (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                        toDo.setDataSensitivity(dataSensitivity.getDatabaseRecordNo());

                        poModel.addToDo(toDo);
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
                descriptionEditView.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.GONE);
                targetDateEditView.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);

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


    private void initialiseDatePicker() {

        targetDatePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        targetDateEditView.setText(dateFormatter.format(newDate.getTime()));
                    }

                }, oldYear, oldMonth, oldDay);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent == roleSpinner) {

            Role r = (Role) roleSpinner.getSelectedItem();
            DataSensitivity ds = poModel.getDataSensitivity(r.getDataSensitivity());
            UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dataSensitivityAdapter, ds);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}




