/**
 * Copyright (C) 1998-2014 DATATANG Inc.All Rights Reserved.
 *
 * FileName：CSVUtil.java
 *
 * Description：  时间帮助类
 *
 * History：
 * 版本号   作者     日期                相关操作
 *  1.0   nancr 2014-10-22  modify
 */
package com.shujutang.highway.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 描述 时间帮助类
 * @author nancr
 * @version 1.0
 * @see
 */
public class DateUtil {

	/**
	 * 时间毫秒数格式化
	 *
	 * @param millis
	 * @return
	 */
	public static String getStringByMillis(long millis) {
		Date date = new Date(millis);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getStringByDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss格式转换成date类型
	 * @param dateString 字符串时间
	 * @return
	 */
	public static Date getDateByStringTime(String dateString){
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("字符串转换成时间date异常！", e);
		}
		return date;
	}

	public static Date getDateByStringDate(String dateString){
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("字符串转换成时间date异常！", e);
		}
		return date;
	}

	/**
	 * 将字符串转换成java.sql.Timestamp类型
	 */
	public static Timestamp getTimestampByString(String time){
		Date date = getDateByString(time);
		return new Timestamp(date.getTime());
	}

	/**
	 * 时间date转成long
	 *
	 *@pram Date
	 *@return long
	 * */
	public static long date2Long(Date date){
		if(date == null){
			return 0;
		}
		System.out.println(date.getTime());
		return date.getTime();
	}

	/**
	 * 使用时间字符串得到date
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateByStr(String strDate) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.parse(strDate);
	}


	/**
	 * 返回毫秒数
	 *
	 * @param strDate
	 * @return
	 */
	public static long getMillis(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (strDate == null || strDate.trim().length() == 0) {
			return 0;
		}
		Date date = null;
		try {
			date = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	/**
	 * 得到一个小时前的时间
	 * @return
	 */
	public static String getPreHourDate(){
		long nowTime = new Date().getTime();
		long oneHour = 60 * 60 * 1000;

		long preHourTime = nowTime - oneHour;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(preHourTime);
//		System.out.println(getStringByMillis(preHourTime));
		int  year =cal.get(Calendar.YEAR);
		int  month = cal.get(Calendar.MONTH)+1;
		String monthStr;
		if (String.valueOf(month).length() == 1) {
			monthStr = "0" + month;
		}else{
			monthStr = String.valueOf(month);
		}

		int  day = cal.get(Calendar.DAY_OF_MONTH);
		String dayStr;
		if (String.valueOf(day).length() == 1) {
			dayStr = "0" + day;
		}else{
			dayStr = String.valueOf(day);
		}

		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String hourStr;
		if (String.valueOf(hour).length() == 1) {
			hourStr = "0" + hour;
		}else{
			hourStr = String.valueOf(hour);
		}
		String time = year + "-" + monthStr + "-" + dayStr + "-" + hourStr;
		return time;
	}

	/**
	 *
	 * getPreHourDate(numOfHour个显示前的时间)
	 * @param numOfHour
	 * @return
	 * @return String
	 */
	public static String getNHourAgoDate(int numOfHour){
		long nowTime = new Date().getTime();
		long NHours = 60 * 60 * 1000 * numOfHour;

		long preHourTime = nowTime - NHours;
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(preHourTime);
//		System.out.println(getStringByMillis(preHourTime));
		int  year =cal.get(Calendar.YEAR);
		int  month = cal.get(Calendar.MONTH)+1;
		String monthStr;
		if (String.valueOf(month).length() == 1) {
			monthStr = "0" + month;
		}else{
			monthStr = String.valueOf(month);
		}

		int  day = cal.get(Calendar.DAY_OF_MONTH);
		String dayStr;
		if (String.valueOf(day).length() == 1) {
			dayStr = "0" + day;
		}else{
			dayStr = String.valueOf(day);
		}

		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String hourStr;
		if (String.valueOf(hour).length() == 1) {
			hourStr = "0" + hour;
		}else{
			hourStr = String.valueOf(hour);
		}
		String time = year + "-" + monthStr + "-" + dayStr + "-" + hourStr;
		return time;
	}

	/**
	 * 得到下个月的年月(时间字符串为年月)
	 * @param currentTime 时间字符串（yyyy-MM）
	 * @return
	 * @throws ParseException
	 */
	public static String getNextMonth(String currentTime) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sdate = sdf.parse(currentTime + "-01 00:00:00");
		int lastDayOfMonth = DateUtil.getLastDayOfMonth(sdate.getTime());
		long oneDayMillis = 24 * 60 * 60 * 1000;
		//下个月开始时间的毫秒值
		long nextMonthMillis = sdate.getTime() + (lastDayOfMonth) * oneDayMillis + 1;

		String nextMonStr = sdf.format(new Date(nextMonthMillis));
		nextMonStr = nextMonStr.substring(0,7);
		return nextMonStr;
	}



	/**
	 *取得当天的初始时间String
	 * @return  当前时间 <font color="red" >前一天</font>字符串  格式为 yyyy-DD-mm  如 2010-12-12
	 */

	public static String getYesterday(){
		long time=System.currentTimeMillis();
		String s = getStringByMillis(time);
		Calendar cal= getCalendar(s);
		String  year=Integer.toString(cal.get(Calendar.YEAR)) ;
		String  mouth=Integer.toString(cal.get(Calendar.MONTH)+1) ;
		String  day=Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) ;
		String today=year+"-"+mouth+"-"+day+" 00:00:00";
		return today;
	}


	/**
	 *取得当天的结束时间String
	 * @return  当前时间字符串  格式为 yyyy-DD-mm  如 2010-12-12
	 */

	public static String getTodayYMD(){
		long time=System.currentTimeMillis();
		String s = getStringByMillis(time);
		Calendar cal= getCalendar(s);
		String  year=Integer.toString(cal.get(Calendar.YEAR)) ;
		String  mouth=Integer.toString(cal.get(Calendar.MONTH)+1) ;
		String  day=Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) ;
		String today=year+"-"+mouth+"-"+day+" 23:59:59";
		return today;
	}

	/**
	 *  得到今天的某个时间
	 * @param hour 小时
	 */
	public static String getTodayTimeStr(String hour, String minute, String second){
		Calendar cal = Calendar.getInstance();
		String  year = Integer.toString(cal.get(Calendar.YEAR)) ;
		String  month = Integer.toString(cal.get(Calendar.MONTH)+1) ;
		String  day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) ;
		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}

	/**
	 * 得到今天的年月日
	 * @return
	 */
	public static String getTodayTimeStr(){
		Calendar cal = Calendar.getInstance();
		String  year = Integer.toString(cal.get(Calendar.YEAR)) ;
		String  month = Integer.toString(cal.get(Calendar.MONTH)+1) ;
		String  day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		if(month.length() == 1){
			month = "0" + month;
		}
		if(day.length() == 1){
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}

	/**
	 * 得到当前时间是那一天
	 * @return
	 */
	public static String getTodayDayStr(){
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 8, 01);
		String  day = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		return day;
	}

	/**
	 * 得到当前年月
	 * @return
	 */
	public static String getCurrentMonth(){
		Calendar cal = Calendar.getInstance();
		String  year = Integer.toString(cal.get(Calendar.YEAR)) ;
		String  month = Integer.toString(cal.get(Calendar.MONTH)+1) ;
		if(month.length() == 1){
			month = "0" + month;
		}

		return year + "-" + month;
	}

	/**
	 * 得到当前月的最后一天
	 * @return
	 */
	public static String getLastDay_CurrentMonth(){
		Calendar cal = Calendar.getInstance();
		String  year = Integer.toString(cal.get(Calendar.YEAR)) ;
		String  month = Integer.toString(cal.get(Calendar.MONTH)+1) ;
		String lastDay = Integer.toString(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		if(month.length() == 1){
			month = "0" + month;
		}

		return year + "-" + month + "-" + lastDay;
	}

	/**
	 * 得到下个月的年月(不建议使用次方法，因为如果是12月份，下个月不会得到下一年的1月)
	 * @return
	 */
	public static String getNextMonth4CurrentYear(){
		Calendar cal = Calendar.getInstance();
		String  year = Integer.toString(cal.get(Calendar.YEAR)) ;
		cal.roll(Calendar.MONTH, true);
		String nextMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
		if(nextMonth.length() == 1){
			nextMonth = "0" + nextMonth;
		}

		return year + "-" + nextMonth;
	}

	/**
	 * 得到时间点  月份的最后一天
	 * @param millis
	 * @return
	 */
	public static int getLastDayOfMonth(long millis){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDay;
	}

	/**
	 * 得到今天是多少号
	 * @return
	 */
	public static int getToday(){
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	/**
	 * 得到当前月最后一天是多少号
	 * @return
	 */
	public static int getLastDay(){
		Calendar cal = Calendar.getInstance();
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDay;
	}

	/**
	 * 得到     [当前周]   周一   的年月日
	 * @return
	 */
	public static String getWeekFirstDay(){
		Calendar ca = Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.MONDAY);
		ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek()); // Monday

		String  year=Integer.toString(ca.get(Calendar.YEAR)) ;
		String  month=Integer.toString(ca.get(Calendar.MONTH)+1) ;
		String  day=Integer.toString(ca.get(Calendar.DAY_OF_MONTH)) ;
		if(month.length() == 1){
			month = "0" + month;
		}
		if(day.length() == 1){
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}

	/**
	 * 得到     [当前周]   周五   的年月日
	 * @param dayOfWeek 周几
	 * @param hour 小时
	 * @param minute 分钟
	 * @param second 秒
	 * @return 时间字符串
	 */
	public static String getWeekFriday(int dayOfWeek, String hour, String minute, String second){

		Calendar ca = Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.MONDAY);
		ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + dayOfWeek - 1); // 周五

		String  year = Integer.toString(ca.get(Calendar.YEAR)) ;
		String  month = Integer.toString(ca.get(Calendar.MONTH)+1) ;
		String  day = Integer.toString(ca.get(Calendar.DAY_OF_MONTH)) ;
		if(month.length() == 1){
			month = "0" + month;
		}
		if(day.length() == 1){
			day = "0" + day;
		}
		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}

	/**
	 * 得到  [当前周] 周天   的年月日
	 * @return
	 */
	public static String getWeekLastDay(){
		Calendar ca = Calendar.getInstance();
		ca.setFirstDayOfWeek(Calendar.MONDAY);
		ca.set(Calendar.DAY_OF_WEEK, ca.getFirstDayOfWeek() + 6); // 周天

		String  year=Integer.toString(ca.get(Calendar.YEAR)) ;
		String  month=Integer.toString(ca.get(Calendar.MONTH)+1) ;
		String  day=Integer.toString(ca.get(Calendar.DAY_OF_MONTH)) ;
		if(month.length() == 1){
			month = "0" + month;
		}
		if(day.length() == 1){
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}


	/**
	 * 根据标准时间字符串格式返回Date对象
	 * @param str
	 * @return
	 */
	public static Calendar getCalendar(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return 	calendar;
	}


	/**
	 * 将以秒为单位的时间数据转换成为字符串格式的数据
	 * @param millis
	 * @return
	 */
	public static String  millisToDate(long millis){
		Date date = new Date(millis*1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	//昨天的开始时间毫秒数
	public static long getYestodayBeginMilis(){
		long yestdayBeginDateInLong = 0;
		yestdayBeginDateInLong = getTodayBeginDateInLong() - 24*60*60*1000;
		return yestdayBeginDateInLong;
	}

	//昨天的结束时间毫秒数
	public static long getYestodayEndMilis(){
		long yestodayEndDateInLong = 0;
		yestodayEndDateInLong = getTodayBeginDateInLong() - 1*1000;
		return yestodayEndDateInLong;
	}

	//得到1小时前的毫秒数
	public static long getOneHourAgoDateInLong(){
		return System.currentTimeMillis() - 60*60*1000;
	}

	//今天的开始时间毫秒数
	public static long getTodayBeginDateInLong(){
		String todayBeginDateInString = getTodayBeginDateInString();
		Date todayBeginDate = null;
		try {
			todayBeginDate = getDateByString(todayBeginDateInString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long todayBeginDateInLong = getDateInLongByDate(todayBeginDate);
		return todayBeginDateInLong;
	}

	public static long getDateInLongByDate(Date date){
		long dateInLong = 0;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		dateInLong = cal.getTimeInMillis();
		return dateInLong;
	}

	public static Date getDateByString(String dateString){
		Date date = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("字符串转换成时间date异常！", e);
		}
		return date;
	}

	public static String getTodayBeginDateInString(){
		Calendar todayCalendar = Calendar.getInstance();
		String year = Integer.toString(todayCalendar.get(Calendar.YEAR));
		String month = Integer.toString(todayCalendar.get(Calendar.MONTH)+1);
		String day = Integer.toString(todayCalendar.get(Calendar.DAY_OF_MONTH));
		String yestodyBeginDateInString = year + "-" + month + "-" + day + " " + "00:00:00";
		return yestodyBeginDateInString;
	}

	public static void main(String[] args) {
		System.out.println(getPreHourDate());
	}
}
