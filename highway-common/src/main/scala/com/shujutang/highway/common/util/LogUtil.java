/*
 * Copyright (C) 2014 DATATANG Inc.All Rights Reserved.
 *
 * FileName： LogUtil.java
 *
 * Description：日志方法类
 *
 * History：
 * 版本号          作者                              日期                                  简要介绍相关操作
 * 1.0    suyuening    2014-11-18    Create
 *
 */
package com.shujutang.highway.common.util;

public class LogUtil {
	/**
	 * 进入方法日志
	 * @param methodName
	 * @return
	 */
    public static final String enterMethod(String methodName) {
    	return String.format("%s start", methodName);
    }

    /**
     * 离开方法日志
     * @param methodName
     * @return
     */
    public static final String exitMethod(String methodName) {
    	return String.format("%s end", methodName);
    }
}
