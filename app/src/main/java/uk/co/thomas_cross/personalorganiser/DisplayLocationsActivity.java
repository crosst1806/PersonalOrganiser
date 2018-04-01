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

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Location;
import uk.co.thomas_cross.personalorganiser.entities.tor.LocationTor;

public class DisplayLocationsActivity extends AppCompatActivity
                    implements LocationCrudDialogFragment.LocationCrudDialogListener {

    ListView listView;
    ArrayList<Location> listitems = new ArrayList();
    LocationsArrayAdapter adapter;
    LocationTor locationTor = new LocationTor();

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
                showLocationCrudDialog(LocationCrudDialogFragment.READ_MODE, r);
            }

        });


        adapter = new LocationsArrayAdapter(this,listitems);
        listView.setAdapter(adapter);

        POModel poModel = new POModel(this);
        listitems = poModel.getLocations();
        adapter.clear();
        adapter.addAll(listitems);
        adapter.sort(locationTor);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location newLocation = new Location();
                newLocation.setOwner(1);
                newLocation.setOwnerType(Ownable.PERSON);
                showLocationCrudDialog(LocationCrudDialogFragment.CREATE_MODE, newLocation);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showLocationCrudDialog(int mode, Location role) {
        FragmentManager fm = getSupportFragmentManager();
        LocationCrudDialogFragment dialogFragment = LocationCrudDialogFragment.newInstance(mode,role);
        dialogFragment.show(fm, "role_crud_form");
    }

    @Override
    public void onDialogPositiveClick(Location role) {

        POModel poModel = new POModel(this);
        ArrayList<Location> locations = poModel.getLocations();
        adapter.clear();
        adapter.addAll(locations);
        adapter.sort(locationTor);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick() {

    }
}
