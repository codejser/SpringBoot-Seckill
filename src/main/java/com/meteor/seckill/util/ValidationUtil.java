package com.meteor.seckill.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: meteor @Date: 2018/8/30 16:38
 * 参数校验工具类
 * 正则表达式匹配相应的手机号码规则
 */
public class ValidationUtil {

    private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
      if (StringUtils.isEmpty(src)){
          return false;
      }
      Matcher m = mobile_pattern.matcher(src);
      return m.matches();
    }
}
