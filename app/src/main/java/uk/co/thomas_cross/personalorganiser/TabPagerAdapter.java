package uk.co.thomas_cross.personalorganiser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by thomas on 05/03/18.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs){
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0:
                DailyScheduleFragment tab1 = new DailyScheduleFragment();
                return tab1;
            case 1:
                DailyDiaryFragment tab2 = new DailyDiaryFragment();
                return tab2;
            case 2:
                ToDoListFragment tab3 = new ToDoListFragment();
                return tab3;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
