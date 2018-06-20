/**
 * Copyright (C) 1998-2014 DATATANG Inc.All Rights Reserved.		
 * 																	
 * FileName：StringUtil.java						
 *			
 * Description：  字符串工具类
 * 																	
 * History：
 * 版本号   作者     日期                相关操作
 *  1.0   nancr 2014-10-22  modify		
 */
package com.shujutang.highway.common.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 描述 字符串工具类
 * @author nancr
 * @version 1.0
 * @see
 */
public class StringUtil {
	private static long seq = 11110;

	/**
	 * 得到错误信息
	 * @return 错误信息字符串
	 */
	public static String getErrorMsg(String cause){
		return "错误原因: 【" + cause + "】";
	}
	
	/**
	 * 得到错误信息
	 * @param className 类名称
	 * @param methodName 方法名称
	 * @return 错误信息字符串
	 */
	public static String getErrorMsg(String className, String methodName){
		return "GPS公共模块 【"+ className +"】【"+ methodName +"】 错误原因: ";
	}
	
	/**
	 * 生成18位字符串主键
	 * @return
	 */
	public synchronized static String getSequences() {
		long millis = System.currentTimeMillis();
		String sequences = millis + "" + (++StringUtil.seq);
		if (StringUtil.seq >= 99999) {
			StringUtil.seq = 11110;
		}
		return sequences;
	}
	
	/**
	 * 字符窜为null或“”时返回true，   否则返回false
	 * @param str
	 * @return
	 */
//	public static boolean isEmpty(String str){
//		boolean res = false;
//		if(str == null || str.equals("")){
//			res = true;
//		}
//		return res;
//	}
	
	public static boolean isEmpty(Object str){
		boolean res = false;
		if(str == null || String.valueOf(str).equals("")){
			res = true;
//			log.info("字符串【" + str + "】是否为空？ 【" + res + "】");
		}
		return res;
	}
	
	/**
	 * 
	  * 描述 判断String[]是否为空
	  * @param param 字符串数组
	  * @return boolean  true:空 false：不为空
	  * @see
	 */
	public static boolean isEmptyArray(String[] param){
		boolean res = false;
		if(param == null || ("").equals(param[0].trim())){
			res = true;
		}
		return res;
	}
	
	public static String listToString(List<String> list, String separate)
    {
        StringBuilder str=new StringBuilder();
        for(int i=0;i<list.size();i++)
        {
        	//当循环到最后一个的时候 就不添加逗号
            if(i==list.size()-1)
            {
                str.append(list.get(i));
            }
            else {
                str.append(list.get(i));
                str.append(separate);
            }
        }
        return str.toString();
    }
	
	public static String listToString(List<String> list)
    {
        return listToString(list, ",");
    }

	/**
	 * 去掉所有空格、回车操作、tab
	 * @param str
	 * @return
	 */
	public static String deleteSpaces(String str){
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\\\s*|\\t|\\r|\\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
}
