package com.itheima.service.impl;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service.impl
 *  @文件名:   SearchServiceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/11/19 15:12
 *  @描述：    TODO
 */

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.pojo.Item;
import com.itheima.pojo.Page;
import com.itheima.service.SearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrClient solrClient;

    //数据来自索引库
    @Override
    public Page<Item> searchItem(String q, int page) {


        try {
            int pageSize=16;

            SolrQuery query=new SolrQuery();

            query.setQuery("item_title:"+q);

            query.setStart((page-1)*pageSize);
            query.setRows(pageSize);

            //设置高亮显示
            query.setHighlight(true);
            query.addHighlightField("item_title");
            query.setHighlightSimplePre("<font color='red'>");
            query.setHighlightSimplePost("</font>");



            //查询到当前页的数据
            QueryResponse response = solrClient.query(query);

            //关键字，itemlist，page，totalpage
            SolrDocumentList results = response.getResults();

            long total=results.getNumFound();

            //获取高亮的数据key：id值，value：表示高亮数据  因为高亮的字段可以设置多个，域也可以存放多个值，所以value有是一个list集合
            Map<String,Map<String, List<String>>> map=response.getHighlighting();

            //TODO:需要封装当前这一页的数据
            List<Item> itemList=new ArrayList<Item>();

            for (SolrDocument document:results){

                long id=Long.parseLong((String)document.getFieldValue("id"));
                String title= (String) document.getFieldValue("item_title");
                String image= (String) document.getFieldValue("item_image");
                long price= (long) document.getFieldValue("item_price");
                long cid= (long) document.getFieldValue("item_cid");

                Item item=new Item();
                item.setId(id);
                item.setCid(cid);

                //设置高亮的文字标题
                List<String> list = map.get(id + "").get("item_title");
                if(list!=null&&list.size()>0){
                    String hlTitle=list.get(0);
                    item.setTitle(hlTitle);
                }else {
                    //设置普通文件的标题
                    item.setTitle(title);
                }


                item.setPrice(price);
                item.setImage(image);

                System.out.println(item);


                itemList.add(item);

            }




            //构建page对象：总记录数，当前页码，每页显示个数
            Page<Item> pageItem=new Page<Item>(total,page,pageSize);

            pageItem.setList(itemList);

            return pageItem;


        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
