package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.Location;

/**
 * Created by thomas on 23/03/18.
 */

public class LocationTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        Location l1 = (Location) o1;
        Location l2 = (Location) o2;

        int result = 0;

        if ( l1.getDatabaseRecordNo() == 0 )
            return -1;
        if ( l2.getDatabaseRecordNo() == 0 )
            return +1;

        result = l1.getTitle().compareTo(l2.getTitle());

        return result;
    }
}
