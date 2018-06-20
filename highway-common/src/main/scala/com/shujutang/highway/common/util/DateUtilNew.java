package com.shujutang.highway.common.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nancr on 15/7/21.
 *
 * 日期帮助类
 */
public class DateUtilNew {
    private static String DATEPATTERN = "yyyy-MM-dd HH:mm:ss";
    private static String TIMEPATTERN = "yyyyMM";

    private static long eightHour = 28800000L;

    /**
     * 对时间进行格式化
     * @param date 时间
     * @return
     */
    public static String format(Date date){
        return  DateFormatUtils.format(date, DATEPATTERN);
    }

    public static String format(Date date, String parttern){
        return  DateFormatUtils.format(date, parttern);
    }

    public static Date parse(String time, String pattern) throws ParseException {
        return DateUtils.parseDate(time, new String[]{pattern});
    }

    /**
     * 将16进制utc时间字符串转成当前时间
     * @param utcStrTime 时间字符串
     * @return
     */
    public static String utc2LocalTimeString(String utcStrTime){
        int utcIntTime = ConvertUtil.sixteen2Decimal(utcStrTime);

        String time = DateFormatUtils.format(utcIntTime * 1000L + eightHour, DATEPATTERN);
        return time;
    }


    /**
     * 将int型utc时间转成本地时间字符串
     * @param utcIntTime int型utc时间
     * @return
     */
    public static String utc2LocalTimeString(int utcIntTime){
        String time = DateFormatUtils.format(utcIntTime * 1000L + eightHour, DATEPATTERN);
        return time;
    }

