package uk.co.thomas_cross.personalorganiser;


import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;
import uk.co.thomas_cross.personalorganiser.model.DBFrontEnd;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 19/03/18.
 */

public class ActivityCrudDialogFragment extends DialogFragment
                                            implements AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private Spinner roleSpinner;
    private Spinner locationSpinner;
    private Spinner prioritySpinner;
    private Spinner dataSensitivitySpinner;

    private Activity activity = null;

    private ArrayAdapter dsAdapter;
    private ArrayAdapter roleAdapter;
    private ArrayAdapter locationsArrayAdapter;
    private ArrayAdapter<Integer> priorityAdapter;

    private ArrayList<DataSensitivity> dataSensitivities = new ArrayList<DataSensitivity>();
    private ArrayList<Role> roles = new ArrayList<Role>();
    private ArrayList<Location> locations = new ArrayList<Location>();

    private DataSensitivityTor dsTor = new DataSensitivityTor();
    private RoleTor roleTor = new RoleTor();
    private LocationTor locationTor = new LocationTor();


    private POModel model = null;


    /* The activity that creates an instance of this dialog fragment must
      * implement this interface in order to receive event callbacks.
      * Each method passes the DialogFragment in case the host needs to query it. */
    public interface ActivityCrudDialogListener {
        public void onDialogPositiveClick(Activity activity);

        public void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    ActivityCrudDialogListener mListener;

    public ActivityCrudDialogFragment() {
    }

    public static ActivityCrudDialogFragment newInstance(int mode, Activity activity) {

        ActivityCrudDialogFragment frag = new ActivityCrudDialogFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("activity", activity);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.activity = (Activity) getArguments().getSerializable("activity");
        this.model = new POModel(this.getContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.activity_crud_form, null);

        final TextView recordNoLabel = view.findViewById(R.id.record_no_label);
        final TextView recordNoView = view.findViewById(R.id.record_no);

        final TextView ownerViewLabel = view.findViewById(R.id.owner_label);
        final TextView ownerView = view.findViewById(R.id.owner);

        final TextView ownerTypeLabel = view.findViewById(R.id.owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.owner_type);

        final TextView roleLabel = view.findViewById(R.id.role_label);
        final TextView roleView = view.findViewById(R.id.role_text_view);
        roleSpinner = view.findViewById(R.id.role_spinner);

        final TextView locationLabel = view.findViewById(R.id.location_label);
        final TextView locationView = view.findViewById(R.id.location_text_view);
        locationSpinner = view.findViewById(R.id.location_spinner);

        final TextView descriptionLabel = view.findViewById(R.id.description_label);
        final TextView descriptionView = view.findViewById(R.id.description_text_view);
        final EditText descriptionEditText = view.findViewById(R.id.description_edit_text);

        final TextView priorityLabel = view.findViewById(R.id.priority_label);
        final TextView priorityView = view.findViewById(R.id.priority_text);
        prioritySpinner = view.findViewById(R.id.priority_spinner);

        final TextView lowestMinutesLabel = view.findViewById(R.id.lowest_minutes_label);
        final TextView lowestMinutesView = view.findViewById(R.id.lowest_minutes_view);

        final TextView highestMinutesLabel = view.findViewById(R.id.highest_minutes_label);
        final TextView highestMinutesView = view.findViewById(R.id.highest_minutes_view);

        final TextView medianMinutesLabel = view.findViewById(R.id.median_minutes_label);
        final TextView medianMinutesView = view.findViewById(R.id.median_minutes_view);

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

        recordNoView.setText(String.valueOf(activity.getDatabaseRecordNo()));

        ownerView.setText(
                model.getUserTitle(
                        activity.getOwner(),
                        activity.getOwnerType()));

        ownerTypeView.setText(
                model.getOwnerType(activity.getOwnerType())
        );

        Role role = model.getRole(activity.getRole());
        if (role != null)
            roleView.setText(role.getTitle());

        roles = model.getRoles();

        roleAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                roles);
        roleSpinner.setAdapter(roleAdapter);
        roleAdapter.sort(new RoleTor());
        roleSpinner.setOnItemSelectedListener(this);
        UtilityHelper.selectSpinnerIndex(roleSpinner, roleAdapter, roles, role);


        Location location = model.getLocation(activity.getLocation());
        if (location != null)
            locationView.setText(location.getTitle());
        locations = model.getLocations();

        ArrayList<Location> locations = model.getLocations();
        locationsArrayAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                locations);
        locationSpinner.setAdapter(locationsArrayAdapter);
        locationsArrayAdapter.sort(new LocationTor());
        UtilityHelper.selectSpinnerIndex(locationSpinner, locationsArrayAdapter, locations, location);


        descriptionView.setText(activity.getDescription());
        descriptionEditText.setText(activity.getDescription());

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
                priorities, Integer.valueOf(activity.getPriority()));

        lowestMinutesView.setText(String.valueOf(activity.getLowestMinutes()));
        highestMinutesView.setText(String.valueOf(activity.getHighestMinutes()));
        medianMinutesView.setText(String.valueOf(activity.getMedianMinutes()));

        DataSensitivity ds = model.getDataSensitivity(activity.getDataSensitivity());
        if ( ds != null )
            dataSensitivityTextView.setText(ds.getTitle());


        dataSensitivities = model.getDataSensitivitys();
        dsAdapter = new ArrayAdapter(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                dataSensitivities);
        dataSensitivitySpinner.setAdapter(dsAdapter);
        dsAdapter.sort(new DataSensitivityTor());
        UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner, dsAdapter, dataSensitivities, ds);

        timeStampView.setText(activity.getTimeStamp());
        lastModifiedBy.setText(
                model.getUserName(activity.getLastModifiedBy())
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

                descriptionEditText.setVisibility(View.GONE);

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.getDatabaseRecordNo() != 0)
                    model.deleteActivity(activity.getDatabaseRecordNo());
                getDialog().dismiss();
                mListener.onDialogPositiveClick(activity);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.getDatabaseRecordNo() == 0)
                    return;

                Role role1 = (Role) roleSpinner.getSelectedItem();
                if (role1 != null)
                    activity.setRole(role1.getDatabaseRecordNo());

                Location location1 = (Location) locationSpinner.getSelectedItem();
                if (location1 != null)
                    activity.setLocation(location1.getDatabaseRecordNo());

                String description = descriptionEditText.getText().toString();
                if (description.length() == 0)
                    return;
                activity.setDescription(description);

                DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                if (ds != null)
                    activity.setDataSensitivity(ds.getDatabaseRecordNo());
                model.updateActivity(activity);
                getDialog().dismiss();
                mListener.onDialogPositiveClick(activity);
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
                roleView.setVisibility(View.GONE);
                roleSpinner.setVisibility(View.VISIBLE);
                locationSpinner.setVisibility(View.VISIBLE);
                locationView.setVisibility(View.GONE);
                descriptionView.setVisibility(View.GONE);
                descriptionEditText.setVisibility(View.VISIBLE);
                priorityView.setVisibility(View.GONE);
                prioritySpinner.setVisibility(View.VISIBLE);
                lowestMinutesLabel.setVisibility(View.GONE);
                lowestMinutesView.setVisibility(View.GONE);
                highestMinutesLabel.setVisibility(View.GONE);
                highestMinutesView.setVisibility(View.GONE);
                medianMinutesLabel.setVisibility(View.GONE);
                medianMinutesView.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.VISIBLE);
                dataSensitivityTextView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);
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
                priorityView.setVisibility(View.GONE);
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
                            activity.setRole(role1.getDatabaseRecordNo());

                        Location location1 = (Location) locationSpinner.getSelectedItem();
                        if (location1 != null)
                            activity.setLocation(location1.getDatabaseRecordNo());

                        String description = descriptionEditText.getText().toString();
                        if (description.length() == 0)
                            return;
                        activity.setDescription(description);

                        Integer priority = (Integer) prioritySpinner.getSelectedItem();
                        if ( priority != null )
                            activity.setPriority(priority.intValue());


                        DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                        if (ds != null)
                            activity.setDataSensitivity(ds.getDatabaseRecordNo());

                        model.addActivity(activity);

                        mListener.onDialogPositiveClick(activity);
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
                prioritySpinner.setVisibility(View.GONE);
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

    // Override the Fragment.onAttach() method to instantiate the DialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (ActivityCrudDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ActivityCrudDialogListener");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if ( parent == roleSpinner ){

            Role r = (Role) roleSpinner.getSelectedItem();
            DataSensitivity ds = model.getDataSensitivity(r.getDataSensitivity());
            UtilityHelper.selectSpinnerIndex(dataSensitivitySpinner,
                    dsAdapter, dataSensitivities, ds);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
