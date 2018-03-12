/**
 * PageResult.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年7月9日
 */
package com.ovt.sale.fcst.common.model;

import java.util.List;

/**
 * PageResult
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PageResult<T>
{
    // 总记录数
    private int totalRecodes;

    // 返回的当前页码
    private int pageNo;

    // 每页需要展示的记录数
    private int pageSize;

    // 当前页的查询结果集
    List<T> results;

    /**
     * @return the totalRecodes
     */
    public int getTotalRecodes()
    {
        return totalRecodes;
    }

    /**
     * @param totalRecodes the totalRecodes to set
     */
    public void setTotalRecodes(int totalRecodes)
    {
        this.totalRecodes = totalRecodes;
    }

    /**
     * @return the pageNo
     */
    public int getPageNo()
    {
        return pageNo;
    }

    /**
     * @param pageNo the pageNo to set
     */
    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize()
    {
        return pageSize;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    /**
     * @return the results
     */
    public List<T> getResults()
    {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List<T> results)
    {
        this.results = results;
    }

}
