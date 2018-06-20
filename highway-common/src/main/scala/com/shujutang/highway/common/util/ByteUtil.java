package com.shujutang.highway.common.util;

import org.apache.commons.lang.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by nancr on 2015/6/25.
 *
 * byte数组和其他类型之间的转换
 */
public class ByteUtil {
    /**
     * 整型转换为4位字节数组
     * @param intValue int整型值
     * @return
     */
    public static byte[] int2ByteArray(int intValue) {
        byte[] byteArr = ByteBuffer.allocate(4).putInt(intValue).array();
        ArrayUtils.reverse(byteArr);
        return byteArr;
    }

    /**
     * 短整型转成字节数组
     * @param shortVal short值
     * @return
     */
    public static byte[] short2ByteArray(Short shortVal){
        byte[] shortArr = ByteBuffer.allocate(2).putShort(shortVal).array();
        ArrayUtils.reverse(shortArr);
        return shortArr;
    }

    /**
     * 将字符串转成字节数组
     * @param str 字符串
     * @param arrLenth 数组长度
     * @return
     */
    public static byte[] string2ByteArray(String str, int arrLenth){
        byte[] strArr = ByteBuffer.allocate(arrLenth).put(str.getBytes()).array();
//        ArrayUtils.reverse(strArr);
        return strArr;
    }

    /**
     * 将字节转成 字节数组
     * @param byt 字节
     * @return
     */
    public static byte[] byte2ByteArray(byte byt){
        byte[] bytes = ByteBuffer.allocate(1).put(byt).array();
        return bytes;
    }

    /**
     * 将字节数组转成int
     * @param bytes 字节数组长度为4
     * @return
     */
    public static int byteArray2Int(byte[] bytes){
        int integer = ByteBuffer.wrap(bytes).getInt();
        return integer;
    }

    /**
     * 将字节数组转成字符串
     * @param bytes 长度大于等于1
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String byteArray2String(byte[] bytes) throws UnsupportedEncodingException {
       return new String(bytes, "UTF-8");
    }

    /**
     * 将字节数组转成字节
     * @param bytes 长度为一得字节数组
     * @return
     */
    public static byte byteArray2Byte(byte[] bytes){
        byte byt = ByteBuffer.wrap(bytes).get();
        return byt;
    }

    /**
     * 将所有字节数组放到一个大的字节数组中
     * @param length 返回字节数组长度
     * @param bytesList 所有子字节数组
     * @return
     */
    public static byte[] addAllByteArray(int length, List<byte[]> bytesList){
        int copyLen = 0;
        //数组集合
        byte[] allArr = new byte[length];
        for (byte[] bytes : bytesList){
            System.arraycopy(bytes, 0, allArr, copyLen, bytes.length);
            copyLen = copyLen + bytes.length;
        }
        return allArr;
    }

    /**
     * 将子字节数据添加到父字节数组
     * @param parentByteArr 父字节数组
     * @param startIndex 添加时父字节数组的起始位置
     * @param subByteArr 子字节数组
     */
    public static void addSubByteArrToParentArr(byte[] parentByteArr,int startIndex, byte[] subByteArr){
        System.arraycopy(subByteArr, 0, parentByteArr, startIndex, subByteArr.length);
    }
    /**
     * 将字节数组转成二进制字符串
     * @param byteBuffer
     * @return
     */
    public static String toBinaryString(byte[] byteBuffer){
        String ZERO="00000000";
        StringBuilder out = new StringBuilder();
        for (int i = 0;i< byteBuffer.length;i++) {
            String s = Integer.toBinaryString(byteBuffer[i]);
            if (s.length() > 8) {
                s = s.substring(s.length() - 8);
            } else if (s.length() < 8) {
                s = ZERO.substring(s.length()) + s;
            }
            out.append(s).append(" ");
        }
        String result = out.toString().trim();
        return result;
    }

}