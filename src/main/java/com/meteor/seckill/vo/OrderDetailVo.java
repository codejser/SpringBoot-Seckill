package com.meteor.seckill.vo;

import com.meteor.seckill.domain.Goods;
import com.meteor.seckill.domain.OrderInfo;

/**
 * @Author: meteor @Date: 2018/9/1 10:07
 * 订单详情页所需要的视图模型
 *
 */
public class OrderDetailVo {

    private OrderInfo orderInfo;
    private Goods goods;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }
}
