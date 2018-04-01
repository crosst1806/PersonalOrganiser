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

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;
import uk.co.thomas_cross.personalorganiser.entities.tor.DataSensitivityTor;

public class DisplayDataSensitivitysActivity extends AppCompatActivity
                    implements DataSensitivityCrudDialogFragment.DataSensitivityCrudDialogListener {

    ListView listView;
    ArrayList<DataSensitivity> listitems = new ArrayList();
    DataSensitivityArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    DataSensitivityTor dataSensitivityTor = new DataSensitivityTor();

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
                showDataSensitivityCrudDialog(DataSensitivityCrudDialogFragment.READ_MODE, r);
            }

        });


        adapter = new DataSensitivityArrayAdapter(this,listitems);
        listView.setAdapter(adapter);

        POModel poModel = new POModel(this);
        listitems = poModel.getDataSensitivitys();
        adapter.clear();
        adapter.addAll(listitems);
        adapter.sort(dataSensitivityTor);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataSensitivity newDataSensitivity = new DataSensitivity();
                newDataSensitivity.setOwner(1);
                newDataSensitivity.setOwnerType(Ownable.PERSON);
                showDataSensitivityCrudDialog(DataSensitivityCrudDialogFragment.CREATE_MODE, newDataSensitivity);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showDataSensitivityCrudDialog(int mode, DataSensitivity dataSensitivity) {
        FragmentManager fm = getSupportFragmentManager();
        DataSensitivityCrudDialogFragment dialogFragment = DataSensitivityCrudDialogFragment.newInstance(mode,dataSensitivity);
        dialogFragment.show(fm, "data_sensitivity_crud_form");
    }

    @Override
    public void onDialogPositiveClick(DataSensitivity dataSensitivity) {
        POModel poModel = new POModel(this);
        ArrayList<DataSensitivity> dataSensitivitys = poModel.getDataSensitivitys();
        adapter.clear();
        adapter.addAll(dataSensitivitys);
        adapter.sort(dataSensitivityTor);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick() {

    }
}
