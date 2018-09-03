package com.meteor.seckill.redis;

public class GoodsKey extends BasePrefix{

	/**
	 * 私有构造方法：过期时间和key的前缀
	 * @param expireSeconds
	 * @param prefix
	 */
	private GoodsKey(int expireSeconds, String prefix) {
		super(expireSeconds,prefix);
	}


	public static GoodsKey goodsList = new GoodsKey(60,"gl");
	public static GoodsKey goodsDetail = new GoodsKey(60,"gd");
	//key 永久存在
	public static GoodsKey goodsStock = new GoodsKey(0,"gs");
}
