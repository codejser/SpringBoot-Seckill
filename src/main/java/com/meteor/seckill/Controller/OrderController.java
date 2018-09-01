package com.meteor.seckill.Controller;

import com.meteor.seckill.domain.Goods;
import com.meteor.seckill.domain.OrderInfo;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.result.CodeMsg;
import com.meteor.seckill.result.Result;
import com.meteor.seckill.service.GoodsService;
import com.meteor.seckill.service.OrderService;
import com.meteor.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: meteor @Date: 2018/9/1 10:05
 * 获取订单详情所需要的Controller
 */
@RequestMapping("/order")
public class OrderController {


    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail/{orderId}")
    @ResponseBody
    public Result<OrderDetailVo> detail(User user,long orderId){
        if (user == null){
            return Result.error(CodeMsg.NEED_LOGIN);
        }
        //先查订单
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null){
            return Result.error(CodeMsg.ORDER_ERROR);
        }
        //再查商品
        Goods goods = goodsService.getGoodsById(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrderInfo(orderInfo);
        orderDetailVo.setGoods(goods);
        return Result.success(orderDetailVo);
    }
}
