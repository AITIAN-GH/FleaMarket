package com.xu.system.dto;

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
public class UserNewDTO implements Serializable {
    private Integer userId;
    private String myContent;
    private String myName;
    private String fromName;
    private String proImg;
}
