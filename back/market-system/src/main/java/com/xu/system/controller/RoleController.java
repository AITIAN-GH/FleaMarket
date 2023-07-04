package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.system.dto.RoleDto;
import com.xu.system.pojo.Permission;
import com.xu.system.pojo.Role;
import com.xu.system.service.PermissionService;
import com.xu.system.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
*  RoleController前端控制器
*
* @author AITIAN
* @since 2023-03-29
*/
@RestController
@Api(tags = "角色信息控制接口")
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;
    @Resource
    private PermissionService permissionService;

    @SaCheckRole("ADMIN")
    @ApiOperation("角色信息添加/修改")
    @PutMapping("/update")
    public Result update(@RequestBody Map<String, Role> data) {
        Role role = data.get("params");
        boolean res;
        res = roleService.updateById(role);
        if (!res) {
            roleService.save(role);
        }
        return Result.success(res);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("角色信息删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean b = roleService.removeById(id);
        permissionService.delPrePermissions(id);
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("角色信息批量删除")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody Map<String, List<Integer>> data) {
        List<Integer> ids = data.get("params");
        boolean b = roleService.removeByIds(ids);
        return b ? Result.success(Constants.OPERATE_SUCCESS) : Result.error(Constants.OPERATE_FAILED);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("获取角色列表")
    @GetMapping
    public Result findAll() {
        List<Role> roleList = roleService.list();
        List<RoleDto> roles = roleList.stream().map(role -> new RoleDto(role.getId(), role.getName(), role.getFlag(), role.getDeleted())).collect(Collectors.toList());
        return Result.success(roles);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("获取角色名称列表(用于修改用户角色)")
    @GetMapping("/allNames")
    public Result getAllNames() {
        List<Role>  roleList = roleService.list();
        List<String> roleNameList = roleList.stream().map(Role::getFlag).collect(Collectors.toList());
        return Result.success(roleNameList);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("角色权限列表")
    @GetMapping("/{roleId}")
    public Result getPermissionsByRoleId(@PathVariable Integer roleId) {
        List<Permission> permissionList = permissionService.getPermissionsByRoleId(roleId);
        return Result.success(permissionList);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("分页获取角色列表")
    @SaCheckPermission("role.list")
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "2") Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>().orderByAsc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(roleService.page(new Page<>(pageIndex, pageSize), queryWrapper));
    }
}
