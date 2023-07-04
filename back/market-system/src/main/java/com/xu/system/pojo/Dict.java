package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* 实体类Dict
 *
* @author AITIAN
* @since 2023-03-29
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 编码
     */
    @TableField("code")
    private String code;

    /**
     * 内容
     */
    @TableField("value")
    private String value;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 删除
     */
    @TableField("deleted")
    @TableLogic(value = "0")
    private Integer deleted;
}