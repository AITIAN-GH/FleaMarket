package com.xu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.system.dto.UserRequest;
import com.xu.system.pojo.User;

import java.util.HashMap;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author AITIAN
 * @since 2023-03-29
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param user UserRequest
     * @param ip String
     * @return HashMap<String,Object>
     */
    HashMap<String,Object> login(UserRequest user, String ip);

    /**
     * 用户登出
     * @param userId 用户id
     * @param userUid 用户 Uid 唯一标识
     * @return Boolean
     */
    Boolean logout(Object userId, Object userUid);

    /**
     * 用户自发修改信息
     * @param user 用户更新信息
     * @return Boolean
     */
    Boolean updateOneInfo(User user);

    /**
     * 跟新用户token
     * @param userId Object
     * @param newToken String
     * @return Boolean
     */
    Boolean refreshToken(Object userId, String newToken);

    /**
     * 重置用户密码
     * @param toEmail String
     * @param newPassword String
     * @param confirmPassword String
     * @param verifyCode String
     * @param captchaKey String
     * @return Boolean 重置用户密码结果
     */
    public Boolean resetUserPassword(String toEmail, String newPassword, String confirmPassword, String verifyCode, String captchaKey);

}
