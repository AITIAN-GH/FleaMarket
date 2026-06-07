package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.system.pojo.User;
import com.xu.system.service.UserNewsService;
import com.xu.system.service.UserService;
import com.xu.system.vo.UserInfoDto;
import com.xu.system.vo.UserNews;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
*  UserController前端控制器
*
* @author AITIAN
* @since 2023-03-29
*/
@RestController
@Api(tags = "用户信息控制接口")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private UserNewsService userNewsService;

    @SaCheckLogin
    @ApiOperation("获取单用户最新消息")
    @GetMapping("/news")
    public Result getNews(@RequestParam(required = true, defaultValue = "0") Integer userId,
                          @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                          @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        PageInfo<UserNews> page = new PageInfo<UserNews>(userNewsService.getUserNewsByUserId(userId));
        HashMap<String, Object> map = new HashMap<>(4);
        map.put("news", page.getList());
        map.put("total", page.getTotal());
        return Result.success(map,Constants.SEARCH_SUCCESS);
    }


    @SaCheckPermission(value = {"user.add", "user.edit"}, mode = SaMode.OR)
    @ApiOperation("权限用户添加用户信息")
    @PutMapping("/update")
    public Result update(@RequestBody Map<String, User> data) {
        User user = data.get("params");
        boolean res = userService.updateById(user);
        if (!res) {
            String uid = UUID.randomUUID().toString().replace("-","");
            user.setUid(uid);
            user.setRole("USER");
            res = userService.save(user);
        }
        return Result.success(res);
    }

    @SaCheckLogin
    @ApiOperation("用户信息添加/最新")
    @PostMapping("/updateInfo")
    public Result updateInfo(@RequestBody User user) {
        Boolean res = userService.updateOneInfo(user);
        if (!res) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        return Result.success(Constants.OPERATE_SUCCESS);
    }

    @SaCheckLogin
    @ApiOperation("用户登陆后修改密码")
    @PostMapping("/changePass")
    public Result changePass(@RequestBody Map<String,Object> data) {
        String password = (String) data.get("password");
        String newPassword = (String) data.get("newPassword");
        String confirmPassword = (String) data.get("confirmPassword");
        User user = userService.getOne(new QueryWrapper<User>().eq("id", (int)data.get("id")));
        if (newPassword.equals(confirmPassword) && user.getPassword().equals(password)) {
            user.setPassword(newPassword);
            userService.updateById(user);
            return Result.success();
        }
        return Result.error(Constants.OPERATE_FAILED);
    }

    @SaCheckPermission("user.delete")
    @DeleteMapping("del/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean res = userService.removeById(id);
        return res ? Result.success(Constants.OPERATE_SUCCESS) : Result.success(Constants.OPERATE_FAILED);
    }

    @SaCheckPermission("user.deleteBatch")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody Map<String, List<Integer>> data) {
        List<Integer> ids = data.get("params");
        boolean b = userService.removeByIds(ids);
        return b ? Result.success(Constants.OPERATE_SUCCESS) : Result.success(Constants.OPERATE_FAILED);
    }

    @SaCheckLogin
    @ApiOperation("获取特定用户信息")
    @PostMapping
    public Result findOne(@RequestBody Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        return Result.success(new UserInfoDto(user.getId(), user.getName(), user.getEmail(), user.getAddress(), user.getAvatar(), user.getSign(), ""));
    }

    @ApiOperation("检测用户名是否已注册")
    @PostMapping("/checkname")
    public Result checkRegister(@RequestBody String username) {
        long count = userService.count(new QueryWrapper<User>().eq("username", username));
        return Result.success(count == 0,"查询成功！！");
    }

    @SaCheckRole("ADMIN")
    @SaCheckPermission("user.list")
    @ApiOperation("分页查询用户信息")
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String username,
                           @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByAsc("id");
        queryWrapper.like(!"".equals(username), "username", username);
        return Result.success(userService.page(new Page<>(pageIndex, pageSize), queryWrapper));
    }

}
