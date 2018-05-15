package uk.co.thomas_cross.personalorganiser;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.LocationCheckBox;
import uk.co.thomas_cross.personalorganiser.util.LocationCheckBox;

/**
 * Created by thomas on 19/03/18.
 */

public class FilterByLocationDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "PersonalOrganiser";

    private POModel poModel;


    public FilterByLocationDialog() {
    }

    public static FilterByLocationDialog newInstance() {

        FilterByLocationDialog frag = new FilterByLocationDialog();
        return frag;

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        poModel = POModel.getInstance(this.getContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.filter_locations, null);
        final ViewGroup placeHolder = view.findViewById(R.id.filter_locations_placeholder);

        ImageButton doneButton = view.findViewById(R.id.done_button);

        CheckBox selectAll = view.findViewById(R.id.fl_select_all);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                ArrayList<POModel.LocationStatus> filteredLocations = poModel.getFilteredLocations();
                for (int i = 0; i < filteredLocations.size(); i++) {
                    POModel.LocationStatus rs = filteredLocations.get(i);
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

        ArrayList<POModel.LocationStatus> filteredLocations = poModel.getFilteredLocations();
        for ( int i=0; i < filteredLocations.size(); i++ ){

            POModel.LocationStatus ls = filteredLocations.get(i);
            Location location = ls.getLocation();
            
            LocationCheckBox checkBox = new LocationCheckBox(getContext());
            checkBox.setText(location.getTitle());
            checkBox.setLocation(location);
            checkBox.setChecked(ls.isAvailable());
            checkBox.setOnCheckedChangeListener(this);
            placeHolder.addView(checkBox);

        }

        doneButton.setOnClickListener(new View.OnClickListener() {
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

        if ( buttonView instanceof LocationCheckBox ){


            LocationCheckBox lcb = (LocationCheckBox) buttonView;
            int locationId = lcb.getLocation().getDatabaseRecordNo();
            ArrayList<POModel.LocationStatus> filteredLocations = poModel.getFilteredLocations();
            for ( int i=0; i < filteredLocations.size(); i++ ){

                POModel.LocationStatus ls = filteredLocations.get(i);
                Location location = ls.getLocation();
                if (location.getDatabaseRecordNo() == locationId ){
                    ls.setAvailable(isChecked);
                    poModel.refreshPlannedActivityArrayAdapter();
                    poModel.refreshToDoArrayAdapter();
                    poModel.refreshDiaryEntryArrayAdapter();
                    poModel.refreshActivityArrayAdapter();
                }

            }

        }

    }
}




