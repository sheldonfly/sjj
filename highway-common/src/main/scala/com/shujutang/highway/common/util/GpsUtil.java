/** 
 * Copyright (C) 1998-2014 DATATANG Inc.All Rights Reserved.
 *
 * FileName: GpsUtil.java 
 *
 * Description: TODO
 *
 * History：
 * 版本号   作者      日期                           操作
 * 1.0    nancr  2014年11月4日 下午7:52:06  Create	
 */

package com.shujutang.highway.common.util;

import java.math.BigDecimal;

/**
 * 描述
 * 
 * @author nancr
 * @version 1.0
 * @see
 */

public class GpsUtil {

	// 计算两GPS间的球面距离
	private final static double EARTH_RADIUS = 6378137.0;

	public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		BigDecimal bd = new BigDecimal(s);
		double dres = bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		return dres;
	}

	public static double Distance(double lon1, double lat1, double lon2, double lat2) {
		double R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		double sa2 = Math.sin((lat1 - lat2) / 2.0);
		double sb2 = Math.sin(((lon1 - lon2) * Math.PI / 180.0) / 2.0);
		return 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
	}

	public static void main(String[] args) {
		//115.123658,36.27072
		//114.896473,31.533016
		//
		double lat_a = 24.515456;
		double lng_a = 114.611441;
		double lat_b = 29.503884;
		double lng_b = 114.852258;
		System.out.println(gps2m(lat_a, lng_a, lat_b, lng_b));
	}

}
