package com.xu.system.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author AITIAN
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class OrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    private String orderCode;

    private String productName;

    private String classimg;

    private Integer delPrice;

    private Integer bycount;

}
