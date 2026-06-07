package com.xu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.system.mapper.ClassifyMapper;
import com.xu.system.pojo.Classify;
import com.xu.system.service.ClassifyService;
import com.xu.system.vo.ClassifyVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-09
 */
@Service
public class ClassifyServiceImpl extends ServiceImpl<ClassifyMapper, Classify> implements ClassifyService {

    @Resource
    ClassifyMapper classifyMapper;

    @Override
    public List<ClassifyVo> getClassifyNames() {
        return classifyMapper.getClassifyNames();
    }
}
