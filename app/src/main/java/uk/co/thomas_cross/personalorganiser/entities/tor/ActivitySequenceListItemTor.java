package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceList;
import uk.co.thomas_cross.personalorganiser.entities.ActivitySequenceListItem;

/**
 * Created by thomas on 30/03/18.
 */

public class ActivitySequenceListItemTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        ActivitySequenceListItem asli1 = (ActivitySequenceListItem) o1;
        ActivitySequenceListItem asli2 = (ActivitySequenceListItem) o2;

        int result = 0;

        result = asli1.getExecutionOrder()-asli2.getExecutionOrder();

        return result;

    }
}
