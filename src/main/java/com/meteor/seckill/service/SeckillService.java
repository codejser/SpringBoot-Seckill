package com.meteor.seckill.service;

import com.meteor.seckill.domain.OrderInfo;
import com.meteor.seckill.domain.SeckillOrder;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.redis.RedisService;
import com.meteor.seckill.redis.SeckillKey;
import com.meteor.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: meteor @Date: 2018/8/31 15:05
 * 整合秒杀的两大业务逻辑
 */
@Service
public class SeckillService {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo) {
        //执行减库存
        int result = goodsService.reduceStock(goodsVo.getId());
        if (result == 1){
            //插入订单数据
            return orderService.createOrder(user,goodsVo);
        }else {
            //秒杀失败就将该商品的id作为key放入缓存中
            setSeckillKey(goodsVo.getId());
            return null;
        }

    }

    private void setSeckillKey(long goodsId) {
        redisService.set(SeckillKey.isExistsGoodsKey,""+goodsId,true);
    }
    //判断是否存在秒杀失败的key
    private boolean isSeckillKey(long goodsId) {
        return redisService.exists(SeckillKey.isExistsGoodsKey,""+goodsId);
    }


    /**
     * 获取秒杀的情况：orderId:秒杀成功 -1：秒杀失败 0：排队中
     * @param userId
     * @param goodsId
     * @return
     */
    public long getSeckillResult(long userId,long goodsId){
        //查询秒杀订单
        SeckillOrder order = orderService.getSeckillOrder(userId,goodsId);
        if (order != null){
            return order.getOrderId();
        }else {
            boolean flag = isSeckillKey(goodsId);
            if (flag){
                return -1;
            }else {
                return 0;
            }
        }
    }

}