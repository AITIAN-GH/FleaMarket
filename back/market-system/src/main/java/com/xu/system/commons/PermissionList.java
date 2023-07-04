package com.xu.system.commons;

import com.xu.common.pojo.RouteNode;
import com.xu.system.pojo.Permission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AITIAN
 */
public class PermissionList {

    /**
     * 生成菜单树
     * @param permissions 所有菜单信息
     * @return List<RouteNode>
     */
    public static List<RouteNode> getPermissionTree(List<Permission> permissions) {
        List<RouteNode> rootPermissions=permissions.stream()
                                        .filter(p -> p.getPid() == 0)
                                        .map(permission -> {
                                            return new RouteNode(permission.getId(), permission.getName(), permission.getPath(), permission.getAuth(), permission.getIcon(), permission.getPage(), permission.getHide(), permission.getOrders(), permission.getDeleted());
                                        }).collect(Collectors.toList());
        for (RouteNode rootPermission : rootPermissions) {
            rootPermission.setChildren(getChildrenPermissions(rootPermission, permissions));
        }
        return rootPermissions;
    }

    /**
     * 递归获取子节点
     *
     * @param parentPermission 父菜单
     * @param permissions  所有菜单信息
     * @return List<RouteNode>
     */
    private static List<RouteNode> getChildrenPermissions(RouteNode parentPermission, List<Permission> permissions) {
        List<RouteNode> childrenPermissions = permissions.stream()
                .filter(permission -> permission.getPid().equals(parentPermission.getId()))
                .map(permission -> {
                    return new RouteNode(permission.getId(), permission.getName(), permission.getPath(), permission.getAuth(), permission.getIcon(), permission.getPage(), permission.getHide(), permission.getOrders(), permission.getDeleted());
                }).collect(Collectors.toList());
        for (RouteNode routeNode : childrenPermissions) {
            routeNode.setChildren(getChildrenPermissions(routeNode, permissions));
        }
        return childrenPermissions;
    }
}
