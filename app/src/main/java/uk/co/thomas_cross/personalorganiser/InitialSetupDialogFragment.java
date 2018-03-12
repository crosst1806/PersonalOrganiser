package uk.co.thomas_cross.personalorganiser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import uk.co.thomas_cross.personalorganiser.entities.Person;

/**
 * Created by thomas on 11/03/18.
 */

public class InitialSetupDialogFragment extends DialogFragment {

    private EditText firstName;
    private EditText lastName;
    private Person person = new Person();

    /* The activity that creates an instance of this dialog fragment must
      * implement this interface in order to receive event callbacks.
      * Each method passes the DialogFragment in case the host needs to query it. */
    public interface InitialSetUpDialogListener {
        public void onDialogPositiveClick(Person person);
        public void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    InitialSetUpDialogListener mListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.initial_setup, null);
        firstName = view.findViewById(R.id.is_first_name);
        lastName = view.findViewById(R.id.is_last_name);
        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // sign in user
                person = new Person();
                person.setFirstName(firstName.getText().toString());
                person.setLastName(lastName.getText().toString());
                mListener.onDialogPositiveClick(person);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick();
                InitialSetupDialogFragment.this.getDialog().cancel();
            }
        });
        builder.setTitle("Initial Set Up");

        // Old Version that worked.
//        builder.setView(inflater.inflate(R.layout.initial_setup, null))
//                // Add action buttons
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        InitialSetupDialogFragment.this.getDialog().cancel();
//                    }
//                });
        return builder.create();
    }



    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (InitialSetUpDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement InitialSetUpDialogListener");
        }
    }

//    public InitialSetupDialogFragment(){
//
//    }
//
//
//    public static InitialSetupDialogFragment newInstance(String title) {
//        InitialSetupDialogFragment frag = new InitialSetupDialogFragment();
//        Bundle args = new Bundle();
//        args.putString("title", title);
//        frag.setArguments(args);
//        return frag;
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.initial_setup, container);
//    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        // Get field from view
////        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);
//        getDialog().setTitle("My God");
//        // Show soft keyboard automatically and request focus to field
////        mEditText.requestFocus();
//        getDialog().getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        getDialog
//    }
}

