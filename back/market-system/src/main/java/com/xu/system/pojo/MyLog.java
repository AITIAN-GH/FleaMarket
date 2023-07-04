package com.xu.system.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类Log
 *
 * @author AITIAN
 * @since 2023-05-13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_log")
@ContentRowHeight(14)
@HeadRowHeight(20)
public class MyLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("日志编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ExcelProperty("用户名称")
    @TableField("user_name")
    private String userName;

    @ExcelProperty("登录Ip地址")
    @TableField("ip")
    private String ip;

    @ExcelProperty("最后登陆时间")
    @TableField("last_time")
    private LocalDateTime lastTime;

    @ExcelProperty("登陆状态")
    @TableField("status")
    private String status;
}