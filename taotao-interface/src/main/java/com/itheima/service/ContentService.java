package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Content;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service
 *  @文件名:   ContentService
 *  @创建者:   admin
 *  @创建时间:  2018/10/30 9:15
 *  @描述：    TODO
 */
public interface ContentService {

    int add(Content content);


    PageInfo<Content> list(int categoryId, int page, int rows);

    int edit(Content content);

    int delete(String ids);

    String selectByCategoryId(long cid);
}
