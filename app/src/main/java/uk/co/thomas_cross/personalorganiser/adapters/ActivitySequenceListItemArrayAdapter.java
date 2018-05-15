package uk.co.thomas_cross.personalorganiser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.R;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceListItem;
import uk.co.thomas_cross.personalorganiser.model.POModel;

/**
 * Created by thomas on 10/03/18.
 */

public class ActivitySequenceListItemArrayAdapter extends ArrayAdapter {


    public ActivitySequenceListItemArrayAdapter(@NonNull Context context, ArrayList<ActivitySequenceListItem> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ActivitySequenceListItem asli = (ActivitySequenceListItem) getItem(position);
        POModel poModel = POModel.getInstance(getContext());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_activity_sequence_list_item, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.pongo_id);
        TextView activityView = (TextView) convertView.findViewById(R.id.pongo_activity);
        TextView executionOrderView = convertView.findViewById(R.id.pongo_execution_order);
        TextView durationView = (TextView) convertView.findViewById(R.id.pongo_duration);

        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(asli.getDatabaseRecordNo()));

        Activity a = poModel.getActivity(asli.getActivity());
        if ( a.getDatabaseRecordNo() == 0 ){
            a.setDescription("Pause");
        }
        activityView.setText(a.getDescription());

        executionOrderView.setText(String.valueOf(asli.getExecutionOrder()));
        durationView.setText(String.valueOf(asli.getDuration()));

        // Return the completed view to render on screen
        return convertView;
    }

}

