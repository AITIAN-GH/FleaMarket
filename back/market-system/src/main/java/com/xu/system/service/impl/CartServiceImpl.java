package com.xu.system.service.impl;

import com.xu.system.mapper.CartMapper;
import com.xu.system.mapper.ProductMapper;
import com.xu.system.pojo.Product;
import com.xu.system.service.CartService;
import com.xu.system.vo.CartVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean addProductToCart(int userId, int productId) {
        Product product = productMapper.selectById(productId);
        // 查询到该商品 且 商品有库存 则添加至购物车
        if (product == null || product.getStock().equals(0)){
            return false;
        }
        int i = cartMapper.addProductToCart(userId, productId);
        return i != 0 && i != -1;
    }

    @Override
    public List<CartVo> getUserCart(int userId) {
        return cartMapper.getUserCart(userId);
    }

    /**
     * 修改购物车信息
     * @param cartId 购物车id
     * @param count 更改后数量
     * @return int
     */
    @Override
    public boolean upUserCart(int cartId, int count) {
        Integer stock = cartMapper.getStockByCartId(cartId);
        if (stock == null || stock < count) {
            return false;
        }
        int i = cartMapper.upUserCart(cartId, count);
        return i != 0 && i != -1;
    }

    @Override
    public boolean deleteCartById(Integer id) {
        int i = cartMapper.deleteById(id);
        return i != 0 && i != -1;
    }

    @Override
    public boolean checkProductInCart(int userId, int productId) {
        String inCart = cartMapper.productInCart(userId, productId);
        return inCart != null && !"".equals(inCart);
    }
}
