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

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.tor.RoleTor;

public class DisplayRolesActivity extends AppCompatActivity
                    implements RoleCrudDialogFragment.RoleCrudDialogListener {

    ListView listView;
    ArrayList<Role> listitems = new ArrayList();
    RoleArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    RoleTor roleTor = new RoleTor();

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
                showRoleCrudDialog(RoleCrudDialogFragment.READ_MODE, r);
            }

        });


        adapter = new RoleArrayAdapter(this,listitems);
        listView.setAdapter(adapter);

        POModel poModel = new POModel(this);
        listitems = poModel.getRoles();
        adapter.clear();
        adapter.addAll(listitems);
        adapter.sort(roleTor);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Role newRole = new Role();
                newRole.setOwner(1);
                newRole.setOwnerType(Ownable.PERSON);
                newRole.setTimeStamp(storageFormat.format(new Date()));
                newRole.setLastModifiedBy(1);
                showRoleCrudDialog(RoleCrudDialogFragment.CREATE_MODE, newRole);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showRoleCrudDialog(int mode, Role role) {
        FragmentManager fm = getSupportFragmentManager();
        RoleCrudDialogFragment dialogFragment = RoleCrudDialogFragment.newInstance(mode,role);
        dialogFragment.show(fm, "role_crud_form");
    }

    @Override
    public void onDialogPositiveClick(Role role) {
        POModel poModel = new POModel(this);
        ArrayList<Role> roles = poModel.getRoles();
        adapter.clear();
        adapter.addAll(roles);
        adapter.sort(roleTor);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick() {

    }
}
