package com.xu.system.controller;

import com.xu.common.pojo.Result;
import com.xu.common.utils.RedisUtil;
import com.xu.system.pojo.Carousel;
import com.xu.system.service.CarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@RestController
@Api(tags = "轮播图信息接口")
@RequestMapping("/carousel")
public class CarouselController {

    @Resource
    private CarouselService carouselService;
    @Resource
    private RedisUtil redisUtil;

    @ApiOperation("轮播图列表")
    @GetMapping
    public Result carousel(){
        List<Carousel> carouselList = (List<Carousel>)redisUtil.get("carouselList");
        if (carouselList == null) {
            carouselList = carouselService.list();
            redisUtil.set("carouselList",carouselList);
        }
        return Result.success(carouselList,"轮播图加载成功");
    }
}
