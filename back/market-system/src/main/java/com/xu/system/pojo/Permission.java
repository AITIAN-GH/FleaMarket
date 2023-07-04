package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类Permission
 *
 * @author AITIAN
 * @since 2023-03-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 路径
     */
    @TableField("path")
    private String path;

    /**
     * 顺序
     */
    @TableField("orders")
    private Integer orders;

    /**
     * 图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 页面路径
     */
    @TableField("page")
    private String page;

    /**
     * 权限
     */
    @TableField("auth")
    private String auth;

    /**
     * 父级id
     */
    @TableField("pid")
    private Integer pid;

    /**
     * 逻辑删除
     */
    @TableField("deleted")
    @TableLogic(value = "0")
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 类型，1目录  2菜单 3按钮
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否隐藏
     */
    @TableField("hide")
    private Integer hide;
}