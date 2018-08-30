package com.meteor.seckill.dao;

import com.meteor.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * @Author: meteor @Date: 2018/8/29 16:58
 */
@Component
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") long id);

}
