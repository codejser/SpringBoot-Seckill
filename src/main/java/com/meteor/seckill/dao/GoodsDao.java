package com.meteor.seckill.dao;

import com.meteor.seckill.domain.Goods;
import com.meteor.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: meteor @Date: 2018/8/29 16:58
 */
@Component
@Mapper
public interface GoodsDao {

    //用左联接去查询goods 和 seckillgoods 表，以seckillgoods为左表
    @Select("SELECT g.*,sg.seckill_price,sg.stock_count,sg.start_time,sg.end_time FROM seckill_goods sg LEFT JOIN goods g ON sg.goods_id = g.id")
    List<GoodsVo> getGoodsVoList();


    //根据商品的id去查询商品的详情
    @Select("SELECT g.*,sg.seckill_price,sg.stock_count,sg.start_time,sg.end_time FROM seckill_goods sg LEFT JOIN goods g ON sg.goods_id = g.id where g.id = #{goodsid}")
    GoodsVo getGoodsById(long goodsid);
}
