package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xu.common.utils.LocalDateConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 实体类User
 *
* @author AITIAN
* @since 2023-03-29
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 昵称
     */
    @TableField("name")
    private String name;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 用户唯一id
     */
    @TableField("uid")
    private String uid;

    /**
     * 逻辑删除 0存在  id删除
     */
    @TableField("deleted")
    @TableLogic(value = "0")
    private Integer deleted;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateConfig.LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateConfig.LocalDateDeSerializer.class)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateConfig.LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateConfig.LocalDateDeSerializer.class)
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 头像
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 角色
     */
    @TableField("role")
    private String role;

    /**
     * 个性签名
     */
    @TableField("sign")
    private String sign;
}