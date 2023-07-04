package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName(value = "sys_carousel")
public class Carousel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "carousel_id", type = IdType.AUTO)
    private Integer carouselId;

    private String name;

    @TableField(value = "imgPath")
    private String imgPath;

}
