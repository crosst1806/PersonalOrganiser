package uk.co.thomas_cross.personalorganiser;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.model.POModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyScheduleFragment extends Fragment {


    public DailyScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_schedule, container, false);
        POModel poModel = new POModel(getContext());
        ListView listView = view.findViewById(R.id.timetable_list_view);
        listView.setDivider(null);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//
//                DiaryEntry diaryEntry = (DiaryEntry) parent.getItemAtPosition(position);
//
//                Snackbar.make(view,"Selected "+diaryEntry.getTextEntry().substring(0,20),Snackbar.LENGTH_LONG);

//            }
//        });
        ArrayList<PlannedActivity> pas = poModel.getPlannedActivitys();
        PlannedActivityArrayAdapter paArrayAdapter =
                                new PlannedActivityArrayAdapter(getContext(),pas);
        listView.setAdapter(paArrayAdapter);
        return view;

    }



}
