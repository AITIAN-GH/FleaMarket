package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.xu.common.pojo.Result;
import com.xu.common.utils.WebSocket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author AITIAN
 */
@Slf4j
@Api(tags = "WS通信接口")
@RestController
@RequestMapping("/api")
public class WebSocketController {

    @Resource
    private WebSocket webSocket;

    @SaCheckRole("ADMIN")
    @ApiOperation("WS公共通知")
    @GetMapping("/sendAllWebSocket")
    public Result sendAllWebSocket(String content) {
        webSocket.sendAllMessage(content);
        return Result.success("websocket群体发送！");
    }

    @SaCheckLogin
    @ApiOperation("WS单用户的发送信息")
    @GetMapping("/sendOneWebSocket")
    public Result sendOneWebSocket(@RequestParam("username") String username,
                                   @RequestParam("message") String message) {
        log.info("接收者{}的信息即将发送", username);
        log.info("发送的信息为：{}", message);
        webSocket.sendOneMessage(username, message);
        return Result.success("websocket单人发送");
    }

}
