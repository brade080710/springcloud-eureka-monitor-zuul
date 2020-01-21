package com.jdd.partition.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * @Title: string2Date
	 * @Description:
	 * @param dateString
	 * @param df
	 * @return 参数说明
	 * @return java.util.Date 返回类型
	 */
	public final static Date string2Date(String dateString, String df) {
		DateFormat dateFormat = new SimpleDateFormat(df, Locale.SIMPLIFIED_CHINESE);
		dateFormat.setLenient(false);
		Date date = null;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return date;
	}
	public final static String convertDateToString(Date date, String df) {
		DateFormat dateFormat = new SimpleDateFormat(df, Locale.SIMPLIFIED_CHINESE);
		dateFormat.setLenient(false);
		String str = null;
		str = dateFormat.format(date);
		return str;
	}

	/**
	 * 将日期字符串按指定格式转换成日期类型
	 * 
	 * @author dylan_xu
	 * @date Mar 11, 2012
	 * @param aMask
	 *            指定的日期格式，如:yyyy-MM-dd
	 * @param strDate
	 *            待转换的日期字符串
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		if (logger.isDebugEnabled()) {
			logger.debug("converting '" + strDate + "' to date with mask '" + aMask + "'");
		}
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			logger.error("ParseException: " + pe);
			throw pe;
		}
		return (date);
	}
}