package com.xu.admin.controller;

import com.xu.common.pojo.AliPay;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.system.service.AliPayService;
import com.xu.system.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AITIAN
 */
@RestController
@Api(tags = "支付接口")
@RequestMapping("/alipay")
public class AliPayController {

    @Resource
    private OrderService orderService;
    @Resource
    private AliPayService aliPayService;

    @ApiOperation("支付接口")
    @GetMapping("/pay")
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
        String fromStr = aliPayService.beforePay(aliPay);
        if (fromStr != null && !fromStr.isEmpty()) {
            httpResponse.setContentType(Constants.RETURN_FORM);
            // 直接将完整的表单html输出到页面
            httpResponse.getWriter().write(fromStr);
        }else {
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.getWriter().write(Constants.OPERATE_FAILED +"原因：抢购商品库存不足，请检查购物车！！");
        }
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @ApiIgnore
    @PostMapping("/notify")
    public Result payNotify(HttpServletRequest request) throws Exception {
        boolean res = false;
        if (Constants.TRADE_SUCCESS.equals(request.getParameter(Constants.TRADE_STATUS))) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>(16);
            Map<String, String[]> requestParams = request.getParameterMap();

            for (String name : requestParams.keySet()) {
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用
                valueStr = new String(valueStr.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                params.put(name, valueStr);
            }
            // 支付宝验签
            if (aliPayService.payCheck(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单状态及商品库存
                res = orderService.updateOrderByOrderCode(params.get("out_trade_no"));
            }
        }
        return res ? Result.success(Constants.OPERATE_SUCCESS) : Result.error(Constants.OPERATE_FAILED);
    }

}


