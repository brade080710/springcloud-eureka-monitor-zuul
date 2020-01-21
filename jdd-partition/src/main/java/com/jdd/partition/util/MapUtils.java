package com.jdd.partition.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class MapUtils {
	private static String longitudeMatch = "[\\-+]?(0?\\d{1,2}|0?\\d{1,2}\\.\\d{1,15}|1[0-7]?\\d|1[0-7]?\\d\\.\\d{1,15}|180|180\\.0{1,15})";
	private static String dimensionMatch = "[\\-+]?([0-8]?\\d|[0-8]?\\d\\.\\d{1,15}|90|90\\.0{1,15})";
	 
	
	    /**
	     * @Title: checkLongitude
	     * @Description: TODO(经度校验)
	     * @param @param longitude
	     * @param @return 参数
	     * @return boolean 返回类型
	     * @throws
	     */
	    
	public static boolean checkLongitude(String longitude) {
		if (StringUtils.isBlank(longitude)) {
			return false;
		}
		if (Pattern.matches(longitudeMatch, longitude)) {
			return true;
		}
		return false;
	}
	
	    /**
	     * @Title: checkDimension
	     * @Description: TODO(维度校验)
	     * @param @param dimension
	     * @param @return 参数
	     * @return boolean 返回类型
	     * @throws
	     */
	    
	public static boolean checkDimension(String dimension) {
		if (StringUtils.isBlank(dimension)) {
			return false;
		}
		if (Pattern.matches(dimensionMatch, dimension)) {
			return true;
		}
		return false;
	}
}
