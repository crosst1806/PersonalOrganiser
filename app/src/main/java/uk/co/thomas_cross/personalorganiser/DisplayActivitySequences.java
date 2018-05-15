package uk.co.thomas_cross.personalorganiser;

import android.content.Context;
import android.content.Intent;
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
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.adapters.ActivitySequenceArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.model.POModel;

public class DisplayActivitySequences extends AppCompatActivity {

    ListView listView;
    ActivitySequenceArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activity_sequences);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivitySequenceList asl = (ActivitySequenceList) parent.getItemAtPosition(position);
                fireup(asl);
            }

        });



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ActivitySequenceList asl = (ActivitySequenceList) parent.getItemAtPosition(position);
                showActivitySequenceCrudDialog(ActivityCrudDialog.READ_MODE, asl);
                return true;
            }
        });


        POModel poModel = POModel.getInstance(this);
        adapter = poModel.getActivitySequenceArrayAdapter(this);
        listView.setAdapter(adapter);


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySequenceList asl = new ActivitySequenceList();
                asl.setOwner(1);
                asl.setOwnerType(Ownable.PERSON);
                asl.setTimeStamp(storageFormat.format(new Date()));
                asl.setLastModifiedBy(1);
                showActivitySequenceCrudDialog(ActivityCrudDialog.CREATE_MODE, asl);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fireup(ActivitySequenceList asl){
        Intent intent = new Intent(this, DisplayActivitySequenceListItems.class);
        intent.putExtra("asl",asl.getDatabaseRecordNo());
        startActivity(intent);

    }

    private void showActivitySequenceCrudDialog(int mode, ActivitySequenceList asl) {
        FragmentManager fm = getSupportFragmentManager();
        ActivitySequenceCrudDialog dialogFragment = ActivitySequenceCrudDialog.newInstance(mode,asl);
        dialogFragment.show(fm, "crud_form_activity_sequence");
    }

}
