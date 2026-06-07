package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* <p>
* 实体类Classify
* </p>
* @author AITIAN
* @since 2023-04-09
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_classify")
public class Classify implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "bid",type = IdType.AUTO)
    private Integer bid;

    @TableField("classify")
    private String classify;

    @TableField("path")
    private String path;
}