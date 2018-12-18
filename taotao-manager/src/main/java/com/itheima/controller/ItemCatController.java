package com.itheima.controller;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   ItemCatController
 *  @创建者:   admin
 *  @创建时间:  2018/10/16 17:09
 *  @描述：    TODO
 */


import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.ItemCat;
import com.itheima.service.ItemCatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {


    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/rest/item/cat")
    @ResponseBody
    public List<ItemCat> selectItemCat(@RequestParam(defaultValue = "0") long id){

        List<ItemCat> list=itemCatService.selectItemCatByParentId(id);

        System.out.println(list);

        return list;
    }

}
