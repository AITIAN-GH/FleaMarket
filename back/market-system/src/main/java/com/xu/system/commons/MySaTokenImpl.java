package com.xu.system.commons;

import cn.dev33.satoken.stp.StpInterface;
import com.xu.system.mapper.PermissionMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author AITIAN
 * 自定义权限验证接口扩展
 */
@Component
public class MySaTokenImpl implements StpInterface {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        System.out.println("权限码集合");
        return permissionMapper.getUserPermissions((String) loginId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        System.out.println("角色标识集合");
        if (loginId instanceof Integer ) {
            return permissionMapper.getUserRoleById((int) loginId);
        }
        return permissionMapper.getUserRole((String) loginId);
    }
}
