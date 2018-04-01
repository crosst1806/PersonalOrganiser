package uk.co.thomas_cross.personalorganiser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.Person;
import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by thomas on 10/03/18.
 */

public class RoleArrayAdapter extends ArrayAdapter {

    private POModel poModel = null;

    public RoleArrayAdapter(@NonNull Context context, ArrayList<Role> listItems) {
        super(context, 0, listItems);
        this.poModel = new POModel(context);
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
//        TextView timeStampView = (TextView) convertView.findViewById(R.id.rli_time_stamp);
//        TextView lastModifiedByView = (TextView) convertView.findViewById(R.id.rli_last_modified_by);

        ArrayList<Person> people = new ArrayList<>();
        people = this.poModel.getPersons();
        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(role.getDatabaseRecordNo()));
//        String owner = "Unknown";
//        String ownerType = "Unknown";
//        switch (role.getOwnerType()) {
//            case Ownable.NONE:
//                ownerType = "None";
//                break;
//            case Ownable.PERSON:
//                Person p = this.poModel.getPerson(role.getOwner());
//                if ( p != null )
//                    owner = p.getFirstName() + " " + p.getLastName();
//                ownerType = "Person";
//                break;
//            case Ownable.TEAM:
//                ownerType = "Team";
//                break;
//            case Ownable.GROUP:
//                ownerType = "Group";
//                break;
//            case Ownable.ORGANISATION:
//                ownerType = "Organisation";
//                break;
//        }
//        ownerView.setText(owner);
//        ownerTypeView.setText(ownerType);

        titleView.setText(role.getTitle());
        DataSensitivity ds = poModel.getDataSensitivity(role.getDataSensitivity());
        String t = "unknown";
        if ( ds != null )
            t = ds.getTitle();
        dataSensitivityView.setText(t);
//        timeStampView.setText(role.getTimeStamp());
//        String lmb = "unknown";
//        UserId userId = poModel.getUserId(role.getLastModifiedBy());
//        if ( userId != null )
//            lmb = userId.getUserName();
//        lastModifiedByView.setText(lmb);

        // Return the completed view to render on screen
        return convertView;
    }

}

