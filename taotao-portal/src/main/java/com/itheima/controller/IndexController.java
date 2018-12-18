package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.itheima.pojo.User;
import com.itheima.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   IndexController
 *  @创建者:   admin
 *  @创建时间:  2018/10/25 14:15
 *  @描述：    TODO
 */
@Controller
public class IndexController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Reference
    private ContentService contentService;

    @RequestMapping("/page/{pageName}")
    public String page(@PathVariable String pageName){
        return pageName;
    }

    /*@RequestMapping("index")
    public String index(){
        System.out.println("显示门户系统");

        return "index";
    }*/

    @RequestMapping("/")
    public String index01(Model model, HttpServletRequest request){

        System.out.println("要获取首页的广告数据了");

        //在这里获取ticket，然后到redis里面去查询用户数据，然后放到页面显示就好了。
        Cookie[] cookies=request.getCookies();
        if(cookies!=null){
            for (Cookie cookie :cookies){
                String name=cookie.getName();
                System.out.println(name);
                if ("ticket".equals(name)){
                    String key=cookie.getValue();

                    String userInfo=redisTemplate.opsForValue().get(key);

                    User user = new Gson().fromJson(userInfo, User.class);

                    model.addAttribute("user",user);
                    break;
                }
            }
        }

        int categoryId= 89;
        String json=contentService.selectByCategoryId(categoryId);

        /*List<Map<String,Object>> list=new ArrayList<>();

        for (Content content:contents){

            Map<String,Object> map=new HashMap<>();
            map.put("src",content.getPic());
            map.put("width",670);
            map.put("height",240);
            map.put("href",content.getUrl());

            list.add(map);

        }*/

        //String json =new Gson().toJson(list);

        model.addAttribute("list",json);

        return "index";
    }
}