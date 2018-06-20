/*
 * Copyright (C) 2014 DATATANG Inc.All Rights Reserved.
 *
 * FileName： AuthenticationServiceImpl.java
 *
 * Description： UUID生成类
 *
 * History：
 * 版本号          作者                              日期                                  简要介绍相关操作
 * 1.0    suyuening    2014-11-18    Create
 *
 */
package com.shujutang.highway.common.util;

import java.util.UUID;

public class UUIDGenerator {

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String s = UUID.randomUUID().toString();
		// 去掉"-"符号
		return s.replace("-", "");
	}
}