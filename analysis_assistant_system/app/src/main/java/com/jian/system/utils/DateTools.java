package com.jian.system.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTools {
	
	private final static String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
	

	/**
	 * 格式化日期
	 * @return 返回当前日期，格式：{@link DateTools#DATE_FORMAT_STR}
	 */
	public static String formatDate(){
		return formatDate(DATE_FORMAT_STR);
	}
	
	/**
	 * 格式化日期
	 * @param str 日期格式
	 * @return 返回当前日期.
	 */
	public static String formatDate(String str){
		return formatDate(null, str);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @return 返回传入日期，格式：{@link DateTools#DATE_FORMAT_STR}
	 */
	public static String formatDate(Date date){
		return formatDate(date, DATE_FORMAT_STR);
	}
	
	/**
	 * 格式化日期
	 * @param date 日期
	 * @param str 返回日期格式，默认：{@link DateTools#DATE_FORMAT_STR}
	 * @return 返回传入日期，传入格式。
	 */
	public static String formatDate(Date date, String str){
		if(date == null){
			Calendar calendar = Calendar.getInstance();
			date = calendar.getTime();
		}
		str = Utils.isNullOrEmpty(str) ? DATE_FORMAT_STR : str;
		return new SimpleDateFormat(str).format(date);
	}
	
}
