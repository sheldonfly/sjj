/**
 * 文件名：EncodeDecodeUtil.java
 *
 * 版本信息：
 * 日期：2015年3月23日
 * Copyright ru Corporation 2015 
 * 版权所有
 *
 */
package com.shujutang.highway.common.util;

import java.nio.charset.Charset;

/**
 * 
 * 项目名称：api-common 类描述： 创建人：nancr 创建时间：2015年3月23日 上午10:05:24 修改人：nancr
 * 修改时间：2015年3月23日 上午10:05:24 修改备注：
 * 
 * @since jdk1.7
 * @version 1.0
 */
public class EncodeDecodeUtil {

	private static final String key0 = "daiaptainside";
	private static final Charset charset = Charset.forName("UTF-8");
	private static byte[] keyBytes = key0.getBytes(charset);

	/**
	 * 
	 * encode(编码)
	 * @param enc
	 * @return
	 * @return String
	 */
	public static String encode(String enc) {
		byte[] b = enc.getBytes(charset);
		for (int i = 0, size = b.length; i < size; i++) {
			for (byte keyBytes0 : keyBytes) {
				b[i] = (byte) (b[i] ^ keyBytes0);
			}
		}
		return new String(b);
	}

	/**
	 * 
	 * decode(解码)
	 * @param dec
	 * @return
	 * @return String
	 */
	public static String decode(String dec) {
		byte[] e = dec.getBytes(charset);
		byte[] dee = e;
		for (int i = 0, size = e.length; i < size; i++) {
			for (byte keyBytes0 : keyBytes) {
				e[i] = (byte) (dee[i] ^ keyBytes0);
			}
		}
		return new String(e);
	}

	public static void main(String[] args) {
//		String s = "1528cdaa460f4dc9adfd8f770343d28a";
//		String enc = encode(s);
		String enc = "EAFL@BD@MLCCDG@GFL";
		
		String dec = decode(enc);
		System.out.println(enc);
		System.out.println(dec);
	}
}
