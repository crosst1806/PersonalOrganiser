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
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;

/**
 * Created by thomas on 10/03/18.
 */

public class ToDoArrayAdapter extends ArrayAdapter {


    public ToDoArrayAdapter(@NonNull Context context, ArrayList<ToDo> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        ToDo toDo = (ToDo) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_list_item, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.td_record_no);
        TextView roleView = (TextView) convertView.findViewById(R.id.td_role);
        TextView locationView = (TextView) convertView.findViewById(R.id.td_location);
        TextView descriptionView = (TextView) convertView.findViewById(R.id.td_description);
        TextView priorityView = (TextView) convertView.findViewById(R.id.td_priority);
        TextView targetDateTimeView = (TextView) convertView.findViewById(R.id.td_target_date_time);

        POModel poModel = new POModel(getContext());
        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(toDo.getDatabaseRecordNo()));
        Role r = poModel.getRole(toDo.getRole());
        roleView.setText(r.getTitle());
        Location location = poModel.getLocation(toDo.getLocation());
        locationView.setText(location.getTitle());
        descriptionView.setText(toDo.getDescription());
        priorityView.setText(String.valueOf(toDo.getPriority()));
        targetDateTimeView.setText(toDo.getTargetDate());

        // Return the completed view to render on screen
        return convertView;
    }

}

