package com.xu.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.system.dto.ProductRequest;
import com.xu.system.pojo.Product;
import com.xu.system.vo.ChartDataVo;
import com.xu.system.vo.ProductVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AITIAN
 * @since 2023-04-08
 */

public interface ProductService extends IService<Product> {

    /**
     * 获取分页信息
     * @return  List<Product>
     * */
    public List<Product> getProductPage();

    /**
     * 通过用户id 查询用户商品信息
     * @param uid 用户id
     * @param name 查询名称
     * @return List<ProductVo>
     */
    public List<ProductVo> getProducts(Integer uid, String name);

    /**
     * 有条件获取分页信息
     * @param wrapper QueryWrapper<Product>
     * @return  List<Product>
     * */
    public List<Product> getProductPage(QueryWrapper<Product> wrapper);

    /**
     * 获取单个商品信息
     * @param productId 商品id
     * @return  Product 返回单个商品信息
     * */
    public Product getOneProduct(int productId);

    /**
     * 用户添加商品
     * @param productRequest ProductRequest
     * @return Boolean
     */
    public Boolean addUserProduct(ProductRequest productRequest);

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
    void removeUserProduct(int userId, List<Integer> ids);
}
