package com.xu.system.dto;

import com.xu.system.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author AITIAN
 */
@Data
@AllArgsConstructor
public class ProductRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private Integer proId;
    private Integer userId;
}
