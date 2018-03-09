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
 * Created by root on 07/01/18.
 */

public class RoleAdapter extends ArrayAdapter<Role> {

    ArrayList<Role> roles = new ArrayList<Role>();

    public RoleAdapter(@NonNull Context context,
                       ArrayList<Role> list) {

        super(context,0,list);
        this.roles = list;
    }


    public View getView(int position,View convertView,ViewGroup parent) {

        View rowView = convertView;

        if ( rowView == null ){

            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        }

        Role role = roles.get(position);

        TextView idView = (TextView) rowView.findViewById(R.id.role_id);
        TextView ownerView = (TextView) rowView.findViewById(R.id.role_owner);
        TextView ownerTypeView = (TextView) rowView.findViewById(R.id.role_owner_type);
        TextView roleTitleView = (TextView) rowView.findViewById(R.id.role_title);
        TextView roleDataSensitivityView = (TextView) rowView.findViewById(R.id.role_data_sensitivity);
        TextView roleTimeStampView = (TextView) rowView.findViewById(R.id.role_time_stamp);
        TextView roleLastModifiedByView = (TextView) rowView.findViewById(R.id.role_last_modified_by);

        idView.setText(role.getDatabaseRecordNo());
        ownerView.setText(role.getOwner());
        ownerTypeView.setText(role.getOwnerType());
        roleTitleView.setText(role.getTitle());
        roleDataSensitivityView.setText(role.getDataSensitivity());
        roleTimeStampView.setText(role.getTimeStamp());
        roleLastModifiedByView.setText(role.getLastModifiedBy());

        return rowView;
    }
}
