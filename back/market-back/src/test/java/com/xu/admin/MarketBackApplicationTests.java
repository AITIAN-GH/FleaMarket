package com.xu.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.common.pojo.Constants;
import com.xu.system.mapper.OrderMapper;
import com.xu.system.mapper.ProductMapper;
import com.xu.system.pojo.Notice;
import com.xu.system.pojo.Order;
import com.xu.system.pojo.Product;
import com.xu.system.service.NoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MarketBackApplicationTests {

    @Resource
    OrderMapper orderMapper;
    @Resource
    ProductMapper productMapper;
    @Resource
    NoticeService noticeService;

    @Test
    public void test1(){
        //
        String outTradeNo = "1684682183851";
        List<Order> orders = orderMapper.selectAll(outTradeNo);
        Map<Integer, Integer> changeMap = orders.stream().collect(Collectors.toMap(Order::getProductId, Order::getBycount));
        // 提取changeMap中的键为List 来用作Mybatis plus查询
        List<Integer> ids = new ArrayList<>(changeMap.keySet());
        System.out.println("ids = " + ids);
        if (ids.size() > 0) {
            System.out.println("有值");
            List<Product> products = productMapper.selectList(new QueryWrapper<Product>().in("id", ids));
            System.out.println("products = " + products);
        }
//        products.forEach(product -> {
//            Integer byCount = changeMap.get(product.getId());
//            if(product.getStock() > 0 && !(product.getStock() < byCount)){
//                product.setStock(product.getStock() - byCount);
//            }else {
//                System.out.println("出错了！！");
//            }
//        });
    }

    @Test
    public void test2(){
        String outTradeNo = "1684662972639";
        boolean b = orderMapper.cancelOrderByCode(outTradeNo);
        System.out.println("products = " + b);
    }

    @Test
    public void test3(){
        String outTradeNo = "1684724572088";
        List<Order> list = orderMapper.selectAll(outTradeNo);
        list.forEach(order -> {
            order.setState(Constants.PAYED_SUCCESS);
            order.setDeleted(1);
            int i = orderMapper.updateById(order);
        });
    }

    @Test
    public void test4(){
        LocalDate now = LocalDate.now();
        QueryWrapper<Notice> queryWrapper = new QueryWrapper<Notice>().orderByAsc("id")
                                            ;
        List<Notice> list = noticeService.list(queryWrapper);
        System.out.println("list = " + list);
    }

}
