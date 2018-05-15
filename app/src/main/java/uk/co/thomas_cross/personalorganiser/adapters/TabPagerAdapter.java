package uk.co.thomas_cross.personalorganiser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;

import uk.co.thomas_cross.personalorganiser.DailyDiaryFragment;
import uk.co.thomas_cross.personalorganiser.DailyScheduleFragment;
import uk.co.thomas_cross.personalorganiser.ToDoListFragment;

/**
 * Created by thomas on 05/03/18.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;
    DailyScheduleFragment tab1 = null;
    DailyDiaryFragment tab2 = null;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs){
        super(fm);
        this.tabCount = numberOfTabs;
        tab1 = new DailyScheduleFragment();
        tab2 = new DailyDiaryFragment();
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                return tab1;
            case 1:
                return tab2;
            case 2:
                ToDoListFragment tab3 = new ToDoListFragment();
                return tab3;
            default:
                return null;

        }

    }


    public void refresh(Calendar calendar){
        tab1.setCurrentDate(calendar);
        tab2.setCurrentDate(calendar);
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
