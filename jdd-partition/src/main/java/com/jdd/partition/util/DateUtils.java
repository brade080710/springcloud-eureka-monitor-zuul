package com.jdd.partition.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {
	private static String defaultDatePattern = "yyyy-MM-dd";  
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
	public static String dateToString(Date date,String pattern) {
		return date == null ? " " : new SimpleDateFormat(pattern).format(date); 
	}
	public static Date stringToDate(String stringDate,String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern); 
		Date date  = sdf.parse(stringDate);
		return date;
	}
	public static String getNextNonth(String strDate) throws ParseException {
	    SimpleDateFormat sdfTemp = new SimpleDateFormat(defaultDatePattern);  
	    SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy-MM");  
	    Date dateTemp = new Date();
		dateTemp = sdfTemp.parse(strDate);
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(dateTemp);
		dateCal.set(Calendar.MONTH,dateCal.get(Calendar.MONTH)+1); //增加一月
		String dateNowStr = sdfMonth.format(dateCal.getTime());  
	    return dateNowStr+"-01";
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println(isDate("长期"));
	}
	
	public static boolean isDate(String date){
		if (org.springframework.util.StringUtils.isEmpty(date)) {
			return false;
		}
/*		if (date.equals("长期")) {
			date = "9999-12-30";
		}*/
		if (date.contains(".")) {
			date = date.replace(".", "-");
		}
		Pattern p = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
		return p.matcher(date).matches();
	}
}
