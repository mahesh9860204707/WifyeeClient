package com.wifyee.greenfields.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConvert {
    public static String getDate(String date){

        String finalDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.US);

        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a",Locale.US);
        finalDate = timeFormat.format(myDate);

        return finalDate;
    }
}
