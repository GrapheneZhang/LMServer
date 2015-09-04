package com.lm.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验方法
 * 
 * @author ZhangYaxu
 * @date 2015年9月3日
 */
public class Validate {
    
    /**
     * @Title: isStringWithComma 
     * @Description: id字符串校验
     * @return
     * @throws
     */
    public static boolean isStringWithComma(String ids){
        String regex = "^(\\d+[,])*(\\d+)$";//数字，如果有多个中间用逗号隔开
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ids);
        return matcher.matches();
    }
    
    /**
     * @Title: isMobilePhone 
     * @Description: 验证11位手机号码
     * @return
     * @throws
     */
    public static boolean isMobilePhone(String mobilePhone){
        String regex = "1{1}[0-9]{10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mobilePhone);
        return matcher.matches();
    }
    
    /**
     * @Title: isMobilePhone 
     * @Description: 验证11位手机号码
     * @return
     * @throws
     */
    public static String findMobilePhone(String errorMsg){
        String regex = "1{1}[0-9]{10}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(errorMsg);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
    
    /** 
     * 验证Email   
     * @param email
     */
    public static boolean isEmail(String email){  
        String regex = "[0-9a-zA-Z]+@[0-9a-zA-Z]+//.[0-9a-zA-Z]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    } 
}
