package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.Collection;
import com.xu.system.vo.CollectionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author AITIAN
 * @since 2023-03-07
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

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
     * @return bookName String
     */
    public String checkProductInCart(@Param("userId") int userId, @Param("productId") int productId);
}
