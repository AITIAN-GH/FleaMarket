package com.xu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.system.pojo.Product;
import com.xu.system.vo.ChartDataVo;
import com.xu.system.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-08
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 管理员查询
     * @return List<ProductVo>
     */
    public List<ProductVo> getProductsByAdmin();

    /**
     * 管理员通过商品名字查询
     * @param name 查询名称
     * @return List<ProductVo>
     */
    public List<ProductVo> getProductsByAdminAndName(String name);

    /**
     * 通过用户id查询出售的商品
     * @param uid 用户id
     * @return List<ProductVo>
     */
    public List<ProductVo> getProductsById(Integer uid);

    /**
     * 通过用户id 商品名称 查询用户商品信息
     * @param uid 用户id
     * @param name 查询名称
     * @return List<ProductVo>
     */
    List<ProductVo> getProductsByIdAndName(@Param("userId") Integer uid,@Param("proName") String name);

    /**
     * 绑定用户与商品的关系
     * @param productId int
     * @param userId int
     */
    void addNewProductToUser(@Param("productId") int productId,@Param("userId") int userId);

    /**
     * 通过用户商品关系表 Id 查询商品id
     * @param upId int
     * @return int
     */
    Integer getProIdByUpId(Integer upId);

    /**
     * 获取 EChat 数据表数据
     * @return Map<String,Integer>
     */
    List<ChartDataVo> getChartData();

    /**
     * 获取 EChat 数据表数据
     * @return Map<String,Integer>
     */
    List<ChartDataVo> getChartPersonData();

    /**
     * 通过 用户id和删除商品id 删除用户与商品关联关系
     * @param userId int
     * @param ids List<Integer>
     */
    void removeUserProduct(@Param("userId") int userId,@Param("ids") List<Integer> ids);

}
