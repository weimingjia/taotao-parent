package com.itheima.controller;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.controller
 *  @文件名:   CartController
 *  @创建者:   admin
 *  @创建时间:  2018/12/3 15:55
 *  @描述：    TODO
 */

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.cart.CartCookieService;
import com.itheima.pojo.Cart;
import com.itheima.pojo.User;
import com.itheima.service.CartService;
import com.itheima.utils.CookieUtil;
import com.itheima.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private RedisTemplate<String,String> template;

    @Autowired
    private CartCookieService cartCookieService;

    @Reference
    private CartService cartService;

    //http://www.taotao.com/cart/add/1458729470.html?num=4
    //@ResponseBody
    @RequestMapping("/cart/add/{id}.html")
    public String addToCart(@PathVariable long id , int num, HttpServletRequest request, HttpServletResponse response){

        //从cookie里面获取用户登录凭证
        String ticket = CookieUtil.findTicket(request);

        //如果凭证部位为空，表示你已经登录了
        if (ticket != null){

            User user = RedisUtil.findUserByTicket(template, ticket);

            cartService.addItemToCart(user.getId(),id,num);

        }else {
            //没有登录
            System.out.println("添加商品到购物车：：：没有登录");
            cartCookieService.addItemByCookie(id,num,request,response);


        }

        //去购物车页面显示数据
        return "cartSuccess";
    }


    @RequestMapping("/cart/cart.html")
    public String showCart(HttpServletRequest request, Model model){


        //从cookie里面获取用户登录凭证
        String ticket = CookieUtil.findTicket(request);

        //如果凭证部位为空，表示你已经登录了
        List<Cart> cartList =null;
        if (ticket != null){

            User user = RedisUtil.findUserByTicket(template, ticket);

            cartList = cartService.queryCartByUserId((user.getId()));

            //model.addAttribute("cartList",cartList);

        }else {
            //没有登录

            cartList = cartCookieService.queryCartByCookie(request);

        }

        model.addAttribute("cartList",cartList);

        return "cart";
    }


    @RequestMapping("/service/cart/update/num/{id}/{num}")
    @ResponseBody
    public void updateNumByCart(@PathVariable long id,@PathVariable int num,HttpServletRequest request,HttpServletResponse response){


        String ticket = CookieUtil.findTicket(request);

        //如果凭证部位为空，表示你已经登录了
        if (ticket != null) {

            User user = RedisUtil.findUserByTicket(template, ticket);

            cartService.updateNumByCart(user.getId(),id ,num);
        }else {

            cartCookieService.updateCartByCookie(id,num,request,response);
        }
    }


    @RequestMapping("/cart/delete/{id}.shtml")
    public String deleteItemByCart(@PathVariable long id,HttpServletRequest request){

        String ticket = CookieUtil.findTicket(request);

        User user = RedisUtil.findUserByTicket(template, ticket);
 //123
        //456
        cartService.deleteItemByCart(user.getId(),id);

        return "redirect:/cart /cart.html";
    }

}
