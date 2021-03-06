/**
 * DaoException.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * May 7, 2015
 */
package com.ovt.sale.fcst.dao.exception;

import com.ovt.sale.fcst.common.exception.OVTRuntimeException;


/**
 * DaoException
 * 
 * @Author hyson
 * @Version 1.0
 * @See
 * @Since [OVT OVALARM]/[API] 1.0
 */
public class DaoException extends OVTRuntimeException
{
    private static final long serialVersionUID = 8390021013387062726L;

    public DaoException()
    {
        super();
    }

    public DaoException(String errCode, String message)
    {
        super(errCode, message);
    }

    public DaoException(String errCode, Throwable cause)
    {
        super(errCode, cause);
    }

    public DaoException(String errCode, String message, Throwable cause)
    {
        super(errCode, message, cause);
    }
}
