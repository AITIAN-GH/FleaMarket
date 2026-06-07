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
public class ClassifyVo implements Serializable {
    private Integer bid;
    private String classifyName;
}
