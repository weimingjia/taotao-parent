package com.itheima.freemarker;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service
 *  @文件名:   ItemService
 *  @创建者:   admin
 *  @创建时间:  2018/12/2 14:13
 *  @描述：    TODO
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Item;
import com.itheima.pojo.ItemDesc;
import com.itheima.service.ItemDescService;
import com.itheima.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
public class ItemFreeMarker {

    @Reference
    private ItemService itemService;

    @Reference
    private ItemDescService itemDescService;

    @JmsListener(destination = "item-save")
    public void addItem(String message) throws Exception {
        System.out.println("item收到的消息是：" + message);

        //根据id查询商品数据
        Item item = itemService.getItemById(Long.parseLong(message));

        ItemDesc itemDesc = itemDescService.getDescById(Long.parseLong(message));


        //2.生成html 封装数据
        Configuration configuration=new Configuration(Configuration.VERSION_2_3_27);


        String path="C:\\Users\\admin\\IdeaProjects\\taotao-parent\\taotao-item-web\\src\\main\\webapp\\ftl";
        configuration.setDirectoryForTemplateLoading(new File(path));

        //获取魔板
        Template template = configuration.getTemplate("item.ftl");

        //指定生成的html位置
        Writer out =new FileWriter("D:\\taotao\\item\\"+message+".html");

        //指定数据来源
        Map<String,Object> root =new HashMap<>();


        root.put("item",item);

        root.put("itemDesc",itemDesc);

        template.process(root,out);


        out.close();

    }

}
