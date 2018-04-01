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

/**
 * Created by thomas on 10/03/18.
 */

public class DataSensitivityArrayAdapter extends ArrayAdapter {


    public DataSensitivityArrayAdapter(@NonNull Context context, ArrayList<DataSensitivity> listItems) {
        super(context, android.R.layout.simple_spinner_dropdown_item, listItems);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        DataSensitivity dataSensitivity = (DataSensitivity) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_sensitivity_list_item, parent, false);
        }
        // Lookup view for data population
        TextView idView = (TextView) convertView.findViewById(R.id.dsli_record_no);
        TextView titleView = (TextView) convertView.findViewById(R.id.dsli_title);

        // Populate the data into the template view using the data object
        idView.setText(String.valueOf(dataSensitivity.getDatabaseRecordNo()));
        titleView.setText(dataSensitivity.getTitle());

        // Return the completed view to render on screen
        return convertView;
    }

}

