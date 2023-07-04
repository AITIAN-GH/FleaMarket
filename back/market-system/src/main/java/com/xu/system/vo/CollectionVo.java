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
public class CollectionVo implements Serializable {
    private Integer id;
    private String productName;
    private String productAuthor;
    private String classImg;
    private String comments;
}
