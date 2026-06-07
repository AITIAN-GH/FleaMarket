package com.xu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.common.pojo.Constants;
import com.xu.system.mapper.CartMapper;
import com.xu.system.mapper.OrderMapper;
import com.xu.system.pojo.Cart;
import com.xu.system.pojo.Order;
import com.xu.system.pojo.Product;
import com.xu.system.service.OrderService;
import com.xu.system.service.ProductService;
import com.xu.system.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private CartMapper cartMapper;
    @Resource
    private ProductService productService;

    @Override
    public Boolean addOrder(List<Integer> cartIds) {
        Order order = new Order();
        List<Cart> carts = cartMapper.selectList(new QueryWrapper<Cart>().in("cart_id",cartIds));
        if (carts == null) {
            return false;
        }
        String orderCode = String.valueOf(System.currentTimeMillis());
        for (Cart cart : carts) {
            order.setOrderCode(orderCode);
            order.setUserId(cart.getUserId());
            order.setProductId(cart.getProductId());
            order.setBycount(cart.getCount());
            orderMapper.addOrder(order);
            cartMapper.deleteById(cart);
        }
        return true;
    }

    @Override
    public List<OrderVo> getOrderList(Integer userId) {
        return orderMapper.getOrderList(userId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateOrderByOrderCode(String outTradeNo) {
        AtomicBoolean res = new AtomicBoolean(true);
        List<Order> list = orderMapper.selectAll(outTradeNo);
        List<Integer> orderIds = list.stream().map(Order::getOrderId).collect(Collectors.toList());
        // 获取每个商品 购买数量
        Map<Integer, Integer> changeMap = list.stream().collect(Collectors.toMap(Order::getProductId, Order::getBycount));

        // 获取商品 id
        List<Integer> productIds = new ArrayList<>(changeMap.keySet());
        if (list.size() < 1) {
            log.info("===================");
            log.info("订单状态异常！！");
            throw new RuntimeException();
        }
        // 修改订单状态
        list.forEach(order -> {
            order.setState(Constants.PAYED_SUCCESS);
            order.setDeleted(1);
            int i = orderMapper.updateById(order);
            if (i == 0 || i == -1) {
                res.set(false);
            }
        });
        int i = orderMapper.deleteBatchIds(orderIds);

        res.set(i != 0 && i != -1);
        if (!res.get() || productIds.size() < 1) {
            log.info("===================");
            log.info("订单状态异常！！");
            throw new RuntimeException();
        }
        // 修改商品库存并持久化
        List<Product> products = productService.list(new QueryWrapper<Product>().in("id", productIds));
        products.forEach(product -> {
            Integer byCount = changeMap.get(product.getId());
            if(product.getStock() > 0 && product.getStock() >= byCount){
                product.setStock(product.getStock() - byCount);
            }
        });
        res.set(productService.updateBatchById(products));
        return res.get();
    }

    @Override
    public List<OrderVo> getPayedOrderList(Integer userId) {
        return orderMapper.getPayedOrderList(userId);
    }

    @Override
    public boolean cancelOrderByCode(String orderCode) {
        return orderMapper.cancelOrderByCode(orderCode);
    }

}