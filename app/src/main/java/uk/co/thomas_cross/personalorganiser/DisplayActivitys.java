package uk.co.thomas_cross.personalorganiser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.adapters.ActivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.tor.ActivityTor;
import uk.co.thomas_cross.personalorganiser.model.POModel;

public class DisplayActivitys extends AppCompatActivity {

    ListView listView;
    ActivityArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activitys);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Activity r = (Activity) parent.getItemAtPosition(position);
                showActivityCrudDialog(ActivityCrudDialog.READ_MODE, r);
            }

        });

        POModel poModel = POModel.getInstance(this);
        adapter = poModel.getActivityArrayAdapter(this);
        listView.setAdapter(adapter);


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity newActivity = new Activity();
                newActivity.setOwner(1);
                newActivity.setOwnerType(Ownable.PERSON);
                newActivity.setTimeStamp(storageFormat.format(new Date()));
                newActivity.setLastModifiedBy(1);
                showActivityCrudDialog(ActivityCrudDialog.CREATE_MODE, newActivity);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activitys_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.filter_activity_by_role) {
            showFilterByRoleDialog();
            return true;
        }

        if ( id == R.id.filter_activity_by_location){
            showFilterByLocationDialog();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showActivityCrudDialog(int mode, Activity role) {
        FragmentManager fm = getSupportFragmentManager();
        ActivityCrudDialog dialogFragment = ActivityCrudDialog.newInstance(mode,role);
        dialogFragment.show(fm, "crud_form_activity");
    }
    private void showFilterByRoleDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterRolesDialog filterRolesDialog = FilterRolesDialog.newInstance();
        filterRolesDialog.show(fm, "filter_roles");
    }

    private void showFilterByLocationDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FilterByLocationDialog dialog = FilterByLocationDialog.newInstance();
        dialog.show(fm,"tag");
    }


}
