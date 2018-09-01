package com.meteor.seckill.dao;

import com.meteor.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

/**
 * @Author: meteor @Date: 2018/8/31 14:25
 */
@Component
@Mapper
public interface OrderInfoDao {

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_time)values("
            + "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createTime} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    long insert(OrderInfo orderInfo);


    @Select("select * order_info from where order_id = #{orderId}")
    OrderInfo getOrderById(long orderId);

}
