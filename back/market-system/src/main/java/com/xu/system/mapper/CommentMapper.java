package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.Comment;
import com.xu.system.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-19
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 通过 产品id 查询所有评论
     * @param productId 产品id
     * @return List<Comment>
     */
    public List<CommentVo> getAllCommentByProductId(int productId);

}
