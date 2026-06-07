package com.xu.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.system.mapper.PermissionMapper;
import com.xu.system.pojo.Permission;
import com.xu.system.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AITIAN
 * @since 2023-03-29
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> getPermissionsByUid(Integer userId) {
        return permissionMapper.getPermissionsByUid(userId);
    }

    @Override
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        return permissionMapper.getPermissionsByRoleId(roleId);
    }

    @Override
    public void saveRolePermissions(Integer roleId, List<Integer> permissions) {
        permissionMapper.saveRolePermissions(roleId, permissions);
    }

    @Override
    public void delPrePermissions(Integer roleId) {
        permissionMapper.delPrePermissions(roleId);
    }
}
