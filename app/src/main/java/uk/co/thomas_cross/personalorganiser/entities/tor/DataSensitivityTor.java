package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.DataSensitivity;

/**
 * Created by thomas on 23/03/18.
 */

public class DataSensitivityTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        DataSensitivity ds1 = (DataSensitivity) o1;
        DataSensitivity ds2 = (DataSensitivity) o2;

        int result = 0;

        if ( ds1.getDatabaseRecordNo() == 0 )
            return -1;
        if ( ds2.getDatabaseRecordNo() == 0 )
            return +1;

        result = ds1.getTitle().compareTo(ds2.getTitle());

        return result;
    }
}
