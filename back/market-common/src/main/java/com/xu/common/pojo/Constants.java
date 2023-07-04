package com.xu.common.pojo;

import org.springframework.stereotype.Component;

/**
 * 通用常量信息
 *
 * @author AITIAN
 */
@Component
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * JSON 格式
     */
    public static final String FORMAT_JSON = "JSON";

    /**
     * 加密格式
     */
    public static final String SIGN_TYPE = "RSA2";

    /**
     * 返回表单格式
     */
    public static String RETURN_FORM = "text/html;charset=UTF-8";

    /**
     * 操作延迟10毫秒
     */
    public static final int OPERATE_DELAY_TIME = 10;

    /**
     * 查询成功
     */
    public static final String SEARCH_SUCCESS = "查询成功！！";

    /**
     * 操作成功
     */
    public static final String OPERATE_SUCCESS = "操作成功！！";

    /**
     * 操作失败
     */
    public static final String OPERATE_FAILED = "操作失败！！";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Login Success";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAILED = "Login Failed";

    /**
     * 注销
     */
    public static final String LOGOUT = "Success Logout";

    /**
     * 出错了
     */
    public static final String FAILED = "SomeThing Error";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 分隔符
     */
    public static final String SEPARATOR = ",";

    /**
     * 未知
     */
    public static final String UNKNOWN = "unknown";

    /**
     * 交易状态
     */
    public static final String TRADE_STATUS = "trade_status";

    /**
     * 交易成功
     */
    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static final String PAYED_SUCCESS = "已支付";

}
