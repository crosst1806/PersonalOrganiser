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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;
import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by thomas on 19/03/18.
 */

public class RoleCrudDialogFragment extends DialogFragment
        implements AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";
    private Spinner dataSensitivitySpinner;

    private Role role = null;
    private DataSensitivityArrayAdapter dsAdapter;
    private ArrayList<DataSensitivity> dataSensitivities = new ArrayList<DataSensitivity>();
    private DataSensitivityTor dsTor = new DataSensitivityTor();
    private POModel poModel;


    /* The activity that creates an instance of this dialog fragment must
      * implement this interface in order to receive event callbacks.
      * Each method passes the DialogFragment in case the host needs to query it. */
    public interface RoleCrudDialogListener {
        public void onDialogPositiveClick(Role role);

        public void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    RoleCrudDialogListener mListener;

    public RoleCrudDialogFragment() {
    }

    public static RoleCrudDialogFragment newInstance(int mode, Role role) {

        RoleCrudDialogFragment frag = new RoleCrudDialogFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("role", role);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.role = (Role) getArguments().getSerializable("role");
        POModel poModel = new POModel(getContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.role_crud_form, null);

        final TextView recordNoLabel = view.findViewById(R.id.record_no_label);
        final TextView recordNoView = view.findViewById(R.id.record_no);
        final TextView ownerViewLabel = view.findViewById(R.id.owner_label);
        final TextView ownerView = view.findViewById(R.id.owner);
        final TextView ownerTypeLabel = view.findViewById(R.id.owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.owner_type);
        final TextView titleTextView = view.findViewById(R.id.title_text_view);
        final EditText titleEditView = view.findViewById(R.id.title_edit_text);
        final TextView dataSensitivityTextView = view.findViewById(R.id.data_sensitivity_text);
        dataSensitivitySpinner = view.findViewById(R.id.data_sensitivity_spinner);
        dataSensitivitySpinner.setOnItemSelectedListener(this);

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

        recordNoView.setText(String.valueOf(role.getDatabaseRecordNo()));
        ownerView.setText(
                poModel.getUserTitle(
                        role.getOwner(),
                        role.getOwnerType()));

        ownerTypeView.setText(
                poModel.getOwnerType(role.getOwnerType())
        );
        titleTextView.setText(role.getTitle());
        titleEditView.setText(role.getTitle());
        DataSensitivity ds = poModel.getDataSensitivity(role.getDataSensitivity());
        dataSensitivityTextView.setText(ds.getTitle());


        dataSensitivities = poModel.getDataSensitivitys();
        dsAdapter = new DataSensitivityArrayAdapter(this.getContext(), dataSensitivities);
        dsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataSensitivitySpinner.setAdapter(dsAdapter);
        for (int i = 0; i < dsAdapter.getCount(); i++) {
            DataSensitivity ds1 = (DataSensitivity) dsAdapter.getItem(i);
            if (role.getDataSensitivity() == ds1.getDatabaseRecordNo())
                dataSensitivitySpinner.setSelection(i);
        }


        timeStampView.setText(role.getTimeStamp());
        lastModifiedBy.setText(
                poModel.getUserName(role.getLastModifiedBy())
        );

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Delete");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.VISIBLE);

                titleEditView.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (role.getDatabaseRecordNo() != 0) {
                    POModel poModel1 = new POModel(getContext());
                    poModel1.deleteRole(role.getDatabaseRecordNo());
                }
                getDialog().dismiss();
                mListener.onDialogPositiveClick(role);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (role.getDatabaseRecordNo() == 0)
                    return;

                String title = titleEditView.getText().toString();
                if (title.length() == 0)
                    return;
                role.setTitle(title);
                DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                if (ds != null)
                    role.setDataSensitivity(ds.getDatabaseRecordNo());
                POModel poModel1 = new POModel(getContext());
                poModel1.updateRole(role);
                getDialog().dismiss();
                mListener.onDialogPositiveClick(role);
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
                titleTextView.setVisibility(View.GONE);
                titleEditView.setVisibility(View.VISIBLE);
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
                titleTextView.setVisibility(View.GONE);
                titleEditView.requestFocus();
                dataSensitivityTextView.setVisibility(View.GONE);
                timeStampLabel.setVisibility(View.GONE);
                timeStampView.setVisibility(View.GONE);
                lastModifiedByLabel.setVisibility(View.GONE);
                lastModifiedBy.setVisibility(View.GONE);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleEditView.getText().toString();
                        if (title.length() > 0) {
                            role.setTitle(title);
                            DataSensitivity ds = (DataSensitivity) dataSensitivitySpinner.getSelectedItem();
                            if (ds != null)
                                role.setDataSensitivity(ds.getDatabaseRecordNo());
                            POModel poModel1 = new POModel(getContext());
                            poModel1.addRole(role);
                        }
                        mListener.onDialogPositiveClick(role);
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
                titleEditView.setVisibility(View.GONE);
                dataSensitivitySpinner.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.GONE);
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
            mListener = (RoleCrudDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement RoleCrudDialogListener");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " +
                        parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
