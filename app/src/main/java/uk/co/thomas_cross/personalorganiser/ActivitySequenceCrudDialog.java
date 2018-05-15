package uk.co.thomas_cross.personalorganiser;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 19/03/18.
 */

public class ActivitySequenceCrudDialog extends DialogFragment
                                            implements AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private Spinner roleSpinner;
    private Spinner locationSpinner;
    private Spinner dataSensitivitySpinner;

    private ActivitySequenceList sequenceList = null;

    private ArrayAdapter dsAdapter;
    private ArrayAdapter roleAdapter;
    private ArrayAdapter locationsArrayAdapter;

    private ArrayList<DataSensitivity> dataSensitivities = new ArrayList<DataSensitivity>();
    private ArrayList<Role> roles = new ArrayList<Role>();
    private ArrayList<Location> locations = new ArrayList<Location>();

    private DataSensitivityTor dsTor = new DataSensitivityTor();
    private RoleTor roleTor = new RoleTor();
    private LocationTor locationTor = new LocationTor();


    private POModel model = null;


    public ActivitySequenceCrudDialog() {
    }

    public static ActivitySequenceCrudDialog newInstance(int mode, ActivitySequenceList sequenceList) {

        ActivitySequenceCrudDialog frag = new ActivitySequenceCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("activitySequenceList", sequenceList);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.sequenceList = (ActivitySequenceList) getArguments().getSerializable("activitySequenceList");
        this.model = POModel.getInstance(this.getContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_activity_sequence, null);

        final TextView recordNoLabel = view.findViewById(R.id.cfas_record_no_label);
        final TextView recordNoView = view.findViewById(R.id.cfas_record_no_view);

        final TextView ownerViewLabel = view.findViewById(R.id.cfas_owner_label);
        final TextView ownerView = view.findViewById(R.id.cfas_owner_view);

        final TextView ownerTypeLabel = view.findViewById(R.id.cfas_owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.cfas_owner_type_view);

        final TextView roleLabel = view.findViewById(R.id.cfas_role_label);
        final TextView roleView = view.findViewById(R.id.cfas_role_view);
        roleSpinner = view.findViewById(R.id.cfas_role_spinner);

        final TextView locationLabel = view.findViewById(R.id.cfas_location_label);
        final TextView locationView = view.findViewById(R.id.cfas_location_view);
        locationSpinner = view.findViewById(R.id.cfas_location_spinner);

        final TextView descriptionLabel = view.findViewById(R.id.cfas_description_label);
        final TextView descriptionView = view.findViewById(R.id.cfas_description_view);
        final EditText descriptionEditText = view.findViewById(R.id.cfas_description_input);



        final TextView dataSensitivityTextView = view.findViewById(R.id.cfas_data_sensitivity_view);
        dataSensitivitySpinner = view.findViewById(R.id.cfas_data_sensitivity_spinner);

        final TextView timeStampLabel = view.findViewById(R.id.cfas_time_stamp_label);
        final TextView timeStampView = view.findViewById(R.id.cfas_time_stamp_view);

        final TextView lastModifiedByLabel = view.findViewById(R.id.cfas_last_modified_by_label);
        final TextView lastModifiedBy = view.findViewById(R.id.cfas_last_modified_by_view);

        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);

        recordNoView.setText(String.valueOf(sequenceList.getDatabaseRecordNo()));

        ownerView.setText(
                model.getUserTitle(
                        sequenceList.getOwner(),
                        sequenceList.getOwnerType()));

        ownerTypeView.setText(
                model.getOwnerType(sequenceList.getOwnerType())
        );

        Role role = model.getRole(sequenceList.getRole());
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


        Location location = model.getLocation(sequenceList.getLocation());
        if (location != null)
            locationView.setText(location.getTitle());
        locations = model.getLocations();

        ArrayList<Location> locations = model.getLocations();
        locationsArrayAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                locations);
        locationSpinner.setAdapter(locationsArrayAdapter);
        locationsArrayAdapter.sort(new LocationTor());
        UtilityHelper.selectSpinnerIndex(locationSpinner, locationsArrayAdapter, location);


        descriptionView.setText(sequenceList.getDescription());
        descriptionEditText.setText(sequenceList.getDescription());


        DataSensitivity ds = model.getDataSensitivity(sequenceList.getDataSensitivity());
        if ( ds != null )
            dataSensitivityTextView.setText(ds.getTitle());


        dataSensitivities = model.getDataSensitivitys();
        dsAdapter = new ArrayAdapter(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                dataSensitivities);
        dataSensitivitySpinner.setAdapter(dsAdapter);
        dsAdapter.sort(new DataSensitivityTor());
        UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dsAdapter, ds);

        timeStampView.setText(sequenceList.getTimeStamp());
        lastModifiedBy.setText(
                model.getUserName(sequenceList.getLastModifiedBy())
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
                dataSensitivitySpinner.setVisibility(View.GONE);

                descriptionEditText.setVisibility(View.GONE);

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sequenceList.getDatabaseRecordNo() != 0)
                    model.deleteActivitySequenceList(sequenceList.getDatabaseRecordNo());
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sequenceList.getDatabaseRecordNo() == 0)
                    return;

//                Role role1 = (Role) roleSpinner.getSelectedItem();
//                if (role1 != null)
//                    sequenceList.setRole(role1.getDatabaseRecordNo());
//
//                Location location1 = (Location) locationSpinner.getSelectedItem();
//                if (location1 != null)
//                    sequenceList.setLocation(location1.getDatabaseRecordNo());

                String description = descriptionEditText.getText().toString();
                if (description.length() == 0)
                    return;
                sequenceList.setDescription(description);

                DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                if (ds != null)
                    sequenceList.setDataSensitivity(ds.getDatabaseRecordNo());
                model.updateActivitySequenceList(sequenceList);
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
                doneButton.setVisibility(View.VISIBLE);

                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);
                ownerViewLabel.setVisibility(View.GONE);
                ownerView.setVisibility(View.GONE);
                ownerTypeLabel.setVisibility(View.GONE);
                ownerTypeView.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                dataSensitivityTextView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                descriptionEditText.setVisibility(View.VISIBLE);
                dataSensitivitySpinner.setVisibility(View.VISIBLE);

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
                descriptionView.setVisibility(View.GONE);
                dataSensitivityTextView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Role role1 = (Role) roleSpinner.getSelectedItem();
                        if (role1 != null)
                            sequenceList.setRole(role1.getDatabaseRecordNo());

                        Location location1 = (Location) locationSpinner.getSelectedItem();
                        if (location1 != null)
                            sequenceList.setLocation(location1.getDatabaseRecordNo());

                        String description = descriptionEditText.getText().toString();
                        if (description.length() == 0)
                            return;
                        sequenceList.setDescription(description);


                        DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                        if (ds != null)
                            sequenceList.setDataSensitivity(ds.getDatabaseRecordNo());

                        model.addActivitySequenceList(sequenceList);
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
                dataSensitivitySpinner.setVisibility(View.GONE);

                descriptionEditText.setVisibility(View.GONE);


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

        if ( parent == roleSpinner ){

            Role r = (Role) roleSpinner.getSelectedItem();
            DataSensitivity ds = model.getDataSensitivity(r.getDataSensitivity());
            UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dsAdapter, ds);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
