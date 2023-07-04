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
public class CommentVo implements Serializable {

    private Integer id;
    private String content;
    private String time;
    private String fromName;
    private String toName;
    private Integer pid;

}
