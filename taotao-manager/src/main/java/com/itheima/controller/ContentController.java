package com.itheima.controller;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   ContentController
 *  @创建者:   admin
 *  @创建时间:  2018/10/30 9:13
 *  @描述：    TODO
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ContentController {

    @Reference
    private ContentService contentService;

    //@RequestMapping("/rest/content")
    @PostMapping("/rest/content")
    public String add(Content content){
        contentService.add(content);
        return "seccess";
    }

    //@RequestMapping("/rest/content")
    @GetMapping("/rest/content")
    public Map<String,Object> list(int categoryId,int page,int rows){


        PageInfo<Content> pageInfo = contentService.list(categoryId, page, rows);

        Map<String,Object> map =new HashMap<>();

        map.put("total",pageInfo.getTotal());
        map.put("rows",pageInfo.getList());


        return map;
    }

    @RequestMapping("/rest/content/edit")
    public Map<String ,Integer> edit(Content content){
        int edit = contentService.edit(content);

        System.out.println(edit);

        Map<String ,Integer> map=new HashMap<>();

        if(edit>0){
            map.put("status",200);
        }else {
            map.put("status",500);
        }

        return map;
    }

    @RequestMapping("/rest/content/delete")
    public Map<String ,Integer> delete(String ids){

        int result = contentService.delete(ids);
        System.out.println(result);


        Map<String ,Integer> map=new HashMap<>();
        if(result>0){
            map.put("status",200);
        }else {
            map.put("status",500);
        }

        return map;
    }

}
