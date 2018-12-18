package com.itheima.controller;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   ItemController
 *  @创建者:   admin
 *  @创建时间:  2018/10/16 19:07
 *  @描述：    TODO
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;
import com.itheima.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ItemController {


    @Reference
    private ItemService itemService;

    //添加商品的时候，大部分内容都会装载到item对象里面去
    @RequestMapping(value = "/rest/item",method = RequestMethod.POST)
    @ResponseBody
    public String addItem(Item item,String desc){

        int result = itemService.addItem(item,desc);

        System.out.println(result);

        return "success";

    }


    @RequestMapping(value = "/rest/item",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> list(int page, int rows){

        PageInfo<Item> pageInfo=itemService.list(page, rows);

        Map<String,Object> map=new HashMap<>();
        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());

        return map;
    }
}
