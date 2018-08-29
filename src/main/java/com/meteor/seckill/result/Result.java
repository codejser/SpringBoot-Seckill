package com.meteor.seckill.result;

/**
 * @Author: meteor @Date: 2018/8/29 14:15
 * 封装返回结果的Result类
 */
public class Result<T> {
    //信息代码
    private int code;
    //具体的信息
    private String msg;
    //返回的对象
    private T data;

    //成功返回数据时使用的构造器
    public Result(T data){
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    //失败时使用的构造器
    public Result(CodeMsg cm){
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    //通过泛型方法来封装不同的调用方法：失败或者成功
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
