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

import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 19/03/18.
 */

public class DiaryEntryCrudDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private DiaryEntry diaryEntry = null;
    private POModel poModel;

    private Spinner roleSpinner;
    private ArrayAdapter roleAdapter;
    private ArrayList<Role> roles;

    private Spinner locationSpinner;
    private ArrayAdapter locationAdapter;

    private TextView dataSensitivityTextView;

    public DiaryEntryCrudDialog() {
    }

    public static DiaryEntryCrudDialog newInstance(int mode, DiaryEntry diaryEntry) {

        DiaryEntryCrudDialog frag = new DiaryEntryCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("diaryEntry", diaryEntry);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.diaryEntry = (DiaryEntry) getArguments().getSerializable("diaryEntry");
        poModel = POModel.getInstance(this.getContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_diary_entry, null);

        final TextView recordNoLabel = view.findViewById(R.id.record_no_label);
        final TextView recordNoView = view.findViewById(R.id.record_no);
        final TextView ownerViewLabel = view.findViewById(R.id.owner_label);
        final TextView ownerView = view.findViewById(R.id.owner);
        final TextView ownerTypeLabel = view.findViewById(R.id.owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.owner_type);

        final TextView roleLabel = view.findViewById(R.id.role_label);
        final TextView roleView = view.findViewById(R.id.role_view);
        roleSpinner = view.findViewById(R.id.role_spinner);
        roleSpinner.setOnItemSelectedListener(this);

        final TextView locationLabel = view.findViewById(R.id.location_label);
        final TextView locationView = view.findViewById(R.id.location_view);
        locationSpinner = view.findViewById(R.id.location_spinner);

        final TextView creationTimeView = view.findViewById(R.id.time_view);
        final TextView creationDateView = view.findViewById(R.id.date_view);

        final TextView textEntryLabel = view.findViewById(R.id.text_entry_label);
        final TextView textEntryView = view.findViewById(R.id.text_entry_view);
        final EditText textEntryEdit = view.findViewById(R.id.text_entry_edit);

        final TextView dataSensitivityLabel = view.findViewById(R.id.data_sensitivity_label);
        dataSensitivityTextView = view.findViewById(R.id.data_sensitivity_view);

        final TextView timeStampLabel = view.findViewById(R.id.time_stamp_label);
        final TextView timeStampView = view.findViewById(R.id.time_stamp);

        final TextView lastModifiedByLabel = view.findViewById(R.id.last_modified_by_label);
        final TextView lastModifiedBy = view.findViewById(R.id.last_modified_by_view);


        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);


        creationTimeView.setText(diaryEntry.getDateTime().substring(11));
        creationDateView.setText(diaryEntry.getDateTime().substring(0,11));
        textEntryView.setText(diaryEntry.getTextEntry());
        textEntryEdit.setText(diaryEntry.getTextEntry());

        recordNoView.setText(String.valueOf(diaryEntry.getDatabaseRecordNo()));
        ownerView.setText(
                poModel.getUserTitle(
                        diaryEntry.getOwner(),
                        diaryEntry.getOwnerType()));

        ownerTypeView.setText(
                poModel.getOwnerType(diaryEntry.getOwnerType())
        );

        Role role = poModel.getRole(diaryEntry.getRole());
        if (role != null)
            roleView.setText(role.getTitle());

        roles = poModel.getRoles();
        roleAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                roles);
        roleSpinner.setAdapter(roleAdapter);
        roleAdapter.sort(new RoleTor());
        UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, role);

        Location location = poModel.getLocation(diaryEntry.getLocation());
        if (location != null)
            locationView.setText(location.getTitle());

        ArrayList<Location> locations = poModel.getLocations();
        locationAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                locations);
        locationSpinner.setAdapter(locationAdapter);
        locationAdapter.sort(new LocationTor());
        UtilityHelper.selectSpinnerIndex(locationSpinner, locationAdapter, location);

        DataSensitivity dataSensitivity = poModel.getDataSensitivity(diaryEntry.getDataSensitivity());
        if (dataSensitivity != null)
            dataSensitivityTextView.setText(dataSensitivity.getTitle());

        timeStampView.setText(diaryEntry.getTimeStamp());
        lastModifiedBy.setText(poModel.getUserName(diaryEntry.getLastModifiedBy()));

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

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diaryEntry.getDatabaseRecordNo() != 0)
                    poModel.deleteDiaryEntry(diaryEntry.getDatabaseRecordNo());
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (diaryEntry.getDatabaseRecordNo() == 0)
                    return;

                Role r = (Role) roleSpinner.getSelectedItem();
                diaryEntry.setRole(r.getDatabaseRecordNo());

                Location l = (Location) locationSpinner.getSelectedItem();
                diaryEntry.setLocation(l.getDatabaseRecordNo());

                diaryEntry.setTextEntry(textEntryEdit.getText().toString());

                poModel.updateDiary(diaryEntry);
                getDialog().dismiss();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Update");

                textEntryView.setVisibility(View.GONE);
                textEntryEdit.setVisibility(View.VISIBLE);
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
                dataSensitivityTextView.setVisibility(View.VISIBLE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.VISIBLE);

                recordNoView.setText(String.valueOf(diaryEntry.getDatabaseRecordNo()));
                ownerView.setText(
                        poModel.getUserTitle(
                                diaryEntry.getOwner(),
                                diaryEntry.getOwnerType()));

                ownerTypeView.setText(
                        poModel.getOwnerType(diaryEntry.getOwnerType())
                );

                Role role = poModel.getRole(diaryEntry.getRole());
                if (role != null)
                    roleView.setText(role.getTitle());

                final ArrayList<Role> roles = poModel.getRoles();
                roleAdapter.clear();
                roleAdapter.addAll(poModel.getRoles());
                roleAdapter.sort(new RoleTor());
                UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, role);

                Location location = poModel.getLocation(diaryEntry.getLocation());
                if (location != null)
                    locationView.setText(location.getTitle());

                ArrayList<Location> locations = poModel.getLocations();
                locationAdapter = new ArrayAdapter(getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        locations);
                locationSpinner.setAdapter(locationAdapter);
                locationAdapter.sort(new LocationTor());
                UtilityHelper.selectSpinnerIndex(locationSpinner, locationAdapter, location);

                DataSensitivity dataSensitivity = poModel.getDataSensitivity(diaryEntry.getDataSensitivity());
                if (dataSensitivity != null)
                    dataSensitivityTextView.setText(dataSensitivity.getTitle());


                timeStampView.setText(diaryEntry.getTimeStamp());
                lastModifiedBy.setText(poModel.getUserName(diaryEntry.getLastModifiedBy()));
            }

        });

        switch (mode) {
            case CREATE_MODE:

                dialogTitle.setText("New Diary Entry");

                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);
                ownerViewLabel.setVisibility(View.GONE);
                ownerView.setVisibility(View.GONE);
                ownerTypeLabel.setVisibility(View.GONE);
                ownerTypeView.setVisibility(View.GONE);
                roleView.setVisibility(View.GONE);
                locationView.setVisibility(View.GONE);
                textEntryLabel.setVisibility(View.GONE);
                textEntryView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Role r = (Role) roleSpinner.getSelectedItem();
                        diaryEntry.setRole(r.getDatabaseRecordNo());

                        Location l = (Location) locationSpinner.getSelectedItem();
                        diaryEntry.setLocation(l.getDatabaseRecordNo());

                        diaryEntry.setTextEntry(textEntryEdit.getText().toString());

                        poModel.addDiaryEntry(diaryEntry);


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

                ownerViewLabel.setVisibility(View.VISIBLE);
                ownerView.setVisibility(View.VISIBLE);
                ownerTypeLabel.setVisibility(View.VISIBLE);
                ownerTypeView.setVisibility(View.VISIBLE);
                roleView.setVisibility(View.VISIBLE);
                locationView.setVisibility(View.VISIBLE);
                textEntryView.setVisibility(View.VISIBLE);
                timeStampLabel.setVisibility(View.VISIBLE);
                timeStampView.setVisibility(View.VISIBLE);
                lastModifiedByLabel.setVisibility(View.VISIBLE);
                lastModifiedBy.setVisibility(View.VISIBLE);


                textEntryLabel.setVisibility(View.GONE);
                textEntryEdit.setVisibility(View.GONE);
                roleSpinner.setVisibility(View.GONE);
                locationSpinner.setVisibility(View.GONE);

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

        if (parent == roleSpinner) {

            Role r = (Role) roleSpinner.getSelectedItem();
            DataSensitivity ds = poModel.getDataSensitivity(r.getDataSensitivity());
            dataSensitivityTextView.setText(ds.getTitle());
            diaryEntry.setDataSensitivity(ds.getDatabaseRecordNo());
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}




