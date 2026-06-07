package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.common.utils.RedisUtil;
import com.xu.system.pojo.Classify;
import com.xu.system.service.ClassifyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassifyController前端控制器
 *
 * @author AITIAN
 * @since 2023-04-09
 */
@RestController
@Api(tags = "商品分类信息接口")
@RequestMapping("/classify")
public class ClassifyController {

    @Resource
    private ClassifyService classifyService;
    @Resource
    private RedisUtil redisUtil;

    @SaCheckRole("ADMIN")
    @ApiOperation("商品分类信息添加/修改")
    @PostMapping
    public Result save(@RequestBody Classify classify) {
        boolean flag = false;
        if (classify == null) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        if (classify.getBid() == 0 ) {
            classify.setBid(null);
            flag = classifyService.save(classify);
        }else {
            flag = classifyService.updateById(classify);
        }
        return flag ? Result.success(Constants.OPERATE_SUCCESS) : Result.error(Constants.OPERATE_FAILED);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("商品分类信息删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean res = classifyService.removeById(id);
        return res ? Result.success(Constants.OPERATE_SUCCESS): Result.error(Constants.OPERATE_FAILED);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("商品分类信息批量删除")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        boolean res = classifyService.removeByIds(ids);
        return res ? Result.success(Constants.OPERATE_SUCCESS): Result.error(Constants.OPERATE_FAILED);
    }

    @ApiOperation("获取所有分类的分类名")
    @GetMapping
    public Result getAllClassifyName() {
        return Result.success(classifyService.getClassifyNames());
    }

    @ApiOperation("获取所有分类的分类信息")
    @GetMapping("/all")
    public Result getAllCategory() {
        List<Classify> allCategory = (List<Classify>) redisUtil.get("allCategory");
        if (allCategory == null) {
            allCategory = classifyService.list();
            redisUtil.set("allCategory", allCategory, 10 * 60);
        }
        return Result.success(allCategory, "查询成功");
    }

    @ApiOperation("分页查找分类信息")
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        QueryWrapper<Classify> queryWrapper = new QueryWrapper<Classify>().orderByAsc("bid");
        queryWrapper.like(!"".equals(name), "classify", name);
        return Result.success(classifyService.page(new Page<>(pageIndex, pageSize), queryWrapper));
    }

}
