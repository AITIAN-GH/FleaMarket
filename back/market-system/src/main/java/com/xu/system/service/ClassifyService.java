package com.xu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.system.pojo.Classify;
import com.xu.system.vo.ClassifyVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-09
 */
public interface ClassifyService extends IService<Classify> {

    /**
     * 或如商品分类名称
     * @return List<ClassifyVo>
     */
    public List<ClassifyVo> getClassifyNames();
}
