package com.itheima.service;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service
 *  @文件名:   ItemService
 *  @创建者:   admin
 *  @创建时间:  2018/10/16 19:11
 *  @描述：    TODO
 */


import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;

public interface ItemService {

    int addItem(Item item,String desc);

    PageInfo<Item> list(int page,int rows);

    Item getItemById(long id);

    int deleteItem(long id);

    int updateItem(Item item);
}
