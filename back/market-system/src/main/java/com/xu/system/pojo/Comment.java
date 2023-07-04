package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 实体类Comment
*
* @author AITIAN
* @since 2023-04-19
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论信息
     */
    @TableField("content")
    private String content;

    /**
     * 评论时间
     */
    @TableField("time")
    private LocalDateTime time;

    /**
     * 评论者ID
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 产品ID
     */
    @TableField("product_id")
    private Integer productId;

    /**
     * 父评论ID
     */
    @TableField("pid")
    private Integer pid;

    /**
     * 逻辑删除字段
     */
    @TableLogic(value = "deleted")
    private Integer deleted;
}