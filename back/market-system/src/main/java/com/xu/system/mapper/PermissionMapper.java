package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AITIAN
 * @since 2023-03-29
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 通过 uid 获取用户权限列表
     * @param userId Integer
     * @return List<com.xu.system.pojo.Permission>
     */
    List<Permission> getPermissionsByUid(Integer userId);

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
    void saveRolePermissions(@Param("roleId") Integer roleId, @Param("permissions") List<Integer> permissions);

    /**
     * 删除之前的角色权限
     * @param roleId 角色id
     */
    void delPrePermissions(Integer roleId);

    /**
     * 通过用户id获取用户权限列表
     * @param userUid 用户id
     * @return List<String>
     */
    List<String> getUserPermissions(String userUid);

    /**
     * 通过用户Uid获取用户角色
     * @param userUid 用户id
     * @return List<String>
     */
    List<String> getUserRole(String userUid);

    /**
     * 通过用户id获取用户角色
     * @param userId 用户id
     * @return List<String>
     */
    List<String> getUserRoleById(int userId);
}
