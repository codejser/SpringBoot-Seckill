package com.meteor.seckill.Controller;

import com.meteor.seckill.domain.Goods;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.redis.RedisService;
import com.meteor.seckill.service.GoodsService;
import com.meteor.seckill.service.UserService;
import com.meteor.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {

	@Autowired
	UserService userService;
	
	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/to_list")
    public String list(Model model,User user) {
    	model.addAttribute("user", user);

		List<GoodsVo> goodsVoList = goodsService.goodsVoList();
		model.addAttribute("goodsList",goodsVoList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
	public String detail(Model model, User user, @PathVariable("goodsId") long goodsId){
    	model.addAttribute("user",user);
		//根据请求的id来查询商品的详情
		GoodsVo goods = goodsService.getGoodsById(goodsId);

		model.addAttribute("goods",goods);

		//商品秒杀开始的时间
		long start = goods.getStartTime().getTime();
		//商品秒杀结束的时间
		long end = goods.getEndTime().getTime();
		//系统现在的时间
		long now = System.currentTimeMillis();
		//秒杀的状态(0:秒杀未开始；1：秒杀进行中；2：秒杀已结束)
		int seckillStatus;
		//距离秒杀开始的时间
		int remainTime;

		if (start > now){
			seckillStatus = 0;
			remainTime = (int)(start-now)/1000;
		}else if (end < now){
			seckillStatus = 2;
			remainTime = -1;
		}else {
			seckillStatus = 1;
			remainTime = 0;
		}

		model.addAttribute("seckillStatus",seckillStatus);
		model.addAttribute("remainTime",remainTime);

		return "goods_detail";
	}
    
}
