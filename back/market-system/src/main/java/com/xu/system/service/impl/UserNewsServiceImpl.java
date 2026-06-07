package com.xu.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.xu.system.commons.AsyncManager;
import com.xu.system.commons.PageUtil;
import com.xu.system.dto.UserNewDTO;
import com.xu.system.mapper.UserNewsMapper;
import com.xu.system.pojo.Comment;
import com.xu.system.pojo.UserNew;
import com.xu.system.service.CommentService;
import com.xu.system.service.UserNewsService;
import com.xu.system.vo.UserNews;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author AITIAN
 */
@Service
public class UserNewsServiceImpl implements UserNewsService {

    @Resource
    private UserNewsMapper userNewsMapper;
    @Resource
    private CommentService commentService;
    @Resource
    private AsyncManager asyncManager;

    @Override
    public List<UserNews> getUserNewsByUserId(int userId) {
        PageUtil.startPage();
        return userNewsMapper.getUserNewsByUserId(userId);
    }

    @Override
    public Integer addUserNews(Integer userId, Integer commentId) {
        try {
            UserNew userNew = new UserNew(null, userId, commentId, 0);
            userNewsMapper.addUserNews(userNew);
            return userNew.getUserId() != 0 ? userNew.getId() : null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean updateUserNewsByIds(List<Integer> ids) {
        try {
            for (Integer id : ids) {
                userNewsMapper.userReceiveNews(id);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean handlerNewComment(Comment comment){
        UserNewDTO und;
        if( comment.getPid() != 0 ){
            und = userNewsMapper.getSocketNewDataByPid(comment.getUserId(), comment.getPid());
            return noticeUserNews(und, comment, true);
        }else {
            boolean save = commentService.save(comment);
            if (!save) {
                return false;
            }
            // 如果是自己发的就不通知
            und = userNewsMapper.getSocketNewDataByProductId(comment.getUserId(), comment.getProductId());
            return noticeUserNews(und, comment, false);
        }
    }

    public Boolean noticeUserNews(UserNewDTO und, Comment comment, Boolean flag){
        if (!und.getUserId().equals(comment.getUserId())) {
            if (flag) {
                boolean save = commentService.save(comment);
                if (!save) {
                    return false;
                }
            }
            Integer insertId = addUserNews(und.getUserId(), comment.getId());
            if (insertId == null || insertId == 0) {
                throw new RuntimeException();
            }
            // 封装 socket 数据并通知
            UserNews userNews = new UserNews(insertId, comment.getContent(), und.getMyContent(), und.getFromName(), und.getMyName(), und.getProImg(), comment.getProductId(), comment.getId(), comment.getTime(), 0);
            asyncManager.execute(asyncManager.newCommentNotice(und.getMyName(), JSON.toJSONString(userNews)));
            return true;
        }
        return false;
    }
}
