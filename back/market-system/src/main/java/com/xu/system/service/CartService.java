package com.xu.system.service;


import com.xu.system.vo.CartVo;

import java.util.List;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
public interface CartService {
    /**
     * 判断商品是否已经在购物车中
     * @param userId 用户id
     * @param productId 商品id
     * @return boolean
     */
    public boolean checkProductInCart(int userId, int productId);

    /**
     * 加入购物车
     * @param userId 用户id
     * @param productId 商品id
     * @return boolean
     */
    public boolean addProductToCart(int userId, int productId);

    /**
     * 加载购物车信息
     * @param userId 用户id
     * @return List
     */
    public List<CartVo> getUserCart(int userId);

    /**
     * 修改购物车信息
     * @param cartId 购物车id
     * @param count 更改后数量
     * @return int
     */
    public boolean upUserCart(int cartId, int count);

    /**
     * 购物车信息删除
     * @param id 购物车id
     * @return Boolean
     */
    public boolean deleteCartById(Integer id);
}
