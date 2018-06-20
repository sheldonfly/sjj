/** 
 * Copyright (C) 2010-2014 DATATANG Inc.All Rights Reserved.
 *
 * FileName: JsonUtil.java 
 *
 * Description: TODO
 *
 * History：
 * 版本号   作者      日期                           操作
 * 1.0    nancr  2014年11月18日 下午7:57:15  Create	
 */

package com.shujutang.highway.common.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * 描述 json帮助类
 * 
 * @author nancr
 * @version 1.0
 * @see
 */

public class JsonUtil {
	static Gson gson;
	static {
		gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	}

	/**
	 * 格式化json字符串
	 * @param josnStr json字符串
	 * @return
	 */
	public static String formatJson(String josnStr){
//		Gson gson2 = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(josnStr);
		String prettyJsonStr = gson.toJson(je);
		return prettyJsonStr;
	}

	/**
	 * 
	  * 描述 gson 将对象转换成json
	  * @param obj
	  * @return
	  * @see
	 */
	public static String obj2JsonByGson(Object obj) {
		return gson.toJson(obj);
	}
	
	/**
	 * 
	  * 描述 fastjson将对象转成json
	  * @param obj
	  * @return
	  * @see
	 */
	public static String obj2JsonByFast(Object obj){
		return JSON.toJSONString(obj);
	}
	

	/**
	 * 
	  * 描述 gson将json转成map
	  * @param data
	  * @return
	  * @see
	 */
	public static Map<String, String> json2MapByGson(String data) {
		Map<String, String> map = gson.fromJson(data, new TypeToken<Map<String, String>>() {
		}.getType());
		return map;
	}
	
	/**
	 * 
	  * 描述
	  * @param json
	  * @return
	  * @see
	 */
	public static Map<String, Object> json2MapByFast(String json) {
		Map<String, Object> map = JSON.parseObject(json, Map.class);
		return map;
	}
	
	public static <T> T json2ObjByFast(String json, Class<T> clazz) {
		Object obj = JSON.parseObject(json, clazz);
		return (T) obj;
	}
	
}
