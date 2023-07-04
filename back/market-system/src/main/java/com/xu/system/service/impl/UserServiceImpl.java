package com.xu.system.service.impl;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.RouteNode;
import com.xu.common.utils.JwtUtil;
import com.xu.common.utils.RedisUtil;
import com.xu.system.commons.AsyncManager;
import com.xu.system.commons.PermissionList;
import com.xu.system.dto.UserRequest;
import com.xu.system.mapper.UserMapper;
import com.xu.system.pojo.User;
import com.xu.system.service.PermissionService;
import com.xu.system.service.UserService;
import com.xu.system.vo.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author AITIAN
 * @since 2023-03-29
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PermissionService permissionService;
    @Resource
    private AsyncManager asyncManager;

    @Override
    public HashMap<String, Object> login(UserRequest user, String ip) {
        long startTime = System.currentTimeMillis();
        User realUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        HashMap<String, Object> map = new HashMap<>(4);
        String currentSite;
        if ("".equals(user.getValidCode()) || user.getValidCode() == null) {
            currentSite ="商品页登录, 登录状态：";
        }else {
            currentSite = "管理页登录, 登录状态：";
        }
        if (realUser != null) {
            if (user.getPassword().equals(realUser.getPassword())) {
                if ("".equals(user.getValidCode()) || user.getValidCode() == null) {
                    String token = JwtUtil.createToken(realUser.getName(), realUser.getId());
                    log.info("商品页登录");
                    map.put("id", realUser.getId());
                    map.put("username", realUser.getName());
                    map.put("token", token);
                    StpUtil.login(realUser.getId(), SaLoginConfig.setToken(token));
                    redisUtil.set("userTokenBefore:" + realUser.getId(), token, 72 * 60 * 60);
                } else {
                    String token = JwtUtil.createToken(realUser.getName(), realUser.getUid());
                    List<RouteNode> permissionList = PermissionList.getPermissionTree(permissionService.getPermissionsByUid(realUser.getId()));
                    map.put("user", new UserInfoDto(realUser.getId(), realUser.getUid(), realUser.getName(), realUser.getEmail(), realUser.getAddress(), realUser.getAvatar(), realUser.getSign(), "housebroken"));
                    map.put("permissionList", permissionList);
                    map.put("token", token);
                    StpUtil.login(realUser.getUid(), SaLoginConfig.setToken(token));
                    redisUtil.set("userTokenBack:" + realUser.getUid(), token, 72 * 60 * 60);
                }
                asyncManager.execute(asyncManager.recordLoginInfo(realUser, currentSite + Constants.LOGIN_SUCCESS, ip));
            }else {
                asyncManager.execute(asyncManager.recordLoginInfo(realUser, currentSite + Constants.LOGIN_FAILED, ip));
            }
            log.info("登录花费时间 {}ms", System.currentTimeMillis() - startTime);
        }
        return map;
    }

    @Override
    public Boolean logout(Object userId, Object userUid) {
        if (userId != null) {
            redisUtil.del("userTokenBefore:" + (int)userId);
        }else if (userUid != null) {
            log.info("用户token{}登出",userUid);
            StpUtil.logout(userUid);
            redisUtil.del("userTokenBack:" + (String) userUid);
        }else {
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateOneInfo(User user) {
        User one = userMapper.selectOne(new QueryWrapper<User>().eq("id", user.getId()));
        if (one == null) {
            return false;
        }
        one.setName(user.getName());
        one.setEmail(user.getEmail());
        one.setAddress(user.getAddress());
        one.setSign(user.getSign());
        one.setAvatar(user.getAvatar());
        System.out.println("one = " + one);
        int i = userMapper.updateById(one);
        return i != 0;
    }

    @Override
    public Boolean refreshToken(Object userId, String newToken) {
        if (userId instanceof Integer) {
            Object beforeToken = redisUtil.get("userTokenBefore:" + (int) userId);
            if (beforeToken == null) {
                return false;
            }
            newToken = (String) beforeToken;
        }else if(userId instanceof String){
            Object afterToken = redisUtil.get("userTokenBack:" + userId);
            if (afterToken == null) {
                return false;
            }
            newToken = (String) afterToken;
        }else {
            return false;
        }
        return true;
    }

    @Override
    public Boolean resetUserPassword(String toEmail, String newPassword, String confirmPassword, String verifyCode, String captchaKey) {

        // 从 Redis 中获取验证码文本
        String realCaptcha = (String) redisUtil.get(captchaKey);
        if (realCaptcha == null || realCaptcha.trim().isEmpty()) {
            return false;
        }
        // 比较用户输入的验证码文本和 Redis 中存储的验证码文本是否一致
        if (!verifyCode.equalsIgnoreCase(realCaptcha)) {
            return false;
        }
        // 加密 确认密码 对比传输途中是否被篡改
        String cpw = DigestUtils.md5DigestAsHex(new StringBuilder(
                DigestUtils.md5DigestAsHex(confirmPassword.getBytes())).reverse().toString().getBytes());
        if (cpw.equals(newPassword)) {
            User realUser = userMapper.selectOne(new QueryWrapper<User>().eq(!toEmail.isEmpty(),"email", toEmail));
            if (realUser == null) {
                return false;
            }
            // 第一个参数为要更新的实体，这里为 null 表示更新时不需要使用实体
            userMapper.update(null, new UpdateWrapper<User>().eq("id", realUser.getId()).set("password", cpw));
        }
        // 从 Redis 中删除验证码
        asyncManager.execute(() -> redisUtil.del(realCaptcha));
        return true;
    }


}
