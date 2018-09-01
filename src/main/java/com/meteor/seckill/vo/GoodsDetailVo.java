package com.meteor.seckill.vo;

import com.meteor.seckill.domain.User;

/**
 * @Author: meteor @Date: 2018/9/1 09:08
 * 页面静态化所需的视图模型，把相应的数据传到前端展示
 */
public class GoodsDetailVo {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods ;
    private User user;

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }

    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }

    public int getRemainSeconds() {
        return remainSeconds;
    }

    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
