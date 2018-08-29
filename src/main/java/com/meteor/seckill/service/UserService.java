package com.meteor.seckill.service;

import com.meteor.seckill.dao.UserDao;
import com.meteor.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: meteor @Date: 2018/8/29 17:04
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 测试userDao
     * @param id
     * @return
     */
    public User getUserId(int id){
        return userDao.getUserById(id);
    }

    /**
     * 测试事务
     * 此时插入方法在一个事务中，
     * 先后插入u1,u2;
     * 若事务成功，
     * 则数据库会回滚到该方法之前。
     * @return
     */
    @Transactional
    public boolean insert(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("test2");
        userDao.insert(u1);

        /*User u2 = new User();
        u2.setId(1);
        u2.setName("test1");
        userDao.insert(u2);*/

        return true;
    }

}
