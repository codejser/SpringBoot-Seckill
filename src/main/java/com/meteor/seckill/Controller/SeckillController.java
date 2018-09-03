package com.meteor.seckill.Controller;

import com.meteor.seckill.domain.OrderInfo;
import com.meteor.seckill.domain.SeckillOrder;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.rabbitmq.MQSender;
import com.meteor.seckill.rabbitmq.SeckillMessage;
import com.meteor.seckill.redis.GoodsKey;
import com.meteor.seckill.redis.RedisService;
import com.meteor.seckill.result.CodeMsg;
import com.meteor.seckill.result.Result;
import com.meteor.seckill.service.GoodsService;
import com.meteor.seckill.service.OrderService;
import com.meteor.seckill.service.SeckillService;
import com.meteor.seckill.vo.GoodsVo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean{

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService seckillOrderService;

	@Autowired
	SeckillService seckillService;

	@Autowired
    RedisService redisService;

	//消费者：发送消息的一方
	@Autowired
    MQSender sender;

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
	@RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
	@ResponseBody
	public Result<OrderInfo> seckill2(User user, @RequestParam("goodsId") long goodsId){
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

	/**
	 * 秒杀优化,集成RabbitMQ
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/doSeckillMQ",method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> seckill3(User user,@RequestParam("goodsId")long goodsId){
		//未登录，就跳转到登陆页面
		if (user == null){
			return Result.error(CodeMsg.NEED_LOGIN);
		}
		//预减库存逻辑
        long stock = redisService.decr(GoodsKey.goodsStock,goodsId+"");
		if (stock < 0){
            //库存不足，秒杀结束
            return Result.error(CodeMsg.STOCK_EMPTY);
        }
        //判断是否重复秒杀
        SeckillOrder seckillOrder = seckillOrderService.getSeckillOrder(user.getId(),goodsId);
		if (seckillOrder != null){
			return Result.error(CodeMsg.REPEANT_SECKILL);
		}

        //入队：确定可以秒杀之后，可以将秒杀的信息（用户和对应秒杀的商品id）加入消息队列
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setGoodsId(goodsId);
        seckillMessage.setUser(user);
        sender.sendSeckillMessage(seckillMessage);
		return Result.success(0);  //排队中
	}

    /**
     * 重写此方法：让容器将商品的库存量 存入缓存 便于预减库存逻辑使用
     * @throws Exception
     */
	@Override
	public void afterPropertiesSet() throws Exception {
		//获取所有商品的集合
        List<GoodsVo> goodsVoList = goodsService.goodsVoList();
        if (goodsVoList == null){
            return;
        }
        for (GoodsVo goodsVo : goodsVoList){
            redisService.set(GoodsKey.goodsStock,goodsVo.getId()+"",goodsVo.getStockCount());
        }
	}


	@RequestMapping("/result/{goodsId}")
	public Result<Long> getResult(User user,long goodsId){
		if (user == null){
			return Result.error(CodeMsg.NEED_LOGIN);
		}

		long result = seckillService.getSeckillResult(user.getId(),goodsId);
		return Result.success(result);
	}
}
