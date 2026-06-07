package com.xu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.system.pojo.Permission;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AITIAN
 * @since 2023-03-29
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 通过 uid 获取用户权限列表
     * @param uid Integer
     * @return List<com.xu.system.pojo.Permission>
     */
    List<Permission> getPermissionsByUid(Integer uid);

    /**
     * 通过 roleId 获取用户权限列表
     * @param roleId Integer
     * @return List<com.xu.system.pojo.Permission>
     */
    List<Permission> getPermissionsByRoleId(Integer roleId);

    /**
     * 保存角色菜单树权限信息
     * @param roleId 角色id
     * @param permissions 权限信息
     */
    void saveRolePermissions(Integer roleId, List<Integer> permissions);

    /**
     * 删除之前的角色权限
     * @param roleId 角色id
     */
    void delPrePermissions(Integer roleId);
}
