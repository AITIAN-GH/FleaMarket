package com.xu.system.commons;

import cn.hutool.core.convert.Convert;
import com.github.pagehelper.PageHelper;
import com.xu.common.pojo.PageDomain;
import com.xu.common.utils.ServletUtils;

/**
 * @author AITIAN
 */
public class PageUtil extends PageHelper {

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_INDEX = "pageIndex";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    public static PageDomain proPage() {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageIndex(Convert.toInt(ServletUtils.getParameter(PAGE_INDEX), 1));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 3));
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE,true));
        return pageDomain;
    }


    /**
     * 设置请求分页数据
     */
    public static void startPage() {
        PageDomain pageDomain = proPage();
        Integer pageIndex = pageDomain.getPageIndex();
        Integer pageSize = pageDomain.getPageSize();
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageIndex, pageSize).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage() {
        PageHelper.clearPage();
    }
}
