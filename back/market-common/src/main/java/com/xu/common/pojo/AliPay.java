package com.xu.common.pojo;

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
public class AliPay implements Serializable {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
