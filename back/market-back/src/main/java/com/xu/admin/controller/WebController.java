package com.xu.admin.controller;

import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.common.utils.IpUtil;
import com.xu.common.utils.JwtUtil;
import com.xu.common.utils.ServletUtils;
import com.xu.common.utils.StringUtils;
import com.xu.system.dto.UserRequest;
import com.xu.system.pojo.User;
import com.xu.system.service.ProductService;
import com.xu.system.service.UserService;
import com.xu.system.vo.ChartDataVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author AITIAN
 */
@SuppressWarnings("all")
@Slf4j
@Api(tags = "Root控制接口")
@RestController
public class WebController {

    @Resource
    private UserService userService;
    @Resource
    private ProductService productService;
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 访问首页，提示语
     */
    @ApiOperation("Web Description")
    @GetMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用AITIAN'S 后台管理框架，当前版本：v1.0.0，请通过前端地址访问QAQ");
    }

    @ApiOperation("登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody UserRequest user){
        String ip = IpUtil.getIpAddr(ServletUtils.getRequest());
        HashMap<String, Object> map = userService.login(user, ip);
        if (map.get(Constants.TOKEN) == null) {
            return Result.error("登陆失败！！");
        }
        return Result.success(map);
    }

    @ApiOperation("注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        // 生成 uid
        String uid = UUID.randomUUID().toString().replace("-","");
        user.setUid(uid);
        user.setName(user.getUsername());
        boolean save = userService.save(user);
        if(!save){
            log.info("注册失败！！");
            return Result.error("注册失败！！");
        }
        return Result.success("注册成功！！");
    }

    @ApiOperation("检查Token")
    @PostMapping("/checkToken")
    public Result checkToken(@RequestBody Map<String,Object> data, HttpServletRequest request){
        Object username = data.get("username");
        Object id = data.get("id");
        Object flag = data.get("flag");
        String token = request.getHeader("token");
        // 判断是否为管理页请求
        if (flag == null && id instanceof String){
            return Result.success(jwtUtil.checkUserToken((String) id, (String) username, token));
        }else {
            return Result.success(jwtUtil.checkUserToken((Integer) id, (String) username, token, (int) flag));
        }
    }

    @ApiOperation("刷新Token")
    @GetMapping("/refreshToken/{userId}")
    public Result refreshToken(@PathVariable("userId") Object userId)  {
        String newToken = null;
        HashMap<String, Object> hashMap = new HashMap<String, Object>(4);
        Boolean res = userService.refreshToken(userId, newToken);
        if (!res || newToken == null) {
            return Result.error(false,Constants.OPERATE_FAILED);
        }
        hashMap.put("newToken", newToken);
        return Result.success(hashMap, Constants.OPERATE_SUCCESS);
    }

    @ApiOperation("登出接口")
    @PostMapping("/logout")
    public Result logout(@RequestBody Map<String,Object> data)  {
        Boolean logout = userService.logout(data.get("userId"), data.get("userUid"));
        if (!logout) {
            return Result.error(Constants.FAILED);
        }
        return Result.success(Constants.LOGOUT);
    }

    @ApiOperation("商品可视化数据接口")
    @GetMapping("/EChartsData")
    public Result getChartData() {
        List<ChartDataVo> chartData = productService.getChartData();
        if (chartData == null || !(chartData.size() > 0)) {
            return Result.success(chartData,"没数别查了QAQ");
        }
        return Result.success(chartData, Constants.SEARCH_SUCCESS);
    }

    @ApiOperation("用户可视化数据接口")
    @GetMapping("/EChartsPersonData")
    public Result getChartPersonData() {
        List<ChartDataVo> chartData = productService.getChartPersonData();
        if (chartData == null || !(chartData.size() > 0)) {
            return Result.success(chartData,"没数别查了QAQ");
        }
        return Result.success(chartData, Constants.SEARCH_SUCCESS);
    }

    @ApiOperation("用户重置密码")
    @PostMapping("/resetUser")
    public Result resetUser(@RequestBody Map<String, Object> data, HttpServletRequest request, HttpServletResponse response){
        // 从 Cookie 中获取验证码 key
        String captchaKey = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("captchaKey".equals(cookie.getName())) {
                    captchaKey = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println("captchaKey = " + captchaKey);
        Object password = data.get("password");
        Object confirmPassword = data.get("confirmPassword");
        Object captcha = data.get("captcha");
        Object email = data.get("email");
        Boolean res = false;
        if (captchaKey == null) {
            return Result.error("验证码已过期！");
        }
        if (email != null && password != null && confirmPassword != null && captcha != null) {
            res = userService.resetUserPassword((String) email, (String) password, (String) confirmPassword, (String) captcha, captchaKey);
        }
        if (!res) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        // 从 Cookie 中删除验证码 key
        Cookie cookie = new Cookie("captchaKey", null);
        cookie.setMaxAge(0);
        cookie.setPath("/api/resetUser");
        response.addCookie(cookie);
        return Result.success(Constants.OPERATE_SUCCESS);
    }
}
