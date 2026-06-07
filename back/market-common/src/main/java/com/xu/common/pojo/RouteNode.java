package com.xu.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AITIAN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteNode {
    private Integer id;
    private String name;
    private String path;
    private String auth;
    private String icon;
    private String page;
    private Integer hide;
    private Integer orders;
    private Integer deleted;
    private List<RouteNode> children;

    public RouteNode(Integer id, String name, String path, String auth, String icon, String page, Integer hide, Integer orders, Integer deleted) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.auth = auth;
        this.icon = icon;
        this.page = page;
        this.hide = hide;
        this.orders = orders;
        this.deleted = deleted;
        this.children = new ArrayList<>();
    }
}