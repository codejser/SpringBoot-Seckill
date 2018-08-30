package com.meteor.seckill.Controller;

import com.meteor.seckill.result.Result;
import com.meteor.seckill.service.UserService;
import com.meteor.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author: meteor @Date: 2018/8/30 14:49
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登陆页面的控制器
     * @return
     */
    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    /**
     * 处理登陆的逻辑
     * @return
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        userService.login(response,loginVo);
        return Result.success(true);
    }

}
