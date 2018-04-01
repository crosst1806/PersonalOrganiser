package uk.co.thomas_cross.personalorganiser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.tor.ActivityTor;
import uk.co.thomas_cross.personalorganiser.model.POModel;

public class DisplayActivitysActivity extends AppCompatActivity
                    implements ActivityCrudDialogFragment.ActivityCrudDialogListener {

    ListView listView;
    ArrayList<Activity> listitems = new ArrayList();
    ActivityArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    ActivityTor activityTor = new ActivityTor();

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
                showActivityCrudDialog(ActivityCrudDialogFragment.READ_MODE, r);
            }

        });


        adapter = new ActivityArrayAdapter(this,listitems);
        listView.setAdapter(adapter);

        POModel poModel = new POModel(this);
        listitems = poModel.getActivitys();
        adapter.clear();
        adapter.addAll(listitems);
        adapter.sort(activityTor);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity newActivity = new Activity();
                newActivity.setOwner(1);
                newActivity.setOwnerType(Ownable.PERSON);
                newActivity.setTimeStamp(storageFormat.format(new Date()));
                newActivity.setLastModifiedBy(1);
                showActivityCrudDialog(ActivityCrudDialogFragment.CREATE_MODE, newActivity);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showActivityCrudDialog(int mode, Activity role) {
        FragmentManager fm = getSupportFragmentManager();
        ActivityCrudDialogFragment dialogFragment = ActivityCrudDialogFragment.newInstance(mode,role);
        dialogFragment.show(fm, "activity_crud_form");
    }

    @Override
    public void onDialogPositiveClick(Activity activity) {
        POModel poModel = new POModel(this);
        ArrayList<Activity> activitys = poModel.getActivitys();
        adapter.clear();
        adapter.addAll(activitys);
        adapter.sort(activityTor);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick() {

    }
}
