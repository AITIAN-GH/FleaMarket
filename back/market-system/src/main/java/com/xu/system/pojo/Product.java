package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* 实体类Product
*
* @author AITIAN
* @since 2023-04-08
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("product_name")
    private String productName;

    @TableField("product_author")
    private String productAuthor;

    @TableField("classimg")
    private String classImg;

    @TableField("pro_price")
    private Integer proPrice;

    @TableField("del_price")
    private Integer delPrice;

    @TableField("classid")
    private Integer classid;

    @TableField("stock")
    private Integer stock;

    @TableLogic(value = "0",delval = "1")
    private int deleted;

}