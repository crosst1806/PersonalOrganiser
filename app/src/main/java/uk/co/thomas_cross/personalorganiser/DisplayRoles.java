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
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.adapters.RoleArrayAdapter;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;

public class DisplayRoles extends AppCompatActivity {

    ListView listView;
    RoleArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_roles);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Role r = (Role) parent.getItemAtPosition(position);
                showRoleCrudDialog(RoleCrudDialog.READ_MODE, r);
            }

        });


        POModel poModel = POModel.getInstance(this);
        adapter = poModel.getRoleArrayAdapter(this);
        listView.setAdapter(adapter);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Role newRole = new Role();
                newRole.setOwner(1);
                newRole.setOwnerType(Ownable.PERSON);
                newRole.setTimeStamp(storageFormat.format(new Date()));
                newRole.setLastModifiedBy(1);
                showRoleCrudDialog(RoleCrudDialog.CREATE_MODE, newRole);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showRoleCrudDialog(int mode, Role role) {
        FragmentManager fm = getSupportFragmentManager();
        RoleCrudDialog dialogFragment = RoleCrudDialog.newInstance(mode,role);
        dialogFragment.show(fm, "crud_form_role");
    }

}
