package com.meteor.seckill.service;

import com.meteor.seckill.dao.OrderInfoDao;
import com.meteor.seckill.dao.SeckillOrderDao;
import com.meteor.seckill.domain.OrderInfo;
import com.meteor.seckill.domain.SeckillOrder;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.redis.OrderKey;
import com.meteor.seckill.redis.RedisService;
import com.meteor.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: meteor @Date: 2018/8/31 14:15
 */
@Service
public class OrderService {

    @Autowired
    SeckillOrderDao seckillOrderDao;

    @Autowired
    OrderInfoDao orderInfoDao;

    @Autowired
    RedisService redisService;

    /**
     * 查找相应的订单
     * @param userId
     * @param goodsId
     * @return
     */
    public SeckillOrder getSeckillOrder(long userId,long goodsId){
        //return seckillOrderDao.getSeckillOrder(userId,goodsId);
        //查找订单需要查询数据库，可以优化成查redis缓存，把参数作为key的一部分，秒杀订单对象作为值；
        return redisService.get(OrderKey.seckillOrderKey,""+ userId+"_"+goodsId,SeckillOrder.class);

    }

    public OrderInfo getOrderById(long orderId){
        return orderInfoDao.getOrderById(orderId);
    }

    //秒杀逻辑之一:创建订单(两个表：seckill_order/order_info)
    @Transactional
    public OrderInfo createOrder(User user, GoodsVo goodsVo){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateTime(new Date());
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderInfoDao.insert(orderInfo);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrderDao.insertSeckillOrder(seckillOrder);

        //生成秒杀订单的同时，将seckillOrder存入缓存，便于下次直接查询缓存
        redisService.set(OrderKey.seckillOrderKey,""+ user.getId()+"_"+goodsVo.getId(),seckillOrder);

        return orderInfo;
    }
}
