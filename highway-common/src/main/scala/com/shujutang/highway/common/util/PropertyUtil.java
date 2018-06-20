package com.shujutang.highway.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * properties文件帮助类
 *
 */
public class PropertyUtil {

	/**
	 * 根据类路径和key得到value
	 * 
	 * @param fileClassPath
	 *            文件相对类根目录路径
	 * @param key
	 *            key
	 * @return
	 */
	public static String getValBykey(String fileClassPath, String key) {
		String value = null;
		InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(fileClassPath);
		Properties pro = new Properties();
		try {
			pro.load(is);
			is.close();
			value = pro.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 
	 * getAllKV(获取所有的key value)
	 * @param filePath 文件路径
	 * @return Map<String,String>
	 */
	public static Map<String, String> getAllKV(String filePath) {
		Map<String, String> res = new HashMap<String, String>();
		InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(filePath);
		Properties pro = new Properties();
		try {
			pro.load(is);
			is.close();
			Set<Object> keys = pro.keySet();
			for (Object key : keys) {
				String value = pro.getProperty(String.valueOf(key));
				res.put(String.valueOf(key), value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static void main(String[] args) {
		String filePath = "datafile/picurl.conf";
		System.out.println(getAllKV(filePath));
	}
}
