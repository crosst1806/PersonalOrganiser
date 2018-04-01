package uk.co.thomas_cross.personalorganiser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.entities.UserId;
import uk.co.thomas_cross.personalorganiser.model.POModel;

/**
 * Created by thomas on 10/03/18.
 */

public class DiaryEntryArrayAdapter extends ArrayAdapter {


    public DiaryEntryArrayAdapter(@NonNull Context context, ArrayList<DiaryEntry> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        DiaryEntry diaryEntry = (DiaryEntry) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.diary_entry_list_item, parent, false);
        }
        // Lookup view for data population
//        TextView idView = (TextView) convertView.findViewById(R.id.td_record_no);
        TextView roleView = (TextView) convertView.findViewById(R.id.de_role);
        TextView locationView = (TextView) convertView.findViewById(R.id.de_location);
        TextView dateTimeView = (TextView) convertView.findViewById(R.id.de_date_time);
        TextView textEntryView = (TextView) convertView.findViewById(R.id.de_text_entry);
        TextView dataSensitivityView = (TextView) convertView.findViewById(R.id.de_data_sensitivity);
        TextView timeStampView = (TextView) convertView.findViewById(R.id.de_time_stamp);
        TextView lastModifiedByView = (TextView) convertView.findViewById(R.id.de_last_mod_by);


        POModel poModel = new POModel(getContext());
        // Populate the data into the template view using the data object
//        idView.setText(String.valueOf(toDo.getDatabaseRecordNo()));
        Role r = poModel.getRole(diaryEntry.getRole());
        roleView.setText(r.getTitle());
        Location location = poModel.getLocation(diaryEntry.getLocation());
        locationView.setText(location.getTitle());
        dateTimeView.setText(diaryEntry.getDateTime().substring(0, 19));
        textEntryView.setText(diaryEntry.getTextEntry());
        DataSensitivity ds = poModel.getDataSensitivity(diaryEntry.getDataSensitivity());
        dataSensitivityView.setText(ds.getTitle());
        UserId userId = poModel.getUserId(diaryEntry.getLastModifiedBy());
        lastModifiedByView.setText(userId.getUserName());
        timeStampView.setText(diaryEntry.getTimeStamp());

        // Return the completed view to render on screen
        return convertView;
    }

}

