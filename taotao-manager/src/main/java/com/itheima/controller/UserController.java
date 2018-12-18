package com.itheima.controller;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   UserController
 *  @创建者:   admin
 *  @创建时间:  2018/10/5 11:35
 *  @描述：    TODO
 */

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    //@Autowired 只适合在当前项目寻找
    //@Reference
    private UserService userService;

    @RequestMapping("save")
    public String save(){
        System.out.println("调用了UserController的save方法");

        userService.save();
        return  "save success!";
    }


    @RequestMapping("selectOne")
    public User selectOne(){
        User user=userService.selectOne(7);
        return  user;
    }

    @RequestMapping("findByPage")
    public PageInfo<User> findByPage(int currentPage,int pageSize){

        return  userService.findByPage(currentPage,pageSize);
    }

}
