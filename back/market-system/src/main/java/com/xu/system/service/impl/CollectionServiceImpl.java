package com.xu.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.system.mapper.CollectionMapper;
import com.xu.system.pojo.Collection;
import com.xu.system.service.CollectionService;
import com.xu.system.vo.CollectionVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author AITIAN
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

    @Resource
    CollectionMapper collectionMapper;

    @Override
    public List<CollectionVo> getCollectionsByUserId(Integer userId) {
        return collectionMapper.getCollectionsByUserId(userId);
    }

    @Override
    public Boolean checkProductInCollections(Integer userId, Integer productId) {
        String s = collectionMapper.checkProductInCart(userId, productId);
        return s != null && !"".equals(s);
    }
}
