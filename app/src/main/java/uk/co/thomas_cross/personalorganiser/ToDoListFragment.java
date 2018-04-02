package uk.co.thomas_cross.personalorganiser;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoListFragment extends Fragment {


    public ToDoListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do_lists, container, false);

        POModel poModel = new POModel(getContext());
        ListView listView = view.findViewById(R.id.list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ToDo toDo = (ToDo) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(),"To Selected "+toDo,Toast.LENGTH_SHORT);

            }
        });
        ArrayList<ToDo> toDos = poModel.getToDos();
        ToDoArrayAdapter adapter = new ToDoArrayAdapter(getContext(), toDos);
        listView.setAdapter(adapter);

        return view;
    }

}
