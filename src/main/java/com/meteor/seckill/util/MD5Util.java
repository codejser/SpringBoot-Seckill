package com.meteor.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author: meteor @Date: 2018/8/30 13:13
 * 加密工具类:两次加密
 */
public class MD5Util {

    //基础加密md5,使用第三方的工具包
    private static String md5(String pass){
        return DigestUtils.md5Hex(pass);
    }

    //第一次加密用到的盐值
    private static final String salt = "4t5u6i9f";

    //第一次加密:用的盐值是固定值
    public static String md5First(String pass){
        String saltPass = pass + salt.charAt(1)
                +salt.charAt(2)+salt.charAt(4)+salt.charAt(5);
        System.out.println(saltPass);
        return md5(saltPass);
    }

    //第二次加密：用的盐值是随机的
    public static String md5Second(String pass,String dbSalt){
        String saltPass = pass + dbSalt.charAt(1)
                +dbSalt.charAt(2)+dbSalt.charAt(4)+dbSalt.charAt(5);
        return md5(saltPass);
    }

    //从初始密码到最终的密码，封装一下
    public static String md5Third(String pass,String dbSalt){
        String pass1 = md5First(pass);
        String pass2 = md5Second(pass1,dbSalt);
        return pass2;
        //return md5Second(md5First(pass),dbSalt);
    }

    //测试
    public static void main(String[] args) {
        System.out.println("第一次加密：" + md5First("123456"));
        //fc33e9cf047493c6402a1791c0732b67
        //System.out.println("第二次加密：" + md5Second("fc33e9cf047493c6402a1791c0732b67","1q2w3e"));
        //a71da427755cea1be04a21d8d573747a
        //System.out.println("第三次加密：" + md5Third("123456","1q2w3e"));
        //a71da427755cea1be04a21d8d573747a
    }
}
