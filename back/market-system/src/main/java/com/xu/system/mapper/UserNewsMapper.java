package com.xu.system.mapper;

import com.xu.system.dto.UserNewDTO;
import com.xu.system.pojo.UserNew;
import com.xu.system.vo.UserNews;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author AITIAN
 */
@Mapper
public interface UserNewsMapper {

    /**
     * 通过 userId 获取最新消息
     * @param userId int
     * @return List<UserNew>
     */
    List<UserNews> getUserNewsByUserId(int userId);

    /**
     * 获取 socket 返回所需的数据
     * @param userId 用户id - Integer
     * @param pid 父评论id - Integer
     * @return UserNewDTO
     */
    UserNewDTO getSocketNewDataByPid(@Param("userId") Integer userId, @Param("pid") Integer pid);

    /**
     * 非回复评论获取 socket 返回所需的数据
     * @param userId 用户id - Integer
     * @param productId 商品id - Integer
     * @return UserNewDTO
     */
    UserNewDTO getSocketNewDataByProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    /**
     * 添加评论至用户未读列表
     * @param userNew UserNew
     */
    void addUserNews(UserNew userNew);

    /**
     * 用户确认收到信息
     * @param id Integer
     */
    void userReceiveNews(Integer id);

}
