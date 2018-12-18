package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.ItemCatMapper;
import com.itheima.pojo.ItemCat;
import com.itheima.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service.impl
 *  @文件名:   ItemCatService
 *  @创建者:   admin
 *  @创建时间:  2018/10/16 17:16
 *  @描述：    TODO
 */

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public List<ItemCat> selectItemCatByParentId(long parentId) {


        ItemCat itemCat=new ItemCat();
        itemCat.setParentId(parentId);

        return itemCatMapper.select(itemCat);
    }
}
