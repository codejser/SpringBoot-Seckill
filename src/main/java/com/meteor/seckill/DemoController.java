package com.meteor.seckill;

import com.meteor.seckill.result.CodeMsg;
import com.meteor.seckill.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: meteor @Date: 2018/8/29 14:07
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/")
    @ResponseBody
    public String sayHello(){
        return "Hello World!";
    }


    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        return Result.success("hello Result<String>");
    }

    @RequestMapping("/helloerror")
    @ResponseBody
    public Result<String> helloError(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","meteor");
        return "hello";
    }
}
