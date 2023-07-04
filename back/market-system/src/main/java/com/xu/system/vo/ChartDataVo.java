package com.xu.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* <p>
* 实体类Product
* </p>
* @author AITIAN
* @since 2023-05-06
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartDataVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String classname;
    private Integer classcount;

}