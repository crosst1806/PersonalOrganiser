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
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.entities.tor.ToDoTor;

public class DisplayToDosActivity extends AppCompatActivity
                    implements ToDoCrudDialogFragment.ToDoCrudDialogListener {

    ListView listView;
    ArrayList<ToDo> listitems = new ArrayList();
    ToDoArrayAdapter adapter;
    SimpleDateFormat storageFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    ToDoTor toDoTor = new ToDoTor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_to_dos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDo r = (ToDo) parent.getItemAtPosition(position);
                showToDoCrudDialog(ToDoCrudDialogFragment.READ_MODE, r);
            }

        });


        adapter = new ToDoArrayAdapter(this,listitems);
        listView.setAdapter(adapter);

        POModel poModel = new POModel(this);
        listitems = poModel.getToDos();
        adapter.clear();
        adapter.addAll(listitems);
        adapter.sort(toDoTor);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDo newToDo = new ToDo();
                newToDo.setOwner(1);
                newToDo.setOwnerType(Ownable.PERSON);
                newToDo.setTimeStamp(storageFormat.format(new Date()));
                newToDo.setLastModifiedBy(1);
                showToDoCrudDialog(ToDoCrudDialogFragment.CREATE_MODE, newToDo);
            }
        });

        if (getSupportActionBar()!= null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showToDoCrudDialog(int mode, ToDo toDo) {
        FragmentManager fm = getSupportFragmentManager();
        ToDoCrudDialogFragment dialogFragment = ToDoCrudDialogFragment.newInstance(mode,toDo);
        dialogFragment.show(fm, "toDo_crud_form");
    }

    @Override
    public void onDialogPositiveClick(ToDo toDo) {
        POModel poModel = new POModel(this);
        ArrayList<ToDo> toDos = poModel.getToDos();
        adapter.clear();
        adapter.addAll(toDos);
        adapter.sort(toDoTor);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick() {

    }
}
