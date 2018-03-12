/**
 * Condition.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年7月20日
 */
package com.ovt.sale.fcst.common.model;

/**
 * Condition
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
public class Condition
{
    private String name;

    private String value;

    /**
     * 0:int 1:String 2:Timestamp 3:float 4:double
     */
    private int dataType;

    @Override
    public String toString()
    {
        return "SearchCondition [name=" + name + ", value=" + value
                + ", dataType=" + dataType + "]";
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @return the dataType
     */
    public int getDataType()
    {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }

}
