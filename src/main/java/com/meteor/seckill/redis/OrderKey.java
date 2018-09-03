package com.meteor.seckill.redis;

/**
 * @Author: meteor @Date: 2018/9/1 21:35
 */
public class OrderKey extends BasePrefix{

    private OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey seckillOrderKey = new OrderKey("so");


}
