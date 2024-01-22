package org.rmj.g3appdriver.etc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFormatter {

    public static String ParseDateFullyDetailed(String date){
        try {
            if (date != null) {
                Date parseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                return new SimpleDateFormat("MMMM dd, yyyy").format(parseDate);
            } else {
                return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String ParseDateForList(String date){
        try {
            if (date != null) {
                Date parseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
                return new SimpleDateFormat("MMMM dd, yyyy").format(parseDate);
            } else {
                return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
