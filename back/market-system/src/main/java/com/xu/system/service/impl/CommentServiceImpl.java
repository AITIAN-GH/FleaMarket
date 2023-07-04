package com.xu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.system.commons.PageUtil;
import com.xu.system.mapper.CommentMapper;
import com.xu.system.pojo.Comment;
import com.xu.system.service.CommentService;
import com.xu.system.vo.CommentVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-19
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<CommentVo> getAllCommentByProductId(int productId) {
        PageUtil.startPage();
        return commentMapper.getAllCommentByProductId(productId);
    }

    @Override
    public Boolean removeComment(int productId, List<Integer> delIds) {
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("product_id",productId));
        // 若是回复的父评论被删除 pid 置为 0
        List<Comment> newList = commentList.stream().filter(commentVo ->
            delIds.stream().anyMatch(delId -> delId.equals(commentVo.getPid())
        )).peek(commentVo -> commentVo.setPid(0)).collect(Collectors.toList());
        for (Comment comment : newList) {
            commentMapper.updateById(comment);
        }
        commentMapper.deleteBatchIds(delIds);
        return null;
    }
}
