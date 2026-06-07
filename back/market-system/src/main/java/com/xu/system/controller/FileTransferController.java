package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xu.common.pojo.FileTransferProperties;
import com.xu.common.pojo.Result;
import com.xu.common.utils.MyCellStyleStrategy;
import com.xu.system.pojo.MyLog;
import com.xu.system.service.MyLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.util.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FileTransferController 文件传输控制器
 *
 * @author AITIAN
 */
@SuppressWarnings("all")
@RestController
@Api(tags = "文件传输信息控制接口")
@RequestMapping("/file")
public class FileTransferController {

    @Resource
    private FileTransferProperties fileTransferProperties;

    @Autowired
    private MyLogService myLogService;

    /**
     * 上传新图片，并返回文件访问地址
     */
    @ApiOperation("商品信息封面上传")
    @PostMapping("/upload/proImg")
    public Result uploadProductFile(@RequestParam("file") MultipartFile file) throws IOException {
        // 确定新文件全路径
        return getResult(file, fileTransferProperties.getProductImageHome());
    }

    /**
     * 上传新头像，并返回文件访问地址
     */
    @ApiOperation("用户头像上传")
    @PostMapping("/upload/avatar")
    public Result uploadAvatarFile(@RequestParam("file") MultipartFile file) throws IOException {
        return getResult(file, fileTransferProperties.getAvatarHome());
    }

    @SaCheckRole("ADMIN")
    @ApiOperation("分页查询系统日志")
    @GetMapping("/systemlog")
    public Result getSystemLog(@RequestParam(defaultValue = "") String value,
                               @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                               @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        QueryWrapper<MyLog> queryWrapper = new QueryWrapper<MyLog>().orderByAsc("id");
        queryWrapper.like(!"".equals(value), "value", value);
        return Result.success(myLogService.page(new Page<>(pageIndex, pageSize), queryWrapper),"日志信息查询成功！！");
    }

    @SaCheckLogin
    @SaCheckRole("ADMIN")
    @ApiOperation("系统日志下载")
    @GetMapping("/systemlog/download")
    public void downloadSystemLog(@RequestParam(required = false) List<Integer> ids , HttpServletResponse response) throws IOException {
        ByteArrayOutputStream baos = null;
        List<MyLog> myLogs = null;
        try {
            if (ids != null && ids.size() > 0) {
                myLogs = myLogService.list(new QueryWrapper<MyLog>().in("id", ids));
            }else {
                myLogs = myLogService.list();
            }
            // 设置文件名
            String fileName = "AITIAN_SYSLOG_"+ System.currentTimeMillis() +".xlsx";

            // 将 设置响应头信息
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName,"UTF-8"));
            response.setContentType("application/octet-stream");

            // ********* 创建一个基于内存缓存的 excel 文件 *********
            baos = new ByteArrayOutputStream();
            EasyExcel.write(baos, MyLog.class)
                    .registerWriteHandler(MyCellStyleStrategy.getMyCellStyleStrategy())
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet("系统日志")
                    .doWrite(myLogs);
            // Excel 文件转换为字节数组
            byte[] data = baos.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(data);

            // 返回响应数据
            response.setContentLength(data.length);
            IOUtils.copy(resource.getInputStream(), response.getOutputStream());

        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        } finally {
            baos.flush();
            baos.close();
        }
    }

    @NotNull
    private Result getResult(@RequestParam("file") MultipartFile file, String uploadPath) throws IOException {

        File path = new File(uploadPath);
        // 获取文件名
        String filename = file.getOriginalFilename();

        // 检查存储路径是否存在 不存在就创建
        boolean flag = false;
        if (!path.exists()) {
            if (!path.mkdirs()) {
                flag = true;
            }
        }
        if (flag){
            return Result.error("上传失败！！");
        }

        // 图片相同名称不覆盖 修改前缀
        File newFile = new File(uploadPath + File.separator +filename);
        if (newFile.exists()) {
            if (!filename.isEmpty()) {
                String suffix = filename.substring(filename.lastIndexOf("."));
                filename = filename.substring(0, filename.lastIndexOf("."))
                        + "-" + System.currentTimeMillis() + suffix;
                newFile = new File(uploadPath + File.separator + filename);
            }
        }
        file.transferTo(newFile);
        String filePath = uploadPath.substring(uploadPath.lastIndexOf('/'));
        return Result.success(filePath+ "/" +filename,"文件上传成功 ！!");

    }

}
