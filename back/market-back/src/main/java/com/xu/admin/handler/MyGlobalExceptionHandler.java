package com.xu.admin.handler;

import cn.dev33.satoken.exception.NotPermissionException;
import com.sun.mail.smtp.SMTPSendFailedException;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author AITIAN
 */
@SuppressWarnings("all")
@ControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result handleArithmeticException(ArithmeticException e) {
        e.printStackTrace();
        return Result.error("算数异常："+e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public Result handleNullPointerException(NullPointerException e) {
        e.printStackTrace();
        return Result.error("空指针异常:" + e.getMessage());
    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public Result handleNotPermissionException(NotPermissionException e) {
        e.printStackTrace();
        return Result.error("401","未授权异常: 你的权限不足！！");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result handleRuntimeException(Exception e) {
        e.printStackTrace();
        return Result.error(Constants.OPERATE_FAILED);
    }

    @ExceptionHandler(SMTPSendFailedException.class)
    @ResponseBody
    public Result handleSMTPSendFailedException(Exception e) {
        e.printStackTrace();
        return Result.error("邮件发送异常:" + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.error(Constants.UNKNOWN+Constants.OPERATE_FAILED);
    }

}
