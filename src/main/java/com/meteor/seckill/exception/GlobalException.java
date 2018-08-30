package com.meteor.seckill.exception;

import com.meteor.seckill.result.CodeMsg;

/**
 * @Author: meteor @Date: 2018/8/30 18:23
 * 异常工具类
 */
public class GlobalException extends RuntimeException{

    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
