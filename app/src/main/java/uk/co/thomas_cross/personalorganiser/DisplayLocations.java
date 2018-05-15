package uk.co.thomas_cross.personalorganiser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.adapters.LocationsArrayAdapter;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;

public class DisplayLocations extends AppCompatActivity
                    implements LocationCrudDialog.LocationCrudDialogListener {

    ListView listView;
    LocationsArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_locations);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location r = (Location) parent.getItemAtPosition(position);
                showLocationCrudDialog(LocationCrudDialog.READ_MODE, r);
            }

        });



        POModel poModel = POModel.getInstance(this);
        adapter = poModel.getLocationsArrayAdapter(this);
        listView.setAdapter(adapter);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location newLocation = new Location();
                newLocation.setOwner(1);
                newLocation.setOwnerType(Ownable.PERSON);
                showLocationCrudDialog(LocationCrudDialog.CREATE_MODE, newLocation);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showLocationCrudDialog(int mode, Location role) {
        FragmentManager fm = getSupportFragmentManager();
        LocationCrudDialog dialogFragment = LocationCrudDialog.newInstance(mode,role);
        dialogFragment.show(fm, "crud_form_role");
    }

    @Override
    public void onDialogPositiveClick(Location role) {
    }

    @Override
    public void onDialogNegativeClick() {

    }
}
