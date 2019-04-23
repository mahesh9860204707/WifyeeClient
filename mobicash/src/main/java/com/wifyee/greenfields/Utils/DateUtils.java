package com.wifyee.greenfields.Utils;

/**
 * Created by amit on 2/20/2018.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
    public static void main(String[] args) {

        DateUtils obj = new DateUtils();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        try {
            Date date1 = simpleDateFormat.parse("10/10/2013 11:30:10");
            obj.printDifference(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public void printDifference(Date startDate){

        //milliseconds
        long different = startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }
}
