package uk.co.thomas_cross.personalorganiser.entities.tor;

import java.util.Comparator;

import uk.co.thomas_cross.personalorganiser.entities.DiaryEntry;
import uk.co.thomas_cross.personalorganiser.entities.Role;

/**
 * Created by thomas on 23/03/18.
 */

public class DiaryEntryTor implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {

        DiaryEntry de1 = (DiaryEntry) o1;
        DiaryEntry de2 = (DiaryEntry) o2;

        int result = 0;

        result = de1.getDateTime().compareTo(de2.getDateTime());

        return result;
    }
}
