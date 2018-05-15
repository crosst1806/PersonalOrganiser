package uk.co.thomas_cross.personalorganiser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.thomas_cross.personalorganiser.adapters.ActivitySequenceArrayAdapter;
import uk.co.thomas_cross.personalorganiser.adapters.ActivitySequenceListItemArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceListItem;
import uk.co.thomas_cross.personalorganiser.entities.Ownable;
import uk.co.thomas_cross.personalorganiser.model.POModel;

public class DisplayActivitySequenceListItems extends AppCompatActivity {

    ListView listView;
    ActivitySequenceListItemArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_activity_sequence_list_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        POModel poModel = POModel.getInstance(this);
        Bundle extras = getIntent().getExtras();
        int id = extras.getInt("asl");
        final ActivitySequenceList asl = poModel.getActivitySequenceList(id);

        TextView title = findViewById(R.id.sequence_title);
        title.setText(asl.getDescription());

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivitySequenceListItem asli = (ActivitySequenceListItem) parent.getItemAtPosition(position);
                showActivitySequenceListItemsCrudDialog(ActivitySequenceListItemsCrudDialog.READ_MODE, asli);
            }

        });


        adapter = poModel.getActivitySequenceListItemArrayAdapter(this);
        listView.setAdapter(adapter);


        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySequenceListItem asli = new ActivitySequenceListItem();
                asli.setActivitySequenceList(asl.getDatabaseRecordNo());
                asli.setActivity(0);
                asli.setExecutionOrder(0);
                asli.setDuration(0);
                showActivitySequenceListItemsCrudDialog(ActivitySequenceListItemsCrudDialog.CREATE_MODE, asli);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showActivitySequenceListItemsCrudDialog(int mode, ActivitySequenceListItem asli) {
        FragmentManager fm = getSupportFragmentManager();
        ActivitySequenceListItemsCrudDialog dialogFragment =
                ActivitySequenceListItemsCrudDialog.newInstance(mode,asli);
        dialogFragment.show(fm, "crud_form_activity_sequence_list_item");
    }

}
