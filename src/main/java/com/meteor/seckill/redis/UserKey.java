package com.meteor.seckill.redis;

public class UserKey extends BasePrefix{

	/**
	 * 私有构造方法：过期时间和key的前缀
	 * @param expireSeconds
	 * @param prefix
	 */
	private UserKey(int expireSeconds,String prefix) {
		super(expireSeconds,prefix);
	}

	//token的过期时间:2天
	public static final int TOKEN_EXPIRE = 3600*24*2;

	//具体的token
	public static UserKey token = new UserKey(TOKEN_EXPIRE,"tx");

}
