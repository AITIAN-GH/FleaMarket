package com.xu.system.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author AITIAN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserNews implements Serializable {

    /**
     * 用户消息表 id
     */
    private Integer id;
    private String content;
    private String myContent;
    private String fromName;
    private String myName;
    private String proImg;
    private Integer productId;
    private Integer commentId;
    private LocalDateTime time;
    private Integer received;

}
