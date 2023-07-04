package com.xu.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.system.pojo.Comment;
import com.xu.system.vo.CommentVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-19
 */
public interface CommentService extends IService<Comment> {

    /**
     * 通过 产品id 查询所有评论
     * @param productId 产品id
     * @return List<Comment>
     */
    public List<CommentVo> getAllCommentByProductId(int productId);

    /**
     * 删除评论
     * @param productId 商品id
     * @param delIds 评论id
     * @return Boolean
     */
    Boolean removeComment(int productId, List<Integer> delIds);

}
