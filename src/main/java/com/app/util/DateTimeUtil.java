package com.app.util;

import io.netty.util.internal.StringUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {


    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间戳精确到秒
     * @return
     */
    public static String getNowTimeSecond(){
        long date = new Date().getTime();
        return String.valueOf(turnMilliSecondToSecond(date));
    }

    /**
     * 精确到毫秒
     * @return
     */
    public static String getMilliSecond(){
        long date = new Date().getTime();
        return String.valueOf(date);
    }

    /**
     * 字符串转化为时间对象 默认模式
     * @param str
     * @return
     */
    public static Date strToDate(String str){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STANDARD_FORMAT);
        try {
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转化为时间对象 自定义模式
     * @param str
     * @param format
     * @return
     */
    public static Date strToDate(String str,String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间转化为字符串 默认模式
     * @param date
     * @return
     */
    public static String dateToStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STANDARD_FORMAT);
        return simpleDateFormat.format(date);
    }

    /**
     * 时间转化为字符串 自定义模式
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date,String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 将字符串日期转化成时间戳
     * @param str
     * @return
     */
    public static String turnDateStrToTime(String str){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(STANDARD_FORMAT);
        try {
            Date date = simpleDateFormat.parse(str);
            return String.valueOf(turnMilliSecondToSecond(date.getTime()));
        }catch (Exception e){
            return StringUtil.EMPTY_STRING;
        }
    }


    /**
     * 将毫秒转化为秒
     * @param milliSecond
     * @return
     */
    public static int turnMilliSecondToSecond(long milliSecond){
        long div = 1000L;
        long rel = milliSecond/div;
        return (int) rel;
    }

    /**
     * 将秒转化为毫秒
     * @param second
     * @return
     */
    public static long turnSecondToMilliSecond(int second){
        String secondStr = String.valueOf(second);
        secondStr += "000";
        return Long.valueOf(secondStr);
    }
}
