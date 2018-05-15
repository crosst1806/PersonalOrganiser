package uk.co.thomas_cross.personalorganiser;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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
import uk.co.thomas_cross.personalorganiser.util.RoleCheckBox;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

/**
 * Created by thomas on 19/03/18.
 */

public class FilterRolesDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "PersonalOrganiser";

    private POModel poModel;


    public FilterRolesDialog() {
    }

    public static FilterRolesDialog newInstance() {

        FilterRolesDialog frag = new FilterRolesDialog();
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        poModel = POModel.getInstance(this.getContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.filter_roles, null);
        final ViewGroup placeHolder = view.findViewById(R.id.filter_roles_placeholder);

        ImageButton doneButton = view.findViewById(R.id.done_button);

        CheckBox selectAll = view.findViewById(R.id.fr_select_all);

        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ArrayList<POModel.RoleStatus> filteredRoles = poModel.getFilteredRoles();
                for (int i = 0; i < filteredRoles.size(); i++) {
                    POModel.RoleStatus rs = filteredRoles.get(i);
                    rs.setAvailable(isChecked);
                    CheckBox cb = (CheckBox) placeHolder.getChildAt(i);
                    cb.setChecked(isChecked);
                }

                poModel.refreshPlannedActivityArrayAdapter();
                poModel.refreshToDoArrayAdapter();
                poModel.refreshDiaryEntryArrayAdapter();
                poModel.refreshActivityArrayAdapter();

                if ( isChecked ){
                    buttonView.setText("Clear All");
                } else {
                    buttonView.setText("Select All");
                }

            }

        });


        ArrayList<POModel.RoleStatus> filteredRoles = poModel.getFilteredRoles();
        for (int i = 0; i < filteredRoles.size(); i++) {

            POModel.RoleStatus rs = filteredRoles.get(i);
            Role role = rs.getRole();
            RoleCheckBox checkBox = new RoleCheckBox(getContext());
            checkBox.setText(role.getTitle());
            checkBox.setRole(role);
            checkBox.setChecked(rs.isAvailable());
            checkBox.setOnCheckedChangeListener(this);
            placeHolder.addView(checkBox);

        }

        doneButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }

        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (buttonView instanceof RoleCheckBox) {


            RoleCheckBox rcb = (RoleCheckBox) buttonView;
            int roleId = rcb.getRole().getDatabaseRecordNo();
            ArrayList<POModel.RoleStatus> filteredRoles = poModel.getFilteredRoles();
            for (int i = 0; i < filteredRoles.size(); i++) {

                POModel.RoleStatus rs = filteredRoles.get(i);
                Role role = rs.getRole();
                if (role.getDatabaseRecordNo() == roleId) {
                    rs.setAvailable(isChecked);
                    poModel.refreshPlannedActivityArrayAdapter();
                    poModel.refreshToDoArrayAdapter();
                    poModel.refreshDiaryEntryArrayAdapter();
                    poModel.refreshActivityArrayAdapter();
                }

            }

        }

    }
}




