package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.User;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service
 *  @文件名:   UserService
 *  @创建者:   admin
 *  @创建时间:  2018/10/5 12:01
 *  @描述：    TODO
 */
public interface UserService {

    void save();

    User selectOne(long id);

    PageInfo<User> findByPage(int currentPage, int pageSize);

    Boolean check(String param,int type);

    String selectUser(String ticket);

    /**
     * 注册用户
     */

    int addUser(User user);

    String login(User user);
}
