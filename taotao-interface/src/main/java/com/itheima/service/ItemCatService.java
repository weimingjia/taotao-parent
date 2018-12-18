package com.itheima.service;

import com.itheima.pojo.ItemCat;

import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service
 *  @文件名:   ItemCatService
 *  @创建者:   admin
 *  @创建时间:  2018/10/16 17:14
 *  @描述：    TODO
 */
public interface ItemCatService {

    List<ItemCat> selectItemCatByParentId(long parentId);
}
