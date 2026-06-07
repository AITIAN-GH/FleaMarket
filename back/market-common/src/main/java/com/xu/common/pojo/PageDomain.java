package com.xu.common.pojo;

import com.xu.common.utils.StringUtils;

/**
 * 分页数据
 * @author AITIAN
 */
public class PageDomain {
    /**
     * 当前记录起始索引
     */
    private Integer pageIndex;

    /**
     * 每页显示记录数
     */
    private Integer pageSize;

    /**
     * 分页参数合理化
     */
    private Boolean reasonable = true;


    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Boolean getReasonable() {
        if (StringUtils.isNull(reasonable)) {
            return Boolean.TRUE;
        }
        return reasonable;
    }

    public void setReasonable(Boolean reasonable) {
        this.reasonable = reasonable;
    }
}
