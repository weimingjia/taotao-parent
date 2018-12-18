package com.itheima.service;

import com.itheima.pojo.ContentCategory;

import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service
 *  @文件名:   ContentCategoryService
 *  @创建者:   admin
 *  @创建时间:  2018/10/25 23:33
 *  @描述：    TODO
 */
public interface ContentCategoryService {

    List<ContentCategory>  getCategoryByParentId(Long id);

    ContentCategory add(ContentCategory contentCategory);

    int update(ContentCategory contentCategory);

    int delete(ContentCategory contentCategory);


}
