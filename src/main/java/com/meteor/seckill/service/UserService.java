package com.meteor.seckill.service;

import com.meteor.seckill.dao.UserDao;
import com.meteor.seckill.domain.User;
import com.meteor.seckill.exception.GlobalException;
import com.meteor.seckill.redis.RedisService;
import com.meteor.seckill.redis.UserKey;
import com.meteor.seckill.result.CodeMsg;
import com.meteor.seckill.result.Result;
import com.meteor.seckill.util.MD5Util;
import com.meteor.seckill.util.UUIDUtil;
import com.meteor.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: meteor @Date: 2018/8/29 17:04
 */
@Service
public class UserService {

    //cookie的name为常量字符串
    public static final String COOKIE_NAME = "token";

    @Autowired
    private UserDao userDao;

    @Autowired
    RedisService redisService;

    /**
     * 通过token查找对应的对象，并重新设置对象
     * @param response
     * @param token
     * @return
     */
    public User getByToken(HttpServletResponse response,String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }
        User user = redisService.get(UserKey.token,token,User.class);

        if(user != null) {
            addCookieAndRedis(response,token,user);
        }
        return user;
    }


    /**
     * 处理登陆逻辑
     * @param response
     * @param loginVo
     * @return
     */
    public Boolean login(HttpServletResponse response, LoginVo loginVo){
        if (loginVo == null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
            //return Result.error(CodeMsg.SERVER_ERROR);
        }
        //获取视图中的手机号和密码
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        //判断该用户是否存在
        User user = userDao.getUserById(Long.parseLong(mobile));
        if (user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
            //return Result.error(CodeMsg.MOBILE_NOT_EXIST);
        }
        //获取数据库中的密码，进行比对
        String dbpass = user.getPassword();
        //获取数据库中的用户盐值
        String userSalt = user.getSalt();
        //根据请求的参数，计算二次加密后的密码
        String secondPass = MD5Util.md5Second(password,userSalt);
        if (!dbpass.equals(secondPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
            //return Result.error(CodeMsg.PASSWORD_ERROR);
        }
        //生成随机的token,一部分作为保存相应对象（此处为用户对象User）的key和cookie的value值
        String token = UUIDUtil.uuid();
        addCookieAndRedis(response,token,user);
        return true;
    }

    private void addCookieAndRedis(HttpServletResponse response, String token, User user) {

        //token/user存入redis
        redisService.set(UserKey.token,token,user);
        //处理cookie相关的逻辑
        Cookie ck = new Cookie(COOKIE_NAME,token);
        ck.setMaxAge(UserKey.token.expireSeconds());
        ck.setPath("/");
        response.addCookie(ck);
    }

}
