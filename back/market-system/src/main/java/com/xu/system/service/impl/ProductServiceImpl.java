package com.xu.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.system.commons.PageUtil;
import com.xu.system.dto.ProductRequest;
import com.xu.system.mapper.ProductMapper;
import com.xu.system.mapper.UserMapper;
import com.xu.system.pojo.Product;
import com.xu.system.pojo.User;
import com.xu.system.service.ProductService;
import com.xu.system.vo.ChartDataVo;
import com.xu.system.vo.ProductVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品服务实现类
 *
 * @author AITIAN
 * @since 2023-04-08
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    private static final String ROLE_ADMIN = "ADMIN";

    @Resource
    private ProductMapper productMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public List<ProductVo> getProducts(Integer uid, String name) {
        User user = userMapper.selectById(uid);
        PageUtil.startPage();
        if (name == null || "".equals(name)) {
            if (ROLE_ADMIN.equals(user.getRole())) {
                System.out.println("管理员查询中...");
                return productMapper.getProductsByAdmin();
            }
            return productMapper.getProductsById(uid);
        }else if (ROLE_ADMIN.equals(user.getRole())) {
            System.out.println("管理员通过商品名查询中...");
            return productMapper.getProductsByAdminAndName(name);
        }
        return productMapper.getProductsByIdAndName(uid, name);
    }

    @Override
    public List<Product> getProductPage() {
        PageUtil.startPage();
        return productMapper.selectList(null);
    }

    @Override
    public List<Product> getProductPage(QueryWrapper<Product> wrapper) {
        PageUtil.startPage();
        return productMapper.selectList(wrapper);
    }

    @Override
    public Product getOneProduct(int productId) {
        return productMapper.selectById(productId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean addUserProduct(ProductRequest productRequest) {
        Integer productId = productRequest.getProId();
        Product product = productRequest.getProduct();
        if (productId == 0) {
            product.setId(null);
            // 商品添加入数据库
            int insert = productMapper.insert(product);
            productMapper.addNewProductToUser(product.getId(), productRequest.getUserId());
            return insert != 0;
        }else {
            product.setId(productId);
            int i = productMapper.updateById(product);
            return i != 0;
        }
    }

    @Override
    public List<ChartDataVo> getChartData() {
        return productMapper.getChartData();
    }

    @Override
    public List<ChartDataVo> getChartPersonData() {
        return productMapper.getChartPersonData();
    }

    @Override
    public void removeUserProduct(int userId, List<Integer> ids) {
        productMapper.removeUserProduct(userId,  ids);
    }
}
