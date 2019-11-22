package com.gcloud.listedcompany.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class Util {

    public static void sleep(int min, int max) {
        try {
            max *= 1000;
            min *= 1000;
            long time = new Random().nextInt(max - min) + min;

            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Date dateParse(String dateStr, String dateFormat) {
        Date date;
        try {
            date = new SimpleDateFormat(dateFormat).parse(dateStr);
        } catch (ParseException e) {
            date = null;
            System.out.println("dateStr:  " + dateStr);
            System.out.println("dateFormat:  " + dateFormat);
            e.printStackTrace();
        }
        return date;
    }

    public static String dateParse(Date date, String dateFormat) {
        String dateStr = new SimpleDateFormat(dateFormat).format(date);

        return dateStr;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


}
