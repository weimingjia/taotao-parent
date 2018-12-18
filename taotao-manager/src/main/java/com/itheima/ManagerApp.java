package com.itheima;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima
 *  @文件名:   ManagerApp
 *  @创建者:   admin
 *  @创建时间:  2018/10/5 11:35
 *  @描述：    TODO
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)

@SpringBootApplication
public class ManagerApp {

    public static void main(String [] args){
        SpringApplication.run(ManagerApp.class,args);
    }

}
