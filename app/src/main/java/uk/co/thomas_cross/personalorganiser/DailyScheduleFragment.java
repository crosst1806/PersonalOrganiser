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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import uk.co.thomas_cross.personalorganiser.adapters.PlannedActivityArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;

import static uk.co.thomas_cross.personalorganiser.util.UtilityHelper.Format_Tue_25_Dec_2018;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyScheduleFragment extends Fragment {

    private POModel poModel;
    private PlannedActivityArrayAdapter paArrayAdapter;
    private TextView currentDateView;

    public DailyScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_schedule, container, false);
        currentDateView = view.findViewById(R.id.timetable_display_date);
        poModel = POModel.getInstance(getContext());
        setCurrentDate(poModel.getCurrentDate());

        ListView listView = view.findViewById(R.id.timetable_list_view);
        listView.setDivider(null);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlannedActivity plannedActivity = (PlannedActivity) parent.getItemAtPosition(position);
                switch (plannedActivity.getStatus()) {
                    case PlannedActivity.PENDING:
                    case PlannedActivity.EXECUTING:
                    case PlannedActivity.PAUSED:
                        showPlannedActivityActionDialog(plannedActivity);
                        break;
                    case PlannedActivity.COMPLETED:
                    case PlannedActivity.NOT_NECESSARY:
                    case PlannedActivity.LACK_OF_TIME:
                    case PlannedActivity.ADVERSE_WEATHER:
                    case PlannedActivity.NO_ACCESS:
                        // do nothing
                        break;
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PlannedActivity plannedActivity = (PlannedActivity) parent.getItemAtPosition(position);
                showPlannedActivityCrudDialog(PlannedActivityCrudDialog.READ_MODE, plannedActivity);
                return true;
            }
        });
        paArrayAdapter = poModel.getPlannedActivityArrayAdapter(getContext());
        listView.setAdapter(paArrayAdapter);
        return view;

    }

    private void showPlannedActivityCrudDialog(int mode, PlannedActivity plannedActivity) {
        FragmentManager fm = getFragmentManager();
        PlannedActivityCrudDialog dialogFragment = PlannedActivityCrudDialog.newInstance(mode, plannedActivity);
        dialogFragment.setTargetFragment(DailyScheduleFragment.this, 300);
        dialogFragment.show(fm, "crud_form_planned_activity");
    }

    private void showPlannedActivityActionDialog(PlannedActivity plannedActivity) {
        FragmentManager fm = getFragmentManager();
        PlannedActivityActionDialog dialog = PlannedActivityActionDialog.newInstance(plannedActivity);
        dialog.setTargetFragment(DailyScheduleFragment.this, 301);
        dialog.show(fm, "planned_activity_action");
    }

    public void setCurrentDate(Calendar calendar) {
        SimpleDateFormat sdf = UtilityHelper.getSimpleDateFormatter(Format_Tue_25_Dec_2018);
        currentDateView.setText(sdf.format(calendar.getTime()));

    }

}
