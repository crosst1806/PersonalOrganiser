package uk.co.thomas_cross.personalorganiser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.adapters.ToDoArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoListFragment extends Fragment {

    private POModel poModel;
    private ToDoArrayAdapter adapter;

    public ToDoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do_lists, container, false);

        poModel = POModel.getInstance(getContext());
        ListView listView = view.findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ToDo toDo = (ToDo) parent.getItemAtPosition(position);
                showToDoCrudDialog(ToDoCrudDialog.READ_MODE,toDo);

            }
        });
        ArrayList<ToDo> toDos = poModel.getToDos();
        adapter = poModel.getToDoArrayAdapter(getContext());
        listView.setAdapter(adapter);

        return view;
    }

    private void showToDoCrudDialog(int mode, ToDo toDo) {
        FragmentManager fm = getFragmentManager();
        ToDoCrudDialog dialogFragment = ToDoCrudDialog.newInstance(mode,toDo);
        dialogFragment.setTargetFragment(ToDoListFragment.this,300);
        dialogFragment.show(fm, "crud_form_to_do");
    }


}
