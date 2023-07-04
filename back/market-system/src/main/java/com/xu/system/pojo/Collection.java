package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@TableName(value = "sys_collection")
public class Collection implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId("user_id")
    private Integer userId;
    private Integer productId;
}
