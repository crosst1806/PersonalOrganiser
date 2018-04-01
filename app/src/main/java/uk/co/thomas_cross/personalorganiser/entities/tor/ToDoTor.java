package uk.co.thomas_cross.personalorganiser.entities.tor;

import uk.co.thomas_cross.personalorganiser.entities.Role;
import uk.co.thomas_cross.personalorganiser.entities.ToDo;

/**
 * Created by thomas on 23/03/18.
 */

public class ToDoTor implements EntityComparator {

    public static final int BY_PRIORITY = 0;
    public static final int BY_TARGET_DATE_CLOSEST = 1;

    private int sortMethodology = BY_PRIORITY;

    @Override
    public void setSortMethodology(int methodology) {
        switch (methodology) {
            case BY_PRIORITY:
            case BY_TARGET_DATE_CLOSEST:
                this.sortMethodology = methodology;
                break;
        }
    }

    @Override
    public int getSortMethodology() {
        return this.sortMethodology;
    }

    @Override
    public int compare(Object o1, Object o2) {

        ToDo td1 = (ToDo) o1;
        ToDo td2 = (ToDo) o2;

        int result = 0;

        switch (this.sortMethodology) {
            case BY_PRIORITY:
                result = td1.getPriority() - td2.getPriority();
                if ( result == 0) {

                    String day1 = td1.getTargetDate().substring(0,2);
                    String day2 = td2.getTargetDate().substring(0,2);
                    String month1 = td1.getTargetDate().substring(3,5);
                    String month2 = td2.getTargetDate().substring(3,5);
                    String year1 = td1.getTargetDate().substring(6);
                    String year2 = td2.getTargetDate().substring(6);

                    result = year1.compareTo(year2);

                    if ( result == 0 )
                        result = month1.compareTo(month2);

                    if ( result == 0 )
                        result = day1.compareTo(day2);
                }
                break;
            case BY_TARGET_DATE_CLOSEST:

                String day1 = td1.getTargetDate().substring(0,2);
                String day2 = td2.getTargetDate().substring(0,2);
                String month1 = td1.getTargetDate().substring(3,5);
                String month2 = td2.getTargetDate().substring(3,5);
                String year1 = td1.getTargetDate().substring(6);
                String year2 = td2.getTargetDate().substring(6);

                result = year1.compareTo(year2);

                if ( result == 0 )
                    result = month1.compareTo(month2);

                if ( result == 0 )
                    result = day1.compareTo(day2);

                if ( result == 0 )
                    result = td1.getPriority() - td2.getPriority();
                break;
        }

        if (result == 0) {
            result = td1.getDescription().compareTo(td2.getDescription());
        }

        return result;
    }


}
