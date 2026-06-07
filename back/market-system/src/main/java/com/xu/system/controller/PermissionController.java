package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.common.pojo.RouteNode;
import com.xu.system.commons.PermissionList;
import com.xu.system.pojo.Permission;
import com.xu.system.service.PermissionService;
import com.xu.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * PermissionController前端控制器
 *
 * @author AITIAN
 * @since 2023-03-29
 */
@RestController
@Api(tags = "权限信息控制接口")
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;
    @Resource
    private UserService userService;

    @SaCheckRole("ADMIN")
    @ApiOperation("权限信息新增/修改")
    @PostMapping
    public Result save(@RequestBody Permission permission) {
        Permission realPermission = permissionService.getById(permission.getId());
        if (realPermission == null) {
            permissionService.save(permission);
        } else {
            permissionService.updateById(permission);
        }
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("角色权限信息绑定/更新")
    @PostMapping("/saveRoleMenu")
    public Result saveRoleMenu(@RequestBody Map<String, Object> data) {
        List<Integer> permissions = (List<Integer>) data.get("menuIds");
        Integer roleId = (Integer) data.get("roleId");
        if (roleId == null) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        permissionService.delPrePermissions(roleId);
        permissionService.saveRolePermissions(roleId, permissions);
        return Result.success(Constants.OPERATE_SUCCESS);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("删除指定权限信息")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean res = permissionService.removeById(id);
        if (!res) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("批量删除指定权限信息")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean res = permissionService.removeByIds(ids);
        if (!res) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("获取权限信息列表")
    @GetMapping
    public Result findAll() {
        List<Permission> list = permissionService.list();
        List<RouteNode> permissionTree = PermissionList.getPermissionTree(list);
        return Result.success(permissionTree);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("分页查询权限信息列表")
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<Permission>().orderByAsc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(permissionService.page(new Page<>(pageIndex, pageSize), queryWrapper));
    }

}
