package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.Activity;

/**
 * Created by thomas on 30/03/18.
 */

public class ActivityTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        Activity a1 = (Activity) o1;
        Activity a2 = (Activity) o2;
        return a1.getDescription().compareTo(a2.getDescription());

    }
}
