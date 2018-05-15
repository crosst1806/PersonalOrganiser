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

import uk.co.thomas_cross.personalorganiser.adapters.ActivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceListItem;
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

public class ActivitySequenceListItemsCrudDialog extends DialogFragment {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private ActivitySequenceList sequenceList = null;
    private Spinner activitySpinner = null;
    private ArrayAdapter activityAdapter;

    private ActivitySequenceListItem sequenceListItem = null;

    private POModel model = null;


    public ActivitySequenceListItemsCrudDialog() {
    }

    public static ActivitySequenceListItemsCrudDialog newInstance(int mode,
                                                           ActivitySequenceListItem sequenceListItem) {

        ActivitySequenceListItemsCrudDialog frag = new ActivitySequenceListItemsCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("activitySequenceListItem", sequenceListItem);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.sequenceListItem =
                (ActivitySequenceListItem) getArguments().getSerializable("activitySequenceListItem");

        this.model = POModel.getInstance(this.getContext());
        sequenceList = model.getActivitySequenceList(sequenceListItem.getActivitySequenceList());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_activity_sequence_list_item, null);

        final TextView recordNoLabel = view.findViewById(R.id.cfasli_record_no_label);
        final TextView recordNoView = view.findViewById(R.id.cfasli_record_no_view);

        final TextView activityView = view.findViewById(R.id.cfasli_activity_view);

        activitySpinner = view.findViewById(R.id.cfasli_activity_spinner);

        final TextView executionOrderView = view.findViewById(R.id.cfasli_execution_order_view);
        final EditText executionOrderInput = view.findViewById(R.id.cfasli_execution_order_input);

        final TextView durationView = view.findViewById(R.id.cfasli_duration__view);
        final EditText durationInput = view.findViewById(R.id.cfasli_duration_input);

        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);

        recordNoView.setText(String.valueOf(sequenceListItem.getDatabaseRecordNo()));
        Activity activity = model.getActivity(sequenceListItem.getActivity());
        if ( activity.getDatabaseRecordNo() == 0 ){
            activity.setDescription("Pause");
        }
        activityView.setText(activity.getDescription());
        ArrayList<Activity> activitys =
                                model.getActivitysByRoleAndLocation(
                                        sequenceList.getRole(),sequenceList.getLocation());
        for ( int i=0; i < activitys.size(); i++ ){
            Activity a = activitys.get(i);
            if ( a.getDatabaseRecordNo() == 0 )
                a.setDescription("Pause");
        }
        activityAdapter = new ArrayAdapter(this.getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                activitys);
        activitySpinner.setAdapter(activityAdapter);
        activityAdapter.sort(new ActivityTor());
        UtilityHelper.selectSpinnerIndex(activitySpinner, activityAdapter, activity);


        executionOrderView.setText(String.valueOf(sequenceListItem.getExecutionOrder()));
        executionOrderInput.setText(String.valueOf(sequenceListItem.getExecutionOrder()));

        durationView.setText(String.valueOf(sequenceListItem.getDuration()));
        durationInput.setText(String.valueOf(sequenceListItem.getDuration()));

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Delete");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.VISIBLE);

                activitySpinner.setVisibility(View.GONE);

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.deleteActivitySequenceListItem(sequenceListItem.getDatabaseRecordNo());
                getDialog().dismiss();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity activity1 = (Activity) activitySpinner.getSelectedItem();
                if ( activity1 != null )
                    sequenceListItem.setActivity(activity1.getDatabaseRecordNo());

                sequenceListItem.setActivity(activity1.getDatabaseRecordNo());
                sequenceListItem.setExecutionOrder(Integer.parseInt(executionOrderInput.getText().toString()));
                sequenceListItem.setDuration(Integer.parseInt(durationInput.getText().toString()));
                model.updateActivitySequenceListItem(sequenceListItem);
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

                activityView.setVisibility(View.GONE);
                executionOrderView.setVisibility(View.GONE);
                durationView.setVisibility(View.GONE);

                activitySpinner.setVisibility(View.VISIBLE);
                executionOrderInput.setVisibility(View.VISIBLE);
                durationInput.setVisibility(View.VISIBLE);

            }

        });

        switch (mode) {
            case CREATE_MODE:

                dialogTitle.setText("Create");

                recordNoLabel.setVisibility(View.GONE);
                recordNoView.setVisibility(View.GONE);

                activityView.setVisibility(View.GONE);
                executionOrderView.setVisibility(View.GONE);
                durationView.setVisibility(View.GONE);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Activity activity1 = (Activity) activitySpinner.getSelectedItem();
                        if ( activity1 != null )
                            sequenceListItem.setActivity(activity1.getDatabaseRecordNo());
                        sequenceListItem.setExecutionOrder(Integer.parseInt(executionOrderInput.getText().toString()));
                        sequenceListItem.setDuration(Integer.parseInt(durationInput.getText().toString()));

                        model.addActivitySequenceListItem(sequenceListItem);
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

                activitySpinner.setVisibility(View.GONE);
                executionOrderInput.setVisibility(View.GONE);
                durationInput.setVisibility(View.GONE);

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

}
