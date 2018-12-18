package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.itheima.mapper.ContentMapper;
import com.itheima.pojo.Content;
import com.itheima.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.util.*;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service.impl
 *  @文件名:   ContentServiceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/10/30 9:16
 *  @描述：    TODO
 */

@Service
public class ContentServiceImpl implements ContentService {


    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public int add(Content content) {


        Date date=new Date();
        content.setCreated(date);
        content.setUpdated(date);

        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("bingAd","");
        return contentMapper.insert(content);
    }

    @Override
    public PageInfo<Content> list(int categoryId, int page, int rows) {

        //设置分页
        PageHelper.startPage(page,rows);

        Content content=new Content();
        content.setCategoryId((long) categoryId);
        List<Content> list = contentMapper.select(content);

        return new PageInfo<>(list);
    }

    @Override
    public int edit(Content content) {

        content.setUpdated(new Date());
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("bingAd","");
        return contentMapper.updateByPrimaryKeySelective(content);

    }

    @Override
    public int delete(String ids) {

        String[] idArray=ids.split(",");
        int result =0;

        for(String id:idArray){
            result += contentMapper.deleteByPrimaryKey(Long.parseLong(id));
        }

        //删除mysql后，也要去删除redis缓存的数据
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        opsForValue.set("bingAd","");

        return result;
    }


    /*//这里是获取首页的内容，目前仅仅是获取了6张广告
    @Override
    public List<Content> selectByCategoryId(long cid) {


        Content content=new Content();

        content.setCategoryId(cid);
        return contentMapper.select(content);

    }*/

    //用Redis缓存
    /*1.先从redis里面拿数据。
    *2.有就直接返回，没有就去查mysql数据。
    *3.查询完毕，需要吧查到的数据缓存到Redis里面，并且返回这份数据给页面显示
    */


    @Override
    public String selectByCategoryId(long cid) {

        ValueOperations<String,String> operations=redisTemplate.opsForValue();
        String json=operations.get("bingAd");
        System.out.println("从缓存里面获取广告数据" + json);
        if(!StringUtils.isEmpty(json)){
            System.out.println("缓存里面有广告的数据，直接返回");
            return json;
        }

        System.out.println("缓存里面没有广告的数据，要去查询数据库");

        Content c=new Content();
        c.setCategoryId(cid);

        //从mysql数据库里面查询出来广告的的数据
        List<Content> contents = contentMapper.select(c);
        //自己组拼页面要求的字段信息
        List<Map<String,Object>> list=new ArrayList<>();
        //吧数据库查询出来的集合，遍历，一个content就对应一个map集合
        for (Content content:contents){

            Map<String,Object> map=new HashMap<>();
            map.put("src",content.getPic());
            map.put("width",670);
            map.put("height",240);
            map.put("href",content.getUrl());

            list.add(map);

        }

        //把list集合编程json字符串然后存进去
        json =new Gson().toJson(list);
        //存到redis里面去
        operations.set("bingAd",json);



        return json;

    }

}
