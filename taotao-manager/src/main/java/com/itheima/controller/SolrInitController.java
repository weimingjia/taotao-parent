package com.itheima.controller;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   SolrInitController
 *  @创建者:   admin
 *  @创建时间:  2018/11/19 11:39
 *  @描述：    TODO
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.pojo.Item;
import com.itheima.service.ItemService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SolrInitController {

    @Reference
    private ItemService itemService;


    @Autowired
    private SolrClient solrClient;


    @RequestMapping("init")
    public String init() throws Exception{

        int page=1,pageSize =500;

        do {
            PageInfo<Item> pageInfo = itemService.list(page, pageSize);
            List<Item> list=pageInfo.getList();


            List<SolrInputDocument> documentList=new ArrayList<>();
            //查询出来后。吧商品的数据，添加到索引库中

            for (Item item:list){
                SolrInputDocument doc=new SolrInputDocument();

                doc.addField("id",item.getId());
                doc.addField("item_title",item.getTitle());
                doc.addField("item_image",item.getImage());
                doc.addField("item_price",item.getPrice());
                doc.addField("item_cid",item.getCid());
                doc.addField("item_status",item.getStatus());

                documentList.add(doc);

            }

            solrClient.add(documentList);
            solrClient.commit();


            page++;
            pageSize=list.size();

        }while (pageSize==500);

        return "sucess!";
    }
}
