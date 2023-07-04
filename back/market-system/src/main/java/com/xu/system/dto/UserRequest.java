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
public class UserRequest implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String validCode;
    private Integer flag;
}
