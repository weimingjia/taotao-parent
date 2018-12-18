package com.itheima.cart;

import com.itheima.pojo.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*
 *  @项目名：  taotao-parent
 *  @包名：    com.itheima.cart.impl
 *  @文件名:   CartCookieService
 *  @创建者:   admin
 *  @创建时间:  2018/12/13 16:17
 *  @描述：    TODO
 */
public interface CartCookieService {

    void addItemByCookie(long itemId, int num, HttpServletRequest request, HttpServletResponse response);

    List<Cart> queryCartByCookie(HttpServletRequest request);

    void  updateCartByCookie(long itemId,int num,HttpServletRequest request,HttpServletResponse response );

}
