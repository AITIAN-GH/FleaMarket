package com.xu.system.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.xu.common.pojo.Constants;
import com.xu.common.pojo.Result;
import com.xu.common.utils.RedisUtil;
import com.xu.system.dto.ProductRequest;
import com.xu.system.pojo.Collection;
import com.xu.system.pojo.Comment;
import com.xu.system.pojo.Product;
import com.xu.system.service.CollectionService;
import com.xu.system.service.CommentService;
import com.xu.system.service.ProductService;
import com.xu.system.service.UserNewsService;
import com.xu.system.vo.CollectionVo;
import com.xu.system.vo.CommentVo;
import com.xu.system.vo.ProductVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
*  ProductController前端控制器
*
* @author AITIAN
* @since 2023-04-08
*/
@SuppressWarnings("all")
@RestController
@Api(tags = "商品信息控制接口")
@RequestMapping("/product")
public class ProductController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Resource
    private ProductService productService;
    @Resource
    private CollectionService collectionService;
    @Resource
    private CommentService commentService;
    @Resource
    private UserNewsService userNewsService;
    @Resource
    private RedisUtil redisUtil;

    @SaCheckPermission(value = {"pro.edit", "pro.add"}, mode = SaMode.OR)
    @ApiOperation("商品信息添加/修改")
    @PutMapping("/update")
    public Result update(@RequestBody Map<String,ProductRequest> data) {
        ProductRequest productRequest = data.get("params");
        Boolean res = productService.addUserProduct(productRequest);
        if (!res) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        return Result.success(Constants.OPERATE_SUCCESS);
    }

    @SaCheckPermission(value = {"pro.delete", "pro.deleteBatch"}, mode = SaMode.OR)
    @ApiOperation("商品信息删除并删除用户关联")
    @PostMapping("/del")
    @Transactional
    public Result deleteBatch(@RequestBody Map<String,Object> data) {
        List<Integer> ids = (List<Integer>) data.get("delIds");
        int userId = (int) data.get("userId");
        productService.removeByIds(ids);
        productService.removeUserProduct(userId, ids);
        return Result.success();
    }

    @ApiOperation("单个商品信息查询")
    @GetMapping("/one")
    public Result one(@RequestParam("id") int bookId) {
        Product product = (Product) redisUtil.get("product:" + bookId);
        if (product == null) {
            product = productService.getOneProduct(bookId);
            redisUtil.set("product:" + bookId, product, 60);
        }
        return Result.success(product,"商品数据查询成功！！");
    }

    /**
     * 通过名称搜索商品
     */
    @GetMapping("/name/{name}")
    public Result searchByName(@PathVariable("name") String name) {
        List<Product> productList;
        HashMap<String, Object> jsonMap = new HashMap<>(4);
        if (name != null && !"".equals(name)) {
            productList = productService.list(new QueryWrapper<Product>().like("product_name", name));
            PageInfo<Product> p = new PageInfo<>(productList);
            jsonMap.put("productList",productList);
            jsonMap.put("total",p.getTotal());
        }
        return Result.success(jsonMap,"查询成功！！");
    }

    @ApiOperation("获取单个商品评论")
    @GetMapping("/comment/one")
    public Result getOneCommentById(@RequestParam(value = "productId", required = false, defaultValue = "1") Integer productId,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                    @RequestParam(required = false, defaultValue = "8") Integer pageSize) throws IOException {
        Object res = redisUtil.get("productComment:" + productId + ":" + pageIndex + ":pageSize" + pageSize);
        List<CommentVo> list;
        long total;
        if (res == null) {
            list = commentService.getAllCommentByProductId(productId);
            redisUtil.set("productComment:" + productId + ":" + pageIndex + ":pageSize" + pageSize, OBJECT_MAPPER.writeValueAsString(list), 5);
        }else {
            list = OBJECT_MAPPER.readValue((String) res, new TypeReference<List<CommentVo>>(){});
        }
        PageInfo<CommentVo> p = new PageInfo<>(list);
        Object proTotal = redisUtil.get("productComment:" + productId+ ":total");
        if (proTotal == null) {
            total = p.getTotal();
            redisUtil.set("productComment:" + productId+ ":total", total, 5);
        }else {
            total = (int) proTotal;
        }
        HashMap<String, Object> jsonMap = new HashMap<>(4);
        jsonMap.put("commentList", p.getList());
        jsonMap.put("total",total);
        return Result.success(jsonMap,"商品评论查询成功！！");
    }

    @ApiOperation("商品评论删除")
    @SaCheckPermission(value = {"comment.delete", "comment.deleteBatch"}, mode = SaMode.OR)
    @PostMapping("/comment/del")
    public Result getOneCommentById(@RequestBody Map<String, Object> data) {
        int productId = (int) data.get("productId");
        Object delIdsObj = data.get("delIds");
        List<Integer> delIds = delIdsObj instanceof List ? (List<Integer>) delIdsObj : Collections.singletonList((Integer) delIdsObj);
        Boolean removeComment = commentService.removeComment(productId, delIds);
        return Result.success(removeComment,"商品评论查询成功！！");
    }

    @ApiOperation("商品评论添加并通知用户")
    @PostMapping("/comment")
    @Transactional(rollbackFor = RuntimeException.class)
    public Result addComment(@RequestBody Map<String,Object> data) {
        Object ppid = data.get("pid");
        int pid = ppid == null ? 0 : (int) ppid;
        Comment comment = new Comment(null, (String) data.get("content"), LocalDateTime.now(),
                            (int) data.get("userId"), (int) data.get("productId"), pid, null);

        Boolean res = userNewsService.handlerNewComment(comment);
        return res ? Result.error(true,"评论成功！！"): Result.error(false,"评论失败！！");
    }

    @SaCheckLogin
    @ApiOperation("用户收到消息确认")
    @PostMapping("/comment/receive")
    public Result commentReceive(@RequestBody Map<String,Object> data) throws IOException {
        Object userNewId = data.get("userNewId");
        int userId = (int) data.get("userId");
        Object ids = data.get("userNewIds");
        List<Integer> upIds = new ArrayList<>();
        if ( userNewId != null && (int)userNewId != 0 ) {
            upIds.add((int)userNewId);
        }else {
            if (ids != null) {
                upIds = (List<Integer>) ids;
            }
        }
        boolean res = false;
        if (upIds.size() > 0) {
            res = userNewsService.updateUserNewsByIds(upIds);
        }
        return res ? Result.success("更新成功！！"): Result.error("更新失败！！");
    }

    @ApiOperation("指定分类信息查询")
    @GetMapping("/search")
    public Result search(@RequestParam(required = false) Integer classify,
                         @RequestParam(required = false, defaultValue = "1") int pageIndex,
                         @RequestParam(required = false, defaultValue = "8") int pageSize) {
        List<Product> productList;
        int total = -1;
        if (classify != null) {
            productList = (List<Product>) redisUtil.get("Products:"+classify+":"+pageIndex+":"+pageSize);
            Object o = redisUtil.get("Products:" + classify + ":" + pageIndex + ":" + pageSize + ":total");
            if (o!= null){
                total = (int) o;
            }
            if (productList == null || !(total > 0) ) {
                productList = productService.getProductPage(new QueryWrapper<Product>().eq("classid",classify));
                PageInfo<Product> p = new PageInfo<>(productList);
                total = (int) p.getTotal();
                redisUtil.set("Products:"+classify+":"+pageIndex+":"+pageSize, productList,600);
                redisUtil.set("Products:"+classify+":"+pageIndex+":"+pageSize+":total", total,600);
            }
        }else {
            productList = (List<Product>) redisUtil.get("homeProducts");
            Object o = redisUtil.get("homeProducts:total");
            if (o!= null){
                total = (int) o;
            }
            if (productList == null) {
                productList = productService.getProductPage();
                PageInfo<Product> p = new PageInfo<>(productList);
                total = (int) p.getTotal();
                redisUtil.set("homeProducts", productList, 600);
                redisUtil.set("homeProducts:total", total, 600);
            }
        }

        HashMap<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("productList",productList);
        jsonMap.put("total",total);
        return Result.success(jsonMap,"首页数据查询成功！！");
    }

    @SaCheckLogin
    @ApiOperation("用户个人收藏信息查询")
    @GetMapping("/collection")
    public Result collection(@RequestParam(required = false) Integer userId,
                             @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                             @RequestParam(required = false, defaultValue = "8") Integer pageSize) {
        List<CollectionVo> collections = collectionService.getCollectionsByUserId(userId);
        PageInfo<CollectionVo> p = new PageInfo<>(collections);
        HashMap<String, Object> jsonMap = new HashMap<>(4);
        jsonMap.put("productList",collections);
        jsonMap.put("total",p.getTotal());
        return Result.success(jsonMap,"收藏商品信息查询成功！！");
    }


    @SaCheckLogin
    @DeleteMapping("/collection/del")
    public Result delCollection(@RequestParam("uid") Integer uid,@RequestParam("pid") Integer pid) {
        boolean res = collectionService.remove(new QueryWrapper<Collection>().eq("user_id", uid).eq("product_id", pid));
        HashMap<String, Object> jsonMap = new HashMap<>(4);
        jsonMap.put("success",res);
        return Result.success(jsonMap,"信息修改成功！！");
    }

    @SaCheckLogin
    @PostMapping("/collection/one")
    public Result one(@RequestBody Map<String, Integer> data) {
        HashMap<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("success",collectionService.checkProductInCollections(data.get("userId"), data.get("productId")));
        return Result.success(jsonMap,"数据查询成功！！");
    }

    @SaCheckLogin
    @PostMapping("/collection/add")
    public Result addToCollection(@RequestBody Map<String, Integer> data) {
        Object userId = data.get("userId");
        if (userId == null || userId.equals(0)) {
            return Result.error(Constants.OPERATE_FAILED);
        }
        Collection collection = new Collection((int)userId, data.get("productId"));
        HashMap<String, Object> jsonMap = new HashMap<>(2);
        jsonMap.put("success",collectionService.save(collection));
        return Result.success(jsonMap,Constants.OPERATE_SUCCESS);
    }

    @GetMapping("/all")
    public Result findProductsById(@RequestParam(defaultValue = "") String name,
                                   @RequestParam Integer id,
                                   @RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                                   @RequestParam(required = false, defaultValue = "8") Integer pageSize) {
        HashMap<String, Object> hashMap = new HashMap<>(4);
        PageInfo<ProductVo> pageInfo = new PageInfo<>(productService.getProducts(id, name));
        hashMap.put("tableData", pageInfo.getList());
        hashMap.put("total",pageInfo.getTotal());
        return Result.success(hashMap);
    }
}
