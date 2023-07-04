package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.Order;
import com.xu.system.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 下订单
     * @param order 订单对象
     */
    public void addOrder(Order order);

    /**
     * 获取用户订单信息
     * @param userId 用户id
     * @return List<CartBo>
     */
    public List<OrderVo> getOrderList(Integer userId);

    /**
     * 通过订单编号修改订单状态
     * @param orderCode 订单编号
     * @return List<Order>
     */
    public List<Order> selectOrderByOrderCode(String orderCode);

    /**
     * 查询所有未支付订单
     * @param outTradeNo 订单编号
     * @return List<Order>
     */
    List<Order> selectAll(String outTradeNo);

    /**
     * 通过用户id查询已支付的订单列表
     * @param userId 用户id
     * @return List<OrderVo>
     */
    List<OrderVo> getPayedOrderList(Integer userId);

    /**
     * 通过订单编号取消订单
     * @param orderCode String
     * @return int
     */
    boolean cancelOrderByCode(String orderCode);

}
