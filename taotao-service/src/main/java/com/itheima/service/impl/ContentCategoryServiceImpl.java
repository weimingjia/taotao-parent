package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.ContentCategoryMapper;
import com.itheima.pojo.ContentCategory;
import com.itheima.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service.impl
 *  @文件名:   ContentCategoryServiceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/10/25 23:35
 *  @描述：    TODO
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {


    @Autowired
    private ContentCategoryMapper mapper;

    @Override
    public List<ContentCategory> getCategoryByParentId(Long id) {

        ContentCategory category=new ContentCategory();

        category.setParentId(id);

        return mapper.select(category);
    }



    @Override
    public ContentCategory add(ContentCategory contentCategory) {

        contentCategory.setStatus(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());

        mapper.insertSelective(contentCategory);

        //2.上面的代码针对场景是：在父亲分类下创建子分类。如果我们是在子分类aa 下创建子分类bb
        //那么上面的代码只能添加子分类bb，并不会子分类aa变成父级分类


        Long parenId= contentCategory.getParentId();

        ContentCategory parentCategory=mapper.selectByPrimaryKey(parenId);

        if(!parentCategory.getIsParent()){
            parentCategory.setIsParent(true);
        }

        mapper.updateByPrimaryKeySelective(parentCategory);
        return contentCategory;
    }

    @Override
    public int update(ContentCategory contentCategory) {
        int rows = mapper.updateByPrimaryKeySelective(contentCategory);
        return rows;
    }

    @Override
    public int delete(ContentCategory contentCategory) {

        //1现在只能删除子级分类
        //int result = mapper.deleteByPrimaryKey(contentCategory);


        //2.直接删除父级分类

        List<ContentCategory> list=new ArrayList<>();

        list.add(contentCategory);

        findAllChild(list,contentCategory.getId());

        int result =deleteAll(list);

        //最后一个问题 ，aaa没有子级分类了，这个时候他应该变成一个子级分类，而不是原来的父级分类
        //这里是按照parentid去子级分类的总数
        ContentCategory c=new ContentCategory();
        c.setParentId(contentCategory.getParentId());
        int count=mapper.selectCount(c);

        //表示当前操作的这个节点的父亲  下面没有子节点了
        if(count<1){

            c=new ContentCategory();
            c.setId(contentCategory.getParentId());
            c.setIsParent(false);
            mapper.updateByPrimaryKeySelective(c);
        }



        return result;
    }


    /**
    *删除一个集合
    *
    **/
    private int deleteAll(List<ContentCategory> list) {


        int result =0;
        for (ContentCategory category : list) {
            result += mapper.delete(category);
        }

        return  result;
    }

    /*
    * c查询给定的id所有子级分类
    * */
    private void findAllChild(List<ContentCategory> list, Long id) {

        //找到当前节点的孩子
        List<ContentCategory> childList = getCategoryByParentId(id);

        if(childList!=null&&childList.size()>0){

        for (ContentCategory category:childList) {

            list.add(category);

            findAllChild(list, category.getId());

        }

        }
    }
}
