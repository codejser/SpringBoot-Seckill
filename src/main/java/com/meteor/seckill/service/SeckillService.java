package com.meteor.seckill.service;

import com.meteor.seckill.domain.OrderInfo;
import com.meteor.seckill.domain.User;
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

    @Transactional
    public OrderInfo seckill(User user, GoodsVo goodsVo) {
        //执行减库存
        goodsService.reduceStock(goodsVo.getId());

        //插入订单数据
        return orderService.createOrder(user,goodsVo);
    }

}