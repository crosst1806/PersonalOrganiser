package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by thomas on 23/03/18.
 */

public class RoleTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        Role r1 = (Role) o1;
        Role r2 = (Role) o2;

        int result = 0;

        if ( r1.getDatabaseRecordNo() == 0 )
            return -1;
        if ( r2.getDatabaseRecordNo() == 0 )
            return +1;

        result = r1.getTitle().compareTo(r2.getTitle());

        return result;
    }
}
