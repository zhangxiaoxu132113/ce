package com.water.crawl.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhangmiaojie on 2017/5/2.
 */
public class DateUtil {
    public static SimpleDateFormat DATE_FORMAT_M = new SimpleDateFormat("MM");
    public static SimpleDateFormat DATE_FORMAT_Y = new SimpleDateFormat("yyyy");
    public static SimpleDateFormat DATE_FORMAT_MD = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat DATE_FORMAT_YMD = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat DATE_FORMAT_YMD_WITHOUT_SEPARATOR = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat DATE_FORMAT_YMD_ = new SimpleDateFormat("yyyy_MM_dd");
    public static SimpleDateFormat DATE_FORMAT_YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat DATE_FORMAT_YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat DATE_FORMAT_ZONE = new SimpleDateFormat(
            "dd/MMM/yyyy:HH:mm:ss Z", new Locale("ENGLISH", "CHINA"));

    /**
     * 获取当前时间的0点时分秒
     *
     * @return long
     */
    public static long getDefaultDayTimeInMillis() {
        return getCalendar(null, null).getTimeInMillis();
    }

    /**
     * 获取某一天的0点时分秒
     *
     * @param i 天数 [负数为过去的天数，正数为未来的天数]
     * @return long
     */
    public static long getDayTimeInMillis(Object datetime, Integer i) {
        return getCalendar(datetime, i).getTimeInMillis();
    }

    /**
     * 获取当前时间的0点时分
     *
     * @return Date
     */
    public static Date getDefaultDayDate() {
        return getCalendar(null, null).getTime();
    }

    /**
     * 获取某一天的0点时分
     *
     * @param i 天数 [负数为过去的天数，正数为未来的天数]
     * @return Date
     */
    public static Date getDayDate(Object datetime, Integer i) {
        return getCalendar(datetime, i).getTime();
    }

    /**
     * 获取当前时间的0点时分秒
     *
     * @return Calendar
     */
    public static Calendar getDefaultCalendar() {
        return getCalendar(null, null);
    }

    /**
     * 获取某一天的0点时分秒
     *
     * @param i 天数 [负数为过去的天数，正数为未来的天数]
     * @return Calendar
     */
    public static Calendar getCalendar(Object datetime, Integer i) {
        Calendar calendar = Calendar.getInstance();
        if (datetime != null) {
            if (datetime instanceof Long) {
                calendar.setTimeInMillis((Long) datetime);
            } else if (datetime instanceof Date) {
                calendar.setTime((Date) datetime);
            }
        }
        if (i != null) {
            calendar.add(Calendar.DATE, i);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}
