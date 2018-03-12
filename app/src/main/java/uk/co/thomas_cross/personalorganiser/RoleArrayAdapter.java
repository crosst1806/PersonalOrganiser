package uk.co.thomas_cross.personalorganiser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by thomas on 10/03/18.
 */

public class RoleArrayAdapter extends ArrayAdapter {

    public RoleArrayAdapter(@NonNull Context context, ArrayList<Role> listItems) {
        super(context, 0, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Role role = (Role) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.role_list_item, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.rli_record_no);
//        TextView ownerView = (TextView) convertView.findViewById(R.id.rli_owner);
//        TextView ownerTypeView = (TextView) convertView.findViewById(R.id.rli_ownerType);
        TextView titleView = (TextView) convertView.findViewById(R.id.rli_title);
        TextView dataSensitivityView = (TextView) convertView.findViewById(R.id.rli_data_sensitivity);
//        TextView timeStampView = (TextView) convertView.findViewById(R.id.rli_owner);
//        TextView lastModifiedByView = (TextView ) convertView.findViewById(R.id.rli_last_modified_by);

        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(role.getDatabaseRecordNo()));
//        ownerView.setText(role.getOwner());
//        ownerTypeView.setText(role.getOwnerType());
        titleView.setText(role.getTitle());
        dataSensitivityView.setText(String.valueOf(role.getDataSensitivity()));
//        timeStampView.setText(role.getTimeStamp());
//        lastModifiedByView.setText(role.getLastModifiedBy());

        // Return the completed view to render on screen
        return convertView;
    }

}

