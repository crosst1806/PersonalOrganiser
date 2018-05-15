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
import android.widget.TextView;
import android.widget.Toast;

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Location;

/**
 * Created by thomas on 19/03/18.
 */

public class LocationCrudDialog extends DialogFragment
                            implements AdapterView.OnItemSelectedListener {

    public static final int CREATE_MODE = 0;
    public static final int READ_MODE = 1;
    private static final String TAG = "PersonalOrganiser";

    private Location location = null;
    private POModel poModel;


    /* The activity that creates an instance of this dialog fragment must
      * implement this interface in order to receive event callbacks.
      * Each method passes the DialogFragment in case the host needs to query it. */
    public interface LocationCrudDialogListener {
        public void onDialogPositiveClick(Location location);

        public void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    LocationCrudDialogListener mListener;

    public LocationCrudDialog() {
    }

    public static LocationCrudDialog newInstance(int mode, Location location) {

        LocationCrudDialog frag = new LocationCrudDialog();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putSerializable("location", location);
        frag.setArguments(args);
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        this.location = (Location) getArguments().getSerializable("location");
        poModel = POModel.getInstance(this.getContext());

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.crud_form_location, null);

        final TextView recordNoLabel = view.findViewById(R.id.record_no_label);
        final TextView recordNoView = view.findViewById(R.id.record_no);
        final TextView ownerViewLabel = view.findViewById(R.id.owner_label);
        final TextView ownerView = view.findViewById(R.id.owner);
        final TextView ownerTypeLabel = view.findViewById(R.id.owner_type_label);
        final TextView ownerTypeView = view.findViewById(R.id.owner_type);
        final TextView titleTextView = view.findViewById(R.id.title_text_view);
        final EditText titleEditView = view.findViewById(R.id.title_edit_text);

        final View dialogHeader = inflater.inflate(R.layout.custom_view, null);
        final TextView dialogTitle = dialogHeader.findViewById(R.id.title);
        final ImageButton deleteButton = dialogHeader.findViewById(R.id.delete_button);
        final ImageButton updateButton = dialogHeader.findViewById(R.id.update_button);
        final ImageButton doneButton = dialogHeader.findViewById(R.id.done_button);
        final ImageButton deleteForeverButton = dialogHeader.findViewById(R.id.delete_forever_button);

        recordNoView.setText(String.valueOf(location.getDatabaseRecordNo()));
        ownerView.setText(
                poModel.getUserTitle(
                        location.getOwner(),
                        location.getOwnerType()));

        ownerTypeView.setText(
                poModel.getOwnerType(location.getOwnerType())
        );
        titleTextView.setText(location.getTitle());
        titleEditView.setText(location.getTitle());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTitle.setText("Delete");

                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                doneButton.setVisibility(View.GONE);
                deleteForeverButton.setVisibility(View.VISIBLE);

                titleEditView.setVisibility(View.GONE);

            }

        });

        deleteForeverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.getDatabaseRecordNo() != 0)
                    poModel.deleteLocation(location.getDatabaseRecordNo());
                getDialog().dismiss();
                mListener.onDialogPositiveClick(location);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (location.getDatabaseRecordNo() == 0)
                    return;

                String title = titleEditView.getText().toString();
                if (title.length() == 0)
                    return;
                location.setTitle(title);
                poModel.updateLocation(location);
                getDialog().dismiss();
                mListener.onDialogPositiveClick(location);
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

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleEditView.getText().toString();
                        if (title.length() > 0) {
                            location.setTitle(title);
                            poModel.addLocation(location);
                        }
                        mListener.onDialogPositiveClick(location);
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
            mListener = (LocationCrudDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement LocationCrudDialogListener");
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
