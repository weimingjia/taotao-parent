package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service.impl
 *  @文件名:   UserServiceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/11/12 21:13
 *  @描述：    TODO
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

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

    /**
     *
     * @param param
     * @param type
     * @return true :表示存在
     */
    @Override
    public Boolean check(String param, int type) {

        User user=new User();

        switch (type) {
            case 1:
                user.setUsername(param);
                break;
            case 2:
                user.setPhone(param);
                break;
            case 3:
                user.setEmail(param);
                break;
            default:
                user.setUsername(param);
                break;

        }

        List<User> list=userMapper.select(user);

        //false：表示不能用了， 已经被占用了
        //true：表示可用使用。
        return list.size()>0?false:true;
    }

    @Override
    public String selectUser(String ticket) {

        //这里要到redis里面用户的信息
        String key ="iit_"+ticket;

        return redisTemplate.opsForValue().get(key);
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
        return null;
    }


}
