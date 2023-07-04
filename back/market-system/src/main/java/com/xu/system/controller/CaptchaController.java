package com.xu.system.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.common.utils.MailUtil;
import com.xu.common.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 * @author AITIAN
 */
@RestController
@Api(tags = "验证码息接口")
@RequestMapping("/captcha")
public class CaptchaController {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private MailUtil mailUtil;

    @ApiOperation("生成图片验证码")
    @GetMapping("/createImg")
    public void createCaptchaImg(HttpServletRequest request, HttpServletResponse response) {
        // 设置响应头信息
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 80, 4, 4);
        // 生成验证码文本
        String capText = captcha.getCode();

        // 将验证码文本存入 Redis
        String key = "captcha:" + UUID.randomUUID().toString();
        redisUtil.set(key, capText, 30);

        //将验证码 key 存入 Cookie 中
        Cookie cookie = new Cookie("captchaKey", key);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        ServletOutputStream out = null;
        // 生成验证码图片并输出到客户端
        BufferedImage image = captcha.getImage();
        try {
            out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            out.flush();
            out.close();
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @ApiOperation("生成文本验证码并邮箱通知")
    @GetMapping("/create")
    public Result createCaptcha(HttpServletResponse response, @RequestParam String toEmail) {
        // 生成验证码文本
        String capText = CaptchaUtil.createShearCaptcha(200, 80, 4, 4).getCode();

        try {
            mailUtil.sendSimpleMail(toEmail, "AITIAN 管理系统重置密码", "AITIAN 管理系统验证码是："+ capText);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        // 将验证码文本存入 Redis
        String key = "captcha:" + UUID.randomUUID().toString();
        redisUtil.set(key, capText, 60);

        //将验证码 key 存入 Cookie 中
        Cookie cookie = new Cookie("captchaKey", key);
        cookie.setPath("/api/resetUser");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        return Result.success(Constants.OPERATE_SUCCESS);
    }

}
