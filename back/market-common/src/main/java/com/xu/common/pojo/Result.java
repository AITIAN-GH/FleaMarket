package com.xu.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接口统一返回包装类
 * @author AITIAN
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {
    private  static final long serialVersionUID = 42L;
    private static final String CODE_SUCCESS = "200";
    private static final String CODE_SYS_ERROR = "500";

    private String code;
    private Object data;
    private String msg;

    public static Result success() {
        return success(CODE_SUCCESS, null, "操作成功");
    }

    public static Result success(String msg){
        return success(CODE_SUCCESS, null, msg);
    }

    public static Result success(Object data){
        return success(CODE_SUCCESS, data, "操作成功！！");
    }

    public static Result success(Object data, String msg){
        return success(CODE_SUCCESS,data, msg);
    }

    public static Result success(String code, Object data, String msg){
        return new Result(code, data, msg);
    }

    public static Result error() {
        return error(CODE_SYS_ERROR, null, "系统错误");
    }

    public static Result error(String msg) {
        return error(CODE_SYS_ERROR, null, msg);
    }
    public static Result error(String code, String msg) {
        return error(code, null, msg);
    }

    public static Result error(Object data, String msg) {
        return new Result(CODE_SYS_ERROR, data, msg);
    }

    public static Result error(String code, Object data, String msg){
        return new Result(code, data, msg);
    }
}
