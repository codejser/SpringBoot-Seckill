package com.meteor.seckill.dao;

import com.meteor.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: meteor @Date: 2018/8/29 16:58
 */
@Component
@Mapper
public interface SeckillGoodsDao {

    //根据秒杀商品的id来查询秒杀商品的库存数量
    @Select("select stock_count from seckill_goods where goods_id = #{goodsId}")
    int getSeckillGoodsStockById(long goodsId);

    //减少秒杀商品的库存
    @Update("update seckill_goods set stock_count = stock_count -1 where goods_id = #{goodsId}")
    int reduceStock(long goodsId);

}
