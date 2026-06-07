package com.xu.system.service;

import com.alipay.api.AlipayApiException;
import com.xu.common.pojo.AliPay;

import java.util.Map;

/**
 * @author AITIAN
 */
public interface AliPayService {

    /**
     * 阿里云支付服务
     * @param aliPay AliPay
     * @return String
     */
    public String beforePay(AliPay aliPay);

    /**
     * 支付验签调服务
     * @param params Map<String, String>
     * @return Boolean
     * @throws AlipayApiException 支付验签异常
     */
    public Boolean payCheck(Map<String, String> params) throws AlipayApiException;

}
