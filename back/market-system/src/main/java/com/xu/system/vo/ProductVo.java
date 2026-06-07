package com.xu.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author AITIAN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVo implements Serializable {
    private Integer id;
    private String productName;
    private String productAuthor;
    private String classImg;
    private Integer proId;
    private Integer proPrice;
    private Integer delPrice;
    private Integer stock;
    private String classify;
}
