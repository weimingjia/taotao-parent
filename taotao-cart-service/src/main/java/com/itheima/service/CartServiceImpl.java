package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.mapper.ItemMapper;
import com.itheima.pojo.Cart;
import com.itheima.pojo.Item;
import com.itheima.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.service
 *  @文件名:   CartServiceImpl
 *  @创建者:   admin
 *  @创建时间:  2018/12/3 16:10
 *  @描述：    TODO
 */

@Service
public class CartServiceImpl implements CartService{

    private static final String CART_KEY="iitcart_";

    @Autowired
    private ItemMapper itemMapper;


    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    //把商品添加到购物车
    @Override
    public void addItemToCart(long userId, long id, int num) {

        System.out.println("要添加商品到购物车了");

        List<Cart> cartList = queryCartByUserId(userId);

        Cart c=null;

        //遍历之前购物车的商品
        for (Cart cart: cartList){
            if(cart.getItemId()==id){
                c= cart;
                break;
            }
        }

        if (c!=null){
            c.setNum(c.getNum()+num);

            c.setUpdate(new Date());
        }else {
            Item item = itemMapper.selectByPrimaryKey(id);

            Cart cart=new Cart();

            cart.setUserId(userId);
            cart.setItemId(id);
            cart.setItemTitle(item.getTitle());
            cart.setItemPrice(item.getPrice());
            cart.setItemImage(item.getImages()[0]);
            cart.setCreate(new Date());
            cart.setUpdate(new Date());
            cart.setNum(num);

            cartList.add(cart);

        }
        String json = new Gson().toJson(cartList);

        System.out.println("现在购物车的商品有" + json);
        redisTemplate.opsForValue().set("iitcart_"+userId,json);

    }


    //根据用户id 查询他对应的购物车数据
    @Override
    public List<Cart> queryCartByUserId(long userId) {



        //根据用户id查询redis，获取以前购物车数据，是一个json数据
        String json = redisTemplate.opsForValue().get("iitcart_" + userId);

        if (!StringUtils.isEmpty(json)){
            List<Cart> cartList = new Gson().fromJson(json, new TypeToken<List<Cart>>(){}.getType());

            return  cartList;
        }

        return new ArrayList<Cart>();
    }

    @Override
    public void updateNumByCart(long userId, long itemId, int num) {


        String json = redisTemplate.opsForValue().get(CART_KEY+ userId);

        List<Cart> cartList = new Gson().fromJson(json, new TypeToken<List<Cart>>(){}.getType());

        for (Cart cart:cartList){
            if (itemId==cart.getItemId()){
                cart.setNum(num);
                cart.setUpdate(new Date());
                break;
            }
        }

        json=new Gson().toJson(cartList);
        redisTemplate.opsForValue().set(CART_KEY+userId,json);

    }

    @Override
    public void deleteItemByCart(long userId, long itemId) {

        List<Cart> cartList = RedisUtil.findCartFromRedis(redisTemplate, CART_KEY + userId);


        for (Cart cart:cartList){
            if (itemId==cart.getItemId()){
                cartList.remove(cart);
                break;
            }
        }

        RedisUtil.saveCartToRedis(cartList,redisTemplate,CART_KEY+userId);

    }
}
