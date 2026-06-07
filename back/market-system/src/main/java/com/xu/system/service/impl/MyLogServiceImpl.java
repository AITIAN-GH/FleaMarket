package com.xu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.system.mapper.MyLogMapper;
import com.xu.system.pojo.MyLog;
import com.xu.system.service.MyLogService;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 *
 * @author AITIAN
 * @since 2023-05-13
 */
@Service
public class MyLogServiceImpl extends ServiceImpl<MyLogMapper, MyLog> implements MyLogService {

}
