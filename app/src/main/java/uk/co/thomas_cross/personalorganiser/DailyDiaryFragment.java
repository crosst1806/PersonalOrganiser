package uk.co.thomas_cross.personalorganiser;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

import uk.co.thomas_cross.personalorganiser.adapters.DiaryEntryArrayAdapter;
import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;
import uk.co.thomas_cross.personalorganiser.model.POModel;
import uk.co.thomas_cross.personalorganiser.util.UtilityHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyDiaryFragment extends Fragment {

    private POModel poModel;
    private DiaryEntryArrayAdapter diaryEntryArrayAdapter;
    private TextView currentDateView;

    public DailyDiaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("PersonalOrganiser", "onCreateView() " + savedInstanceState);

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_daily_diary, container, false);
        currentDateView = view.findViewById(R.id.diary_display_date);
        poModel = POModel.getInstance(getContext());
        setCurrentDate(poModel.getCurrentDate());

        ListView listView = view.findViewById(R.id.dairy_entry_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DiaryEntry diaryEntry = (DiaryEntry) parent.getItemAtPosition(position);
                showDiaryEntryCrudDialog(DiaryEntryCrudDialog.READ_MODE,diaryEntry);

            }
        });
        diaryEntryArrayAdapter = poModel.getDiaryEntryArrayAdapter(getContext());
        listView.setAdapter(diaryEntryArrayAdapter);
        return view;
    }

    private void showDiaryEntryCrudDialog(int mode, DiaryEntry diaryEntry) {
        FragmentManager fm = getFragmentManager();
        DiaryEntryCrudDialog dialogFragment = DiaryEntryCrudDialog.newInstance(mode,diaryEntry);
        dialogFragment.setTargetFragment(DailyDiaryFragment.this,301);
        dialogFragment.show(fm, "crud_form_diary_entry");
    }

    public void setCurrentDate(Calendar calendar) {
        SimpleDateFormat sdf = UtilityHelper.getSimpleDateFormatter(UtilityHelper.Format_Tue_25_Dec_2018);
        currentDateView.setText(sdf.format(calendar.getTime()));

    }

}
