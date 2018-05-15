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
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.adapters.ActivityIterationArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivityIteration;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.model.POModel;

public class DisplayActivityIterations extends AppCompatActivity {

    ListView listView;
    ActivityIterationArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activity_iterations);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityIteration iteration = (ActivityIteration) parent.getItemAtPosition(position);
                showActivityIterationEnablerDialog(iteration);
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityIteration iteration = (ActivityIteration) parent.getItemAtPosition(position);
                showActivityIterationCrudDialog(ActivityIterationCrudDialog.READ_MODE, iteration);
                return true;
            }
        });

        final POModel poModel = POModel.getInstance(this);
        adapter = poModel.getActivityIterationArrayAdapter(this);
        listView.setAdapter(adapter);


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityIteration iteration = poModel.generateActivityIteration();
                showActivityIterationCrudDialog(ActivityIterationCrudDialog.CREATE_MODE, iteration);

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

    private void showActivityIterationCrudDialog(int mode, ActivityIteration iteration) {
        FragmentManager fm = getSupportFragmentManager();
        ActivityIterationCrudDialog dialogFragment = ActivityIterationCrudDialog.newInstance(mode,iteration);
        dialogFragment.show(fm, "crud_form_activity_iteration");
    }
    private void showActivityIterationEnablerDialog(ActivityIteration iteration){
        FragmentManager fm = getSupportFragmentManager();
        ActivityIterationEnablerDialog dialogFragment = ActivityIterationEnablerDialog.newInstance(iteration);
        dialogFragment.show(fm, "activity_iteration_enabler");
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
