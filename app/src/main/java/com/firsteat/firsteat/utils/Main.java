package com.firsteat.firsteat.utils;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by touchmagics on 12/15/2015.
 */
public class Main {
    public static void main(String[] argv) throws Exception {
        Format formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy, hh:mm:ss.SSS a");
        String today = getFormatedTime();
        System.out.println("Today : " + today);
    }
    private static String getFormatedTime() {
        String dtStart = "2015-12-11 19:15:16";
        Date date;
        Date datetime=new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm.a");
        date = new Date();
        try {
            datetime = format.parse(dtStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);
        return datetime.toString();
    }
}
