package com.xu.system.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 订单Order实体类
 *
 * @author AITIAN
 * @since 2023-03-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@TableName("sys_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    private String orderCode;

    private Integer userId;

    private Integer productId;

    private Integer bycount;

    private String state;

    @TableLogic(value = "0",delval = "1")
    private Integer deleted;

}
