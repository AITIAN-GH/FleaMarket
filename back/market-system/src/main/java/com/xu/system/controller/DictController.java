package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.common.pojo.Result;
import com.xu.system.pojo.Dict;
import com.xu.system.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * DictController前端控制器
 *
 * @author AITIAN
 * @since 2023-03-29
 */
@RestController
@Api(tags = "数据字典信息控制接口")
@RequestMapping("/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @SaCheckRole("ADMIN")
    @ApiOperation("数据字典信息添加/更新")
    @PutMapping("/update")
    public Result update(@RequestBody Map<String, Dict> data) {
        Dict dict = data.get("params");
        boolean res;
        res = dictService.updateById(dict);
        if (!res) {
            dictService.save(dict);
        }
        return Result.success(res);
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("数据字典信息删除")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        dictService.removeById(id);
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("数据字典信息批量删除")
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody Map<String, List<Integer>> data) {
        List<Integer> ids = data.get("params");
        dictService.removeByIds(ids);
        return Result.success();
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("查询数据字典信息列表")
    @SaCheckPermission("dict.list")
    @GetMapping
    public Result findAll() {
        return Result.success(dictService.list());
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("指定字典信息")
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(dictService.getById(id));
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("分页获取数据字典信息")
    @SaCheckPermission("dict.list")
    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "") String value,
                           @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<Dict>().orderByAsc("id");
        queryWrapper.like(!"".equals(value), "value", value);
        return Result.success(dictService.page(new Page<>(pageIndex, pageSize), queryWrapper));
    }

}
