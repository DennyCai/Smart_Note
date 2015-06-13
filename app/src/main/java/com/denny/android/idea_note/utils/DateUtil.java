package com.denny.android.idea_note.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hasee on 2015/6/14.
 */
public class DateUtil {
    public static String format(Date date){
        return new SimpleDateFormat("MM月 dd日 HH:mm:ss").format(date);
    }
    public static String format(long time) {
        return format(new Date(time));
    }

}
