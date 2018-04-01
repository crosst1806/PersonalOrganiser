package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

/**
 * Created by thomas on 25/03/18.
 */

public interface EntityComparator extends Comparator {

    public void setSortMethodology(int methodology);
    public int getSortMethodology();
}

