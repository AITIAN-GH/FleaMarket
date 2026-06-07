package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 实体类Notice
 *
 * @author AITIAN
 * @since 2023-04-28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告栏id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公告标头
     */
    @TableField("title")
    private String title;

    /**
     * 公告详情描述
     */
    @TableField("content")
    private String content;

    /**
     * 公告创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDate createTime;

    /**
     * 公告截止时间
     */
    @TableField("end_time")
    private LocalDate endTime;
}