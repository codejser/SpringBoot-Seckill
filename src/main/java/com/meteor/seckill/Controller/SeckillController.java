package com.meteor.seckill.Controller;

import com.meteor.seckill.domain.OrderInfo;
import com.meteor.seckill.domain.SeckillOrder;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.result.CodeMsg;
import com.meteor.seckill.result.Result;
import com.meteor.seckill.service.GoodsService;
import com.meteor.seckill.service.OrderService;
import com.meteor.seckill.service.SeckillService;
import com.meteor.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/seckill")
public class SeckillController {

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService seckillOrderService;

	@Autowired
	SeckillService seckillService;

	@RequestMapping("/do_seckill")
    public String seckill(Model model,User user,Long goodsId){
		//未登录，就跳转到登陆页面
    	if (user == null){
    		return "login";
		}
		//判断秒杀商品的库存是否足够
		GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
    	if (goodsVo.getStockCount() <= 0){
			model.addAttribute("errorMsg", CodeMsg.STOCK_EMPTY);
			return "seckill_error";
		}
		//判断是否重复秒杀(从当前的秒杀订单表中去查找有没有对应的信息)
		SeckillOrder seckillOrder = seckillOrderService.getSeckillOrder(user.getId(),goodsId);
		if (seckillOrder != null){
			model.addAttribute("errorMsg",CodeMsg.REPEANT_SECKILL);
			return "seckill_error";
		}

		//处理秒杀逻辑：减库存，插订单:两个表；一个是订单信息表，一个是秒杀订单表
		OrderInfo orderInfo = seckillService.seckill(user,goodsVo);
		model.addAttribute("orderInfo",orderInfo);
		model.addAttribute("goods",goodsVo);
		return "order_detail";
	}

	/**
	 * 秒杀优化所需的方法

	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/doSeckill")
	@ResponseBody
	public Result<OrderInfo> seckill2(User user, Long goodsId){
		//未登录，就跳转到登陆页面
		if (user == null){
			return Result.error(CodeMsg.NEED_LOGIN);
		}
		//判断秒杀商品的库存是否足够
		GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
		if (goodsVo.getStockCount() <= 0){
			return Result.error(CodeMsg.STOCK_EMPTY);
		}
		//判断是否重复秒杀(从当前的秒杀订单表中去查找有没有对应的信息)
		SeckillOrder seckillOrder = seckillOrderService.getSeckillOrder(user.getId(),goodsId);
		if (seckillOrder != null){
			return Result.error(CodeMsg.REPEANT_SECKILL);
		}

		//处理秒杀逻辑：减库存，插订单:两个表；一个是订单信息表，一个是秒杀订单表
		OrderInfo orderInfo = seckillService.seckill(user,goodsVo);
		return Result.success(orderInfo);
	}
}
