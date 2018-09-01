package com.meteor.seckill.Controller;


import com.meteor.seckill.domain.User;
import com.meteor.seckill.redis.GoodsKey;
import com.meteor.seckill.redis.RedisService;
import com.meteor.seckill.result.Result;
import com.meteor.seckill.service.GoodsService;
import com.meteor.seckill.service.UserService;
import com.meteor.seckill.vo.GoodsDetailVo;
import com.meteor.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	ApplicationContext applicationContext;
	
    @RequestMapping(value = "/to_list",produces = "text/html")
	@ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response,Model model) {
    	//model.addAttribute("user", user);
		//从缓存中取页面的数据
		String html = redisService.get(GoodsKey.goodsList,"",String.class);
		//如果有缓存直接返回
		if (!StringUtils.isEmpty(html)){
			return html;
		}
		//如果没有缓存，就正常执行逻辑
		List<GoodsVo> goodsVoList = goodsService.goodsVoList();
		model.addAttribute("goodsList",goodsVoList);
		//手动渲染页面：1.先获取需要渲染的数据 2，将数据渲染到html上
		SpringWebContext ctx = new SpringWebContext(request,response,
				request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
		html = thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);
		//存入缓存
		if (!StringUtils.isEmpty(html)){
			redisService.set(GoodsKey.goodsList,"",html);
		}
		//返回页面
        return html;
    }

	/**
	 * thymeleaf模板引擎
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/to_detail3/{goodsId}")
	@ResponseBody
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
		return "goods_list";
	}

	/**
	 * 页面缓存技术
	 * @param request
	 * @param response
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
    @RequestMapping("/to_detail/{goodsId}")
	@ResponseBody
	public String detail2(HttpServletRequest request, HttpServletResponse response,Model model, User user, @PathVariable("goodsId") long goodsId){
    	model.addAttribute("user",user);
		//从缓存中取页面的数据
		String html = redisService.get(GoodsKey.goodsDetail,"",String.class);
		//如果有缓存直接返回
		if (!StringUtils.isEmpty(html)){
			return html;
		}
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

		//手动渲染页面：1.先获取需要渲染的数据 2，将数据渲染到html上
		SpringWebContext ctx = new SpringWebContext(request,response,
				request.getServletContext(),request.getLocale(), model.asMap(), applicationContext );
		html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",ctx);
		//存入缓存
		if (!StringUtils.isEmpty(html)){
			redisService.set(GoodsKey.goodsDetail,"",html);
		}
		//返回页面
		return html;
	}

	/**
	 * 页面静态化所需的Controller
	 * @param request
	 * @param response
	 * @param model
	 * @param user
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("/detail/{goodsId}")
	@ResponseBody
	public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable("goodsId") long goodsId){
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
		//将秒杀的状态值放入GoodsDetailVo中，以Json的形式返回给前端
		GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
		goodsDetailVo.setGoods(goods);
		goodsDetailVo.setMiaoshaStatus(seckillStatus);
		goodsDetailVo.setRemainSeconds(remainTime);
		goodsDetailVo.setUser(user);
		return Result.success(goodsDetailVo);
	}
    
}
