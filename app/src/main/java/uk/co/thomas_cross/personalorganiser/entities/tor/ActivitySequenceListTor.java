package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.Activity;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;

/**
 * Created by thomas on 30/03/18.
 */

public class ActivitySequenceListTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        ActivitySequenceList asl1 = (ActivitySequenceList) o1;
        ActivitySequenceList asl2 = (ActivitySequenceList) o2;

        int result = 0;

        result = asl1.getRole()-asl2.getRole();

        if ( result == 0 ){
            result = asl1.getLocation() - asl2.getLocation();
        }

        if ( result == 0 ) {
            result = asl1.getDescription().compareTo(asl2.getDescription());
        }

        return result;

    }
}
