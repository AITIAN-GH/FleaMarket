package com.xu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.system.pojo.Collection;
import com.xu.system.vo.CollectionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-09
 */
public interface CollectionService extends IService<Collection> {

    /**
     * 用过用户id 查询收藏的商品
     * @param userId 用户id
     * @return List
     */
    public List<CollectionVo> getCollectionsByUserId(Integer userId);

    /**
     * 判断商品是否已经在收藏夹中
     * @param userId 用户id
     * @param productId 商品id
     * @return boolean
     */
    public Boolean checkProductInCollections(Integer userId, Integer productId);
}
