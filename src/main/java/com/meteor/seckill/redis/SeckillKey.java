package com.meteor.seckill.redis;

/**
 * @Author: meteor @Date: 2018/9/1 21:35
 */
public class SeckillKey extends BasePrefix{

    private SeckillKey(String prefix) {
        super(prefix);
    }

    //作为秒杀失败的标记
    public static SeckillKey isExistsGoodsKey = new SeckillKey("isgk");
}
