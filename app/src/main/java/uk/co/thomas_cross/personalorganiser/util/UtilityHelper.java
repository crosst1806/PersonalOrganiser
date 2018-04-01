package uk.co.thomas_cross.personalorganiser.util;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

/**
 * Created by thomas on 26/03/18.
 */

public class UtilityHelper {

    public static void selectSpinnerIndex(
                            Spinner spinner,
                                SpinnerAdapter adapter,
                                        ArrayList objects,
                                            Object object){

        for ( int i=0; i < adapter.getCount(); i++ ){
            Object o1 = adapter.getItem(i);
            if ( o1.toString().equals(object.toString()))
                spinner.setSelection(i);
        }
    }
}
