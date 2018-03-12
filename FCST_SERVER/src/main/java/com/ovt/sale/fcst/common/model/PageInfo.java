/**
 * PageInfo.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 12, 2015
 */
package com.ovt.sale.fcst.common.model;

import com.ovt.sale.fcst.common.utils.StringUtils;


/**
 * PageInfo
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class PageInfo
{

    // page no starts from 0
    private int pageNo = 0;

    private int pageSize = 10;

    public PageInfo()
    {

    }

    public PageInfo(int pageNo, int pageSize)
    {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder me = new StringBuilder();
        me.append("{pageNo: ").append(this.pageNo).append(" pageSize: ")
                .append(this.pageSize).append(" }");
        return me.toString();
    }

    public enum Order
    {
        DESC, ASC;

        public static Order toOrder(String orderStr)
        {
            if (StringUtils.equalsIgnoreCase(orderStr, DESC.toString()))
            {
                return DESC;
            }
            else
            {
                return ASC;
            }
        }
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }
}
