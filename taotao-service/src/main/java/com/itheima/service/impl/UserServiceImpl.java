package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service.impl
 *  @文件名:   UserServiceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/10/5 11:43
 *  @描述：    TODO
 */


@Service //用dubbo的注解
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save() {

    }

    @Override
    public User selectOne(long id) {
        return null;
    }

    @Override
    public PageInfo<User> findByPage(int currentPage, int pageSize) {
        return null;
    }

    @Override
    public Boolean check(String param, int type) {
        return null;
    }

    @Override
    public String selectUser(String ticket) {
        return null;
    }

    @Override
    public int addUser(User user) {
        user.setCreated(new Date());
        user.setUpdated(new Date());

        String password =user.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);

        return userMapper.insert(user);
    }

    @Override
    public String login(User user) {

        String password =user.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);

        List<User> list = userMapper.select(user);

        if(list.size() > 0 ){

            User loginUser = list.get(0);
            String json=new Gson().toJson(loginUser);
            String key="iit02_"+ UUID.randomUUID().toString();
            System.out.println(key + "=" + json);
            //保存到redis里面
            redisTemplate.opsForValue().set(key,json);
            return key;
        }

        return null;
    }



    /*@Autowired
    private UserMapper userMapper;


    @Override

    public void save() {
        System.out.println("调用了 UserServiceImpl的save方法");
    }



    @Override
    public User selectOne(long id) {

        Long id1 = 7L;
        return userMapper.selectByPrimaryKey(id1);

    }

    @Override
    public PageInfo<User> findByPage(int currentPage, int pageSize) {

        PageHelper.startPage(currentPage,pageSize);

        List<User> list = userMapper.selectAll();


        return new PageInfo<>(list);
    }*/
}
