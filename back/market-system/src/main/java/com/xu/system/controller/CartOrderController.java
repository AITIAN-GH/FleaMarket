package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.system.service.CartService;
import com.xu.system.service.OrderService;
import com.xu.system.vo.CartVo;
import com.xu.system.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@RestController
@Api(tags = "购物车以及订单信息控制接口")
@RequestMapping("/cart")
public class CartOrderController {

    @Resource
    private CartService cartService;
    @Resource
    private OrderService orderService;

    @SaCheckLogin
    @ApiOperation("购物车商品信息删除")
    @DeleteMapping(value = "/del")
    public Result delete(@RequestParam("id") Integer id) {
        return Result.success(cartService.deleteCartById(id),"删除成功！！");
    }

    @SaCheckLogin
    @ApiOperation("检测是否重复添加")
    @PostMapping("/one")
    public Result one(@RequestBody Map<String, Integer> data) {
        Integer userId = data.get("userId");
        Integer productId = data.get("productId");
        HashMap<String, Object> jsonMap = new HashMap<>(2);

        jsonMap.put("success",cartService.checkProductInCart(userId, productId));
        return Result.success(jsonMap,Constants.OPERATE_SUCCESS);
    }

    @SaCheckLogin
    @ApiOperation("商品添加购物车")
    @PostMapping("/add")
    public Result addToCart(@RequestBody Map<String, Integer> data) {
        Integer productId = data.get("productId");
        Object userId = data.get("userId");
        if (userId == null || userId.equals(0)) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        boolean res = cartService.addProductToCart((int)userId, productId);
        HashMap<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("success",res);
        return res ? Result.success(jsonMap, Constants.OPERATE_SUCCESS) : Result.error(jsonMap,Constants.OPERATE_FAILED);
    }

    @SaCheckLogin
    @ApiOperation("获取购物车列表")
    @PostMapping("/list")
    public Result list(@RequestBody Map<String, Integer> data) {
        Integer userId = data.get("id");
        List<CartVo> userCart = cartService.getUserCart(userId);
        HashMap<String, Object> jsonMap = new HashMap<>(4);
        jsonMap.put("list",userCart);
        jsonMap.put("count",userCart.size());
        return Result.success(jsonMap, "购物车信息查询成功！！");
    }

    @SaCheckLogin
    @ApiOperation("购物车改变商品数量")
    @PutMapping("/up")
    public Result upUserCart(@RequestBody Map<String, Map<String,Integer>> data) {
        Map<String, Integer> params = data.get("params");
        Integer cartId = params.get("cartId");
        Integer count = params.get("count");
        boolean upUserCart = cartService.upUserCart(cartId, count);
        return upUserCart ?  Result.success(true,Constants.OPERATE_FAILED) : Result.success(false, Constants.OPERATE_FAILED);
    }

    @SaCheckLogin
    @ApiOperation("购物车下订单")
    @PostMapping("/addOrder")
    public Result addOrder(@RequestBody Map<String, Object> data){
        JSONArray jsonArray = JSON.parseArray(data.get("cartIds").toString());
        List<Integer> cartIds = jsonArray.stream().map(cid -> Integer.valueOf(cid.toString())).collect(Collectors.toList());

        boolean order = orderService.addOrder(cartIds);
        if (!order) {
            return Result.error(false,Constants.OPERATE_FAILED);
        }else {
            return Result.success(true,Constants.OPERATE_SUCCESS);
        }
    }

    @SaCheckLogin
    @ApiOperation("获取用户未支付订单数据")
    @PostMapping("/getOrder")
    public Result getOrder(@RequestBody Map<String,Integer> data) {
        Integer userId = data.get("userId");
        List<OrderVo> orderList = orderService.getOrderList(userId);
        return Result.success(orderList, "订单数据加载成功！！");
    }

    @SaCheckLogin
    @ApiOperation("用户取消订单")
    @PostMapping("/cancelOrder")
    public Result cancelOrder(@RequestBody String orderCode) {
        boolean res = orderService.cancelOrderByCode(orderCode);
        return res ? Result.success(Constants.OPERATE_SUCCESS) : Result.error(Constants.OPERATE_FAILED);
    }

    @SaCheckLogin
    @ApiOperation("获取用户已支付订单数据")
    @PostMapping("/getPayed")
    public Result getPayed(@RequestBody Map<String,Integer> data) {
        Integer userId = data.get("userId");

        List<OrderVo> orderList = orderService.getPayedOrderList(userId);
        return Result.success(orderList, "订单数据加载成功！！");
    }

}
