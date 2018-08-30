package com.meteor.seckill.util;

import java.util.UUID;

/**
 * @Author: meteor @Date: 2018/8/30 21:51
 * 随机token工具类
 */
public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
