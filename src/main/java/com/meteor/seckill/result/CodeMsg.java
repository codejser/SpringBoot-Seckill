package com.meteor.seckill.result;

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

    //其他异常

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
}
