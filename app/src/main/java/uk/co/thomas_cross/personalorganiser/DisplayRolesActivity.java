package uk.co.thomas_cross.personalorganiser;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.Role;

public class DisplayRolesActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Role> listitems = new ArrayList();
    RoleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_roles);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        adapter = new RoleArrayAdapter(this,listitems);
        listView.setAdapter(adapter);

        Role r1 = new Role();
        r1.setDatabaseRecordNo(0);
        r1.setOwner(1);
        r1.setOwnerType(1);
        r1.setTitle("Personal");
        r1.setDataSensitivity(0);
        r1.setTimeStamp("10 Mar 2018 01:42:00");
        r1.setLastModifiedBy(1);
        Role r2 = new Role();
        r2.setDatabaseRecordNo(0);
        r2.setOwner(1);
        r2.setOwnerType(1);
        r2.setTitle("UOL Employee");
        r2.setDataSensitivity(0);
        r2.setTimeStamp("10 Mar 2018 01:43:00");
        r2.setLastModifiedBy(1);
        adapter.add(r1);
        adapter.add(r2);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
