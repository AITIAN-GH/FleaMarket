package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author AITIAN
 * @since 2023-03-29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
