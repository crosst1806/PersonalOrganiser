package uk.co.thomas_cross.personalorganiser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.adapters.DataSensitivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;

public class DisplayDataSensitivitys extends AppCompatActivity {

    ListView listView;
    DataSensitivityArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_data_sensitivities);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSensitivity r = (DataSensitivity) parent.getItemAtPosition(position);
                showDataSensitivityCrudDialog(DataSensitivityCrudDialog.READ_MODE, r);
            }

        });

        POModel poModel = POModel.getInstance(this);
        adapter = poModel.getDataSensitivityArrayAdapter(this);
        listView.setAdapter(adapter);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSensitivity newDataSensitivity = new DataSensitivity();
                newDataSensitivity.setOwner(1);
                newDataSensitivity.setOwnerType(Ownable.PERSON);
                showDataSensitivityCrudDialog(DataSensitivityCrudDialog.CREATE_MODE, newDataSensitivity);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showDataSensitivityCrudDialog(int mode, DataSensitivity dataSensitivity) {
        FragmentManager fm = getSupportFragmentManager();
        DataSensitivityCrudDialog dialogFragment = DataSensitivityCrudDialog.newInstance(mode,dataSensitivity);
        dialogFragment.show(fm, "crud_form_data_sensitivity");
    }

}
