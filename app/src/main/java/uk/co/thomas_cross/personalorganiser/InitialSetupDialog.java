package uk.co.thomas_cross.personalorganiser;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.UserId;

/**
 * Created by thomas on 11/03/18.
 */

public class InitialSetupDialog extends DialogFragment {

    private EditText firstName;
    private EditText lastName;
    private EditText userName;
    private EditText password;

    private Person person = new Person();
    private UserId userId = new UserId();

    /* The activity that creates an instance of this dialog fragment must
      * implement this interface in order to receive event callbacks.
      * Each method passes the DialogFragment in case the host needs to query it. */
    public interface InitialSetUpDialogListener {
        public void onDialogPositiveClick(Person person, UserId userId);
        public void onDialogNegativeClick(Person person, UserId userId);
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
        userName = view.findViewById(R.id.is_user_name);
        password = view.findViewById(R.id.is_password);

        builder.setView(view);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // sign in user
                person = new Person();
                person.setFirstName(firstName.getText().toString());
                person.setLastName(lastName.getText().toString());

                userId = new UserId();
                userId.setUserName(userName.getText().toString());
                userId.setPassword(password.getText().toString());

                mListener.onDialogPositiveClick(person,userId);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNegativeClick(null,null);
                InitialSetupDialog.this.getDialog().cancel();
            }
        });
        builder.setTitle("Initial Set Up");

        return builder.create();
    }



    // Override the Fragment.onAttach() method to instantiate the DialogListener
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

}

