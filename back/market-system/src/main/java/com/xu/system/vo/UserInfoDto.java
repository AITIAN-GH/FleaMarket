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
public class UserInfoDto implements Serializable {
    private Integer id;
    private String uid;
    private String name;
    private String email;
    private String address;
    private String avatar;
    private String sign;
    private String token;

    public UserInfoDto(Integer id, String name, String email, String address, String avatar, String sign, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.avatar = avatar;
        this.sign = sign;
        this.token = token;
    }
}
