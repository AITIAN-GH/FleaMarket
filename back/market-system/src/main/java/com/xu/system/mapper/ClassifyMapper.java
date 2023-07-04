package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.Classify;
import com.xu.system.vo.ClassifyVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-09
 */
@Mapper
public interface ClassifyMapper extends BaseMapper<Classify> {

    /**
     * 或如商品分类名称
     * @return List<ClassifyVo>
     */
    public List<ClassifyVo> getClassifyNames();
}