    /**
     * 将当前时间转成utc时间
     * @param currentTime 当前时间字符串
     * @param pattern 当前时间格式
     * @return
     */
    public static int time2Utc(String currentTime, String pattern){
        int utcTime = 0;
        try {
            Date date = DateUtils.parseDate(currentTime, new String[]{pattern});
            utcTime = new Double((date.getTime() - eightHour) / 1000).intValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcTime;
    }

    /**
     * 月份上加一个整数，得到新的时间字符串
     * Adds a number of months to a date returning a new date string.
     * @param ammount 添加月份的数量，可以是正数和负数
     */
    public static String addMonths4String(int ammount){
        Date nowDate = new Date();
        Date preOndeDate = DateUtils.addMonths(nowDate, ammount);
        String time = DateFormatUtils.format(preOndeDate, DATEPATTERN);
        return time;
    }

    /**
     * 加上整数个月份后的时间
     * @param startTime 开始时间
     * @param ammount 加上的月份数
     * @param datePattern 时间格式
     * @return
     * @throws ParseException
     */
    public static String addMonths4String(String startTime,int ammount, String datePattern) throws ParseException {
        Date nowDate = DateUtils.parseDate(startTime, new String[]{datePattern});
        Date preOndeDate = DateUtils.addMonths(nowDate, ammount);
        String time = DateFormatUtils.format(preOndeDate, datePattern);
        return time;
    }

    public static Date addMonths4Date(int ammount){
        Date nowDate = new Date();
        Date preOndeDate = DateUtils.addMonths(nowDate, ammount);
        return preOndeDate;
    }

    public static Date addMonths4Date(Date currentDate, int ammount){
        Date preOndeDate = DateUtils.addMonths(currentDate, ammount);
        return preOndeDate;
    }

    /**
     * 获取几个月前的时间段
     * 如：当前时间2015-07-22 10:11:00 ，得到3个月前时间段
     * 结果：[2015-05-22 10:11:00 -- 2015-06-01 00:00:00)
     * [2015-06-01 00:00:00 -- 2015-07-01 00:00:00)
     * [2015-07-01 00:00:00 -- 2015-07-22 10:11:00)
     * @param ammount
     * @return
     */
    public static List<TimeQuantum> getTimeQuantum(Date currnetDate, int ammount){
        List<TimeQuantum> list = new ArrayList<>();
        //ammount个月之前的时间
        Date preMonthDate = DateUtils.addMonths(currnetDate, ammount);

        while (true){
            //preDate下个月的开始时间
            Date nextMonthStartTime = getNextMonthStartTime(preMonthDate);
            if (nextMonthStartTime.getTime() < currnetDate.getTime()){
                TimeQuantum timeQuantum = new TimeQuantum();
                timeQuantum.setStartTime(DateFormatUtils.format(preMonthDate, DATEPATTERN));
                timeQuantum.setEndTime(DateFormatUtils.format(nextMonthStartTime, DATEPATTERN));
                list.add(timeQuantum);

                preMonthDate = nextMonthStartTime;
            }else {
                TimeQuantum timeQuantum = new TimeQuantum();
                timeQuantum.setStartTime(DateFormatUtils.format(preMonthDate, DATEPATTERN));
                timeQuantum.setEndTime(DateFormatUtils.format(currnetDate, DATEPATTERN));
                list.add(timeQuantum);
                break;
            }
        }
        return list;
    }

    public static List<TimeQuantum> getTimeQuantum(Date startDate,Date endDate){
        List<TimeQuantum> list = new ArrayList<>();
        //ammount个月之前的时间
//        Date startDate = DateUtils.addMonths(currnetDate, ammount);

        while (true){
            //preDate下个月的开始时间
            Date nextMonthStartTime = getNextMonthStartTime(startDate);
            if (nextMonthStartTime.getTime() < endDate.getTime()){
                TimeQuantum timeQuantum = new TimeQuantum();
                timeQuantum.setStartTime(DateFormatUtils.format(startDate, DATEPATTERN));
                timeQuantum.setEndTime(DateFormatUtils.format(nextMonthStartTime, DATEPATTERN));
                list.add(timeQuantum);

                startDate = nextMonthStartTime;
            }else {
                TimeQuantum timeQuantum = new TimeQuantum();
                timeQuantum.setStartTime(DateFormatUtils.format(startDate, DATEPATTERN));
                timeQuantum.setEndTime(DateFormatUtils.format(endDate, DATEPATTERN));
                list.add(timeQuantum);
                break;
            }
        }
        return list;
    }

    /**
     * 根据开始时间和结束时间 得到时间列表
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDateList(Date startDate,Date endDate){
        List<String> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        long endTime = calendar.getTimeInMillis();

        list.add(DateFormatUtils.format(startDate, TIMEPATTERN));
        while (true){
            Date nextMonthStartTime = getNextMonthStartTime(startDate);
            if (nextMonthStartTime.getTime() < endTime){
                list.add(DateFormatUtils.format(nextMonthStartTime, TIMEPATTERN));
                startDate = nextMonthStartTime;
            }else {
                list.add(DateFormatUtils.format(endDate, TIMEPATTERN));
                break;
            }
        }
        return list;
    }

    /**
     * 得到下个月开始的时间
     * @param nowDate 当前时间
     * @return
     */
    public static Date getNextMonthStartTime(Date nowDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);

        //下个月
        cal.add(Calendar.MONTH, 1);
        // 设为当前月的1号
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        return cal.getTime();
    }

    /**
     * 通过字符串得到当前时间
     * @param dateString 时间字符串 ；格式：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDateByDateString(String dateString){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATEPATTERN);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 通过字符串得到Date对象
     * @param dateString 时间字符串
     * @param pattern 时间格式
     * @return
     */
    public static Date getDateByDateString(String dateString, String pattern){
        Date date = null;
        try {
            date = DateUtils.parseDate(dateString, new String[]{pattern});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将字符串时间转成指定格式的时间字符串
     * @param dateString 时间字符串（yyyy-MM-dd HH:mm:ss）
     * @param outPattern 输出时间格式
     * @return
     */
    public static String getYearMonthStr(String dateString, String outPattern){
        String dateStr = null;
        try {
            Date date = DateUtils.parseDate(dateString, new String[]{DATEPATTERN});
            dateStr = DateFormatUtils.format(date, outPattern);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 时间段类
     *
     * 用于表示时间段的开始和结束时间
     */
    public static class TimeQuantum{
        private String startTime;
        private String endTime;

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    public static class Time{
        private static String currentDate;

        public static String getCurrentDate() {
            return currentDate;
        }

        public static void setCurrentDate(String currentDate) {
            Time.currentDate = currentDate;
        }
    }
}
