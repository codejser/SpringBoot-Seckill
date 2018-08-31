package com.meteor.seckill.service;

import com.meteor.seckill.dao.OrderInfoDao;
import com.meteor.seckill.dao.SeckillOrderDao;
import com.meteor.seckill.domain.OrderInfo;
import com.meteor.seckill.domain.SeckillOrder;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 查找相应的订单
     * @param useId
     * @param goodsId
     * @return
     */
    public SeckillOrder getSeckillOrder(long useId,long goodsId){
        return seckillOrderDao.getSeckillOrder(useId,goodsId);
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
        long orderId = orderInfoDao.insert(orderInfo);
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(orderId);
        seckillOrderDao.insertSeckillOrder(seckillOrder);
        return orderInfo;
    }
}
