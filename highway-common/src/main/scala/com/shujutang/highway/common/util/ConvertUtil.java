package com.shujutang.highway.common.util;

/**
 * Created by nancr on 2015/6/25.
 * 进制转化 帮助类
 */
public class ConvertUtil {

    /**
     * 将16进制字符串转化成10进制数组
     * @param hexadecimalStr 16进制字符串
     * @return
     */
    public static int  sixteen2Decimal(String hexadecimalStr){
        int hexadecimal = Integer.valueOf(hexadecimalStr, 16);
        return hexadecimal;
    }
}
