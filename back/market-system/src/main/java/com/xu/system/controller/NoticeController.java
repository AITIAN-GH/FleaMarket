package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.common.pojo.Result;
import com.xu.system.pojo.Notice;
import com.xu.system.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
*  NoticeController前端控制器
*
* @author AITIAN
* @since 2023-04-27
*/
@RestController
@Api(tags = "公告信息控制接口")
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @SaCheckPermission(value = {"notice.add", "notice.edit"}, mode = SaMode.OR)
    @PutMapping
    public Result update(@RequestBody Map<String,Notice> data) {
        Notice notice = data.get("params");
        if (notice.getId() != 0) {
            return Result.success(noticeService.updateById(notice),"更新成功！！");
        }
        notice.setId(null);
        return Result.success(noticeService.save(notice), "添加成功！！");
    }

    @ApiOperation("公告信息删除")
    @SaCheckPermission(value = {"notice.delete", "notice.deleteBatch"}, mode = SaMode.OR)
    @PostMapping("/del")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(noticeService.removeByIds(ids), "删除成功！！");
    }

    @SaCheckLogin
    @SaCheckRole("ADMIN")
    @ApiOperation("管理员公告列表查询")
    @GetMapping("/list")
    public Result selectAll(@RequestParam(defaultValue = "") String title,
                           @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByAsc("id")
                                            .like(!"".equals(title), "title", title);
        return Result.success(noticeService.page(new Page<>(pageIndex, pageSize), queryWrapper));
    }

    @ApiOperation("浏览页公告列表查询")
    @GetMapping("/all")
    public Result findPage(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                           @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        LocalDate now = LocalDate.now();
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByAsc("id")
                                            .le("create_time", now)
                                            .ge("end_time",now);
        return Result.success(noticeService.page(new Page<>(pageIndex, pageSize), queryWrapper));
    }

}
