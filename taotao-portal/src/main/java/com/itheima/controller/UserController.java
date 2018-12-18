package com.itheima.controller;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   UserController
 *  @创建者:   admin
 *  @创建时间:  2018/11/16 21:01
 *  @描述：    注册用户
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Reference
    private UserService userService;


    @PostMapping("/usr/doRegister.shtml")
    @ResponseBody
    public Map<String,String> register(User user){
        System.out.println(user);

        int result=userService.addUser(user);

        Map<String,String> map =new HashMap<>();
        if(result>0){
            map.put("status","200");
        }else {
            map.put("status","500");
        }

        return map;
    }


    /*

    1.登录成功后，生成一个唯一的key（ticke），这这个key和用户的数据保存到redis数据库里面

    2.为了保证下次直接添加到购物车，能够知道是谁完成的操作，需要从redis里面获取用户信息，
    哟啊想获取用户的信息，必须拥有可以（ticket），吧这个key写到cookie里面去，传输给客户端(浏览器）
     */
    @PostMapping("/user/doLogin.shtml")
    @ResponseBody
    public Map<String,String> login(User user, HttpServletResponse response){

        Map<String,String> map=new HashMap<>();

        String ticket = userService.login(user);

        if (!StringUtils.isEmpty(ticket)){
            Cookie cookie =new Cookie("ticket",ticket);

            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/");

            response.addCookie(cookie);

            map.put("status","200");
            map.put("success","http://www.taotao.com");

            return map;

        }

        map.put("status","500");
        return map;

    }


}
