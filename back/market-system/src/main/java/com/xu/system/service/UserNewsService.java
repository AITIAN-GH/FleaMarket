package com.xu.system.service;

import com.xu.system.pojo.Comment;
import com.xu.system.vo.UserNews;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author AITIAN
 */
public interface UserNewsService {

    /**
     * 通过 userId 获取最新消息
     * @param userId int
     * @return List<UserNew>
     */
    List<UserNews> getUserNewsByUserId(int userId);

    /**
     * 添加评论至用户未读列表
     * @param userId Integer
     * @param commentId Integer
     * @return Boolean
     */
    Integer addUserNews(@Param("userId") Integer userId, @Param("commentId") Integer commentId);

    /**
     * 处理用户新评论
     * @param comment Comment
     * @return Result
     */
    public Boolean handlerNewComment(Comment comment);

    /**
     * 通过确定收到消息
     * @param ids List<Integer>
     * @return Boolean
     */
    Boolean updateUserNewsByIds(List<Integer> ids);

}
