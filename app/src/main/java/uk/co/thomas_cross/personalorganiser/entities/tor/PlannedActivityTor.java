package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.PlannedActivity;

/**
 * Created by thomas on 30/03/18.
 */

public class PlannedActivityTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        PlannedActivity pa1 = (PlannedActivity) o1;
        PlannedActivity pa2 = (PlannedActivity) o2;
        int result = pa1.getStartDate().compareTo(pa2.getStartDate());
        if ( result == 0 ){
            result = pa1.getStartTime().compareTo(pa2.getStartTime());
        }
        return result;

    }
}
