package com.itheima.cart.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.cart.CartCookieService;
import com.itheima.pojo.Cart;
import com.itheima.pojo.Item;
import com.itheima.service.ItemService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.cart.impl
 *  @文件名:   CartCookieServiceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/12/13 16:20
 *  @描述：    TODO
 */
@Service
public class CartCookieServiceImpl implements CartCookieService{

    private final static String CART_KEY = "iit_cart";

    @Reference
    private ItemService itemService;


    @Override
    public void addItemByCookie(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {


        List<Cart> cartList = queryCartByCookie(request);

        //2.判断是否有这件商品，如果有，就累加数量，如果没有就构建全新的cart对象
        Cart c=null;
        for (Cart cart:cartList){
            //表示购物车里面有这件商品
            if (itemId==cart.getItemId()){
                c=cart;
                break;
            }
        }


        if (c!=null){
            //表示购物车商品有这件商品
            c.setNum(c.getNum()+num);
        }else {
            Item item = itemService.getItemById(itemId);

            Cart cart=new Cart();
            cart.setItemId(itemId);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImages()[0]);
            cart.setCreate(new Date());
            cart.setUpdate(new Date());
            cart.setNum(num);

            cartList.add(cart);

        }

        //3.把组装好的list<cart> ==> json 放到cookie里面
        String json = new Gson().toJson(cartList);
        System.out.println("购物车已经添加到cookie里面去了：：：" + json);

        try {
            json=URLEncoder.encode(json,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Cookie cookie=new Cookie(CART_KEY,json);
        cookie.setMaxAge(60*60*24*7);

        cookie.setPath("/");
        response.addCookie(cookie);



    }

    //从cookie查询购物车
    @Override
    public List<Cart> queryCartByCookie(HttpServletRequest request){

        List<Cart> cartList=null;
        Cookie[] cookies = request.getCookies();

        try {
            if (cookies!=null){
                for (Cookie cookie : cookies) {
                    if(CART_KEY.equals(cookie.getName())){
                        String json=cookie.getValue();
                        json= URLDecoder.decode(json,"utf-8");
                        cartList = new Gson().fromJson(json, new TypeToken<List<Cart>>(){}.getType());
                    }

                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //如果从cookie里面拿出来的集合是空，表示从来没有购物车，所以这里要创建一个集合出来等着
        if (cartList==null){
            cartList=new ArrayList<>();
        }

        return cartList;
    }

    @Override
    public void updateCartByCookie(long itemId, int num, HttpServletRequest request,HttpServletResponse response) {


        //1.先查询以前的购物车
        List<Cart> cartList = queryCartByCookie(request);


        //2.遍历购物车，取出匹配商品，修改数量
        for (Cart cart : cartList) {
            if (cart.getItemId()==itemId){
                cart.setNum(num);
                cart.setUpdate(new Date());
                break;
            }
        }

        //3.重新放到cooki里面
        try {
            String json = new Gson().toJson(cartList);
            json=URLEncoder.encode(json,"utf-8");
            Cookie cookie=new Cookie(CART_KEY,json);
            cookie.setMaxAge(60*60*24*7);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
