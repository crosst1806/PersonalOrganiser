package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

/**
 * Created by thomas on 30/03/18.
 */

public class MedianMinuteTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        Integer a1 = (Integer) o1;
        Integer a2 = (Integer) o2;
        return a1.intValue()-a2.intValue();

    }
}
