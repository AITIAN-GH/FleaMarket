package com.xu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.system.pojo.Order;
import com.xu.system.vo.OrderVo;

import java.util.List;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
public interface OrderService extends IService<Order> {

    /**
     * 下订单
     * @param cartIds 购物车id
     * @return Boolean
     */
    public Boolean addOrder(List<Integer> cartIds);

    /**
     * 获取用户订单信息
     * @param userId 用户id
     * @return List<CartBo>
     */
    public List<OrderVo> getOrderList(Integer userId);

    /**
     * 通过OrderCode修改订单
     * @param outTradeNo 订单编号
     * @return boolean
     */
    boolean updateOrderByOrderCode(String outTradeNo);

    /**
     * 通过用户id查询已支付的订单列表
     * @param userId 用户id
     * @return List<OrderVo>
     */
    List<OrderVo> getPayedOrderList(Integer userId);

    /**
     * 通过订单编号取消订单
     * @param orderCode String
     * @return boolean
     */
    boolean cancelOrderByCode(String orderCode);

}
