package com.xu.system.service.impl;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.common.pojo.AliPay;
import com.xu.common.pojo.Constants;
import com.xu.common.utils.AlipayConfig;
import com.xu.system.mapper.OrderMapper;
import com.xu.system.mapper.ProductMapper;
import com.xu.system.pojo.Order;
import com.xu.system.pojo.Product;
import com.xu.system.service.AliPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author AITIAN
 */
@SuppressWarnings("all")
@Service
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private AlipayConfig aliPayConfig;
    @Resource
    OrderMapper orderMapper;
    @Resource
    ProductMapper productMapper;

    @Override
    public String beforePay(AliPay aliPay) {
        // 构建支付页面所需元素
        String form = null;
        // 构建原子性标志元素
        AtomicBoolean flag = new AtomicBoolean(true);
        // 获取自定义的订单编号
        String payTraceNo = aliPay.getTraceNo();
        List<Order> orders = orderMapper.selectAll(payTraceNo);
        // 获取每个商品修改数量
        Map<Integer, Integer> changeMap = orders.stream().collect(Collectors.toMap(Order::getProductId, Order::getBycount));
        List<Integer> proIds = orders.stream().map(Order::getProductId).collect(Collectors.toList());
        List<Product> products = productMapper.selectList(new QueryWrapper<Product>().in("id", proIds));
        products.forEach(product -> {
            Integer byCount = changeMap.get(product.getId());
            // 库存不大于零 或者小于 购买数量报错
            if(!(product.getStock() > 0) || product.getStock() < byCount){
                flag.set(false);
            }
        });
        if (flag.get()) {
            // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
            AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.getGateway(), aliPayConfig.getAppId(),
                    aliPayConfig.getAppPrivateKey(), Constants.FORMAT_JSON, Constants.UTF8, aliPayConfig.getAlipayPublicKey(), Constants.SIGN_TYPE);

            // 2. 创建 Request并设置Request参数
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            // 设置同步回调地址
            request.setNotifyUrl(aliPayConfig.getNotifyUrl());
            request.setReturnUrl(aliPayConfig.getReturnUrl());

            JSONObject bizContent = new JSONObject();
            // 自己生成的订单编号
            bizContent.set("out_trade_no", payTraceNo);
            // 订单的总金额
            bizContent.set("total_amount", aliPay.getTotalAmount());
            // 支付的名称
            bizContent.set("subject", aliPay.getSubject());
            // 固定配置
            bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
            request.setBizContent(bizContent.toString());

            // 执行请求，拿到响应的结果，返回给浏览器
            try {
                // 调用SDK生成表单
                form = alipayClient.pageExecute(request).getBody();
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        }

        return form;
    }

    @Override
    public Boolean payCheck(Map<String, String> params) throws AlipayApiException {
        String signType = params.get("sign_type");
        // 验证签名
        boolean checkSignature = AlipaySignature.rsaCheckV1(params,  aliPayConfig.getAlipayPublicKey(), "UTF-8", signType);
        //查看参数都有哪些
        System.out.println(params);
        System.out.println("checkSignature => " + checkSignature);
        return checkSignature;
    }

}
