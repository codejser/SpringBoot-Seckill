package com.meteor.seckill.vo;

import com.meteor.seckill.domain.Goods;

import java.util.Date;

/**
 * @Author: meteor @Date: 2018/8/31 09:20
 * 秒杀商品的视图对象:商品的属性加上部分秒杀商品的属性
 */
public class GoodsVo extends Goods{
    private Double seckillPrice;
    private Integer stockCount;
    private Date startTime;
    private Date endTime;

    public Double getSeckillPrice() {
        return seckillPrice;
    }

    public void setSeckillPrice(Double seckillPrice) {
        this.seckillPrice = seckillPrice;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
