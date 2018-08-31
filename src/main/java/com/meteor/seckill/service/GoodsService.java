package com.meteor.seckill.service;


import com.meteor.seckill.dao.GoodsDao;
import com.meteor.seckill.dao.SeckillGoodsDao;
import com.meteor.seckill.domain.Goods;
import com.meteor.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: meteor @Date: 2018/8/29 17:04
 */
@Service
public class GoodsService {

   @Autowired
   private GoodsDao goodsDao;

   @Autowired
   private SeckillGoodsDao seckillGoodsDao;

    /**
     * 查询商品的列表
     * @return
     */
   public List<GoodsVo> goodsVoList(){
       return goodsDao.getGoodsVoList();
   }

    /**
     * 根据id查找商品
     * @param goodsId
     * @return
     */
   public GoodsVo getGoodsById(long goodsId){
       return goodsDao.getGoodsById(goodsId);
   }

   public int reduceStock(long goodsId){
       return seckillGoodsDao.reduceStock(goodsId);
   }
}
