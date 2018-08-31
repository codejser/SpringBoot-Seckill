package com.meteor.seckill.dao;

import com.meteor.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @Author: meteor @Date: 2018/8/29 16:58
 */
@Component
@Mapper
public interface SeckillOrderDao {

    //根据用户的id和商品的id来查询秒杀订单
    @Select("select * from seckill_order where user_id = #{userId} and goods_id = #{goodsId}")
    SeckillOrder getSeckillOrder(@Param("userId") long userId,@Param("goodsId") long goodsId);

    @Insert("insert into seckill_order(user_id,goods_id,order_id) values(#{userId},#{goodsId},#{orderId})")
    int insertSeckillOrder(SeckillOrder seckillOrder);
}
