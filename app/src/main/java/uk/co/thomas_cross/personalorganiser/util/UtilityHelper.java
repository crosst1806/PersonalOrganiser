package uk.co.thomas_cross.personalorganiser.util;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by thomas on 26/03/18.
 */

public class UtilityHelper {

    public static final int Format_25_Dec_2018 = 0;
    public static final int Format_Tue_25_Dec_2018 = 1;
    public static final int Format_21_59 = 2;
    public static final int Format_Tue_25_Dec_2018_23_59 = 3;
    public static final int Format_25_12_2018 = 4;
    public static final int Format_Tue_25_12_2018 = 5;
    public static final int Format_TimeStamp_Storage = 6;
    public static final int Format_yyyyMMdd = 7;
    public static final int Format_TimeStamp_Database = 8;

    public static void selectSpinnerIndex(
                            Spinner spinner,
                                SpinnerAdapter adapter,
                                            Object object){
        if ( object == null )
            return;

        for ( int i=0; i < adapter.getCount(); i++ ){
            Object o1 = adapter.getItem(i);
            if ( o1.toString().equals(object.toString()))
                spinner.setSelection(i);
        }
    }

    public static SimpleDateFormat getSimpleDateFormatter(int format){

        SimpleDateFormat sdf = null;
        switch (format){
            case Format_25_Dec_2018:
                sdf = new SimpleDateFormat("dd MMM yyyy");
                break;
            case Format_Tue_25_Dec_2018:
                sdf = new SimpleDateFormat("EEE dd MMM yyyy");
                break;
            case Format_21_59:
                sdf = new SimpleDateFormat("HH:mm");
                break;
            case Format_Tue_25_Dec_2018_23_59:
                sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
                break;
            case Format_25_12_2018:
                sdf = new SimpleDateFormat("dd MM yyyy");
                break;
            case Format_Tue_25_12_2018:
                sdf = new SimpleDateFormat("EEE dd MM yyyy");
                break;
            case Format_TimeStamp_Storage:
                sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:sss");
                break;
            case Format_yyyyMMdd:
                sdf = new SimpleDateFormat("yyyyMMdd");
                break;
        }


        return sdf;
    }

    public static Calendar getCalendarFromTimeStamp(String timeStamp){

        Calendar cal = Calendar.getInstance();
        if ( timeStamp.length() < 13 )
            return cal;
        String yearX = timeStamp.substring(6,10);
        String monthX = timeStamp.substring(3,5);
        String dayX = timeStamp.substring(0,2);
        String hourX = timeStamp.substring(11,13);
        String minuteX = timeStamp.substring(14,16);
        String secondX = timeStamp.substring(17);

        cal.set(Calendar.YEAR,Integer.parseInt(yearX));
        cal.set(Calendar.MONTH,Integer.parseInt(monthX)-1);
        cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dayX));
        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hourX));
        cal.set(Calendar.MINUTE,Integer.parseInt(minuteX));
        cal.set(Calendar.SECOND,Integer.parseInt(secondX));

        return cal;
    }

    public static Calendar getCalendarFromStartDate(String startDate){

        Calendar cal = Calendar.getInstance();
        if ( startDate.length() != 8 )      // yyyyMMdd
            return cal;
        String yearX = startDate.substring(0,4);
        String monthX = startDate.substring(4,6);
        String dayX = startDate.substring(6);

        cal.set(Calendar.YEAR,Integer.parseInt(yearX));
        cal.set(Calendar.MONTH,Integer.parseInt(monthX)-1);
        cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dayX));

        return cal;
    }

    public static Calendar getCalendarFromStartDateAndTime(String startDateAndTime){

        Calendar cal = Calendar.getInstance();
        if ( startDateAndTime.length() != 13 )      // yyyyMMddHH:mm
            return cal;
        String yearX = startDateAndTime.substring(0,4);
        String monthX = startDateAndTime.substring(4,6);
        String dayX = startDateAndTime.substring(6,8);
        String hourX = startDateAndTime.substring(8,10);
        String minuteX = startDateAndTime.substring(11);

        cal.set(Calendar.YEAR,Integer.parseInt(yearX));
        cal.set(Calendar.MONTH,Integer.parseInt(monthX)-1);
        cal.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dayX));
        cal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hourX));
        cal.set(Calendar.MINUTE,Integer.parseInt(minuteX));

        return cal;
    }
}
