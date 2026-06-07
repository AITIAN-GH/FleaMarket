package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.Cart;
import com.xu.system.vo.CartVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 判断商品是否已经在购物车中
     * @param userId 用户id
     * @param productId 商品id
     * @return bookName String
     */
    public String productInCart(@Param("userId") int userId, @Param("productId") int productId);

    /**
     * 加入购物车
     * @param userId 用户id
     * @param productId 商品id
     * @return int
     */
    public int addProductToCart(@Param("userId") int userId,@Param("productId") int productId);

    /**
     * 获取用户购物车信息
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
    public int upUserCart(@Param("cartId") int cartId,@Param("count") int count);

    /**
     * 通过cartId查询相关商品库存数量
     * @param cartId int
     * @return Integer
     */
    Integer getStockByCartId(int cartId);
}
