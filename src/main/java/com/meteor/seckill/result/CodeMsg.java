package com.meteor.seckill.result;

import com.sun.org.apache.bcel.internal.classfile.Code;

/**
 * @Author: meteor @Date: 2018/8/29 14:19
 * 封装错误代码和对应错误信息的工具类
 */
public class CodeMsg {

    //成员变量
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务器异常");

    //登陆异常
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(501101,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(501102,"密码错误");
    public static CodeMsg NEED_LOGIN = new CodeMsg(501103,"未登录，请先登陆");

    //秒杀异常
    public static CodeMsg STOCK_EMPTY = new CodeMsg(502101,"库存不足");
    public static CodeMsg REPEANT_SECKILL = new CodeMsg(502102,"重复秒杀");

    //订单异常
    public static CodeMsg ORDER_ERROR = new CodeMsg(503101,"订单不存在");
    //构造器
    public CodeMsg(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    //成员变量的get方法

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
