package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.ItemDescMapper;
import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;

import java.util.Date;
import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service.impl
 *  @文件名:   ItemSerivceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/10/16 19:12
 *  @描述：    TODO
 */

@Service
public class ItemSerivceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescMapper itemDescMapper;

    @Autowired
    private JmsMessagingTemplate jms;


    @Override
    public int addItem(Item item, String desc) {

        //添加item表
        //itemMapper.insert();//添加数据
        //itemMapper.insertSelective(item);//添加数据selective；有选项性


        long id= (long)(System.currentTimeMillis()+Math.random()*10000);

        item.setId(id);
        item.setStatus(1);
        item.setCreated(new Date());
        item.setUpdated(new Date());

        int result =itemMapper.insertSelective(item);


        //添加desc表
        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemId(id);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());

        itemDescMapper.insertSelective(itemDesc);

        //添加商品完毕后，要记得发消息给mq
        jms.convertAndSend("item-save",id);


        return result;

    }

    @Override
    public PageInfo<Item> list(int page, int rows) {

        PageHelper.startPage(page,rows);

        List<Item> list= itemMapper.select(null);

        return new PageInfo<Item>(list);
    }

    @Override
    public Item getItemById(long id) {
        return itemMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteItem(long id) {
        return itemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateItem(Item item) {
        return itemMapper.updateByPrimaryKey(item);
    }
}
