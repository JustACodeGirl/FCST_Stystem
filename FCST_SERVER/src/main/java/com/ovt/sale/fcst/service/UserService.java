/**
 * UserService.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package com.ovt.sale.fcst.service;

import com.ovt.sale.fcst.dao.vo.User;
import com.ovt.sale.fcst.service.exception.ServiceException;


/**
 * 
 * @author jinzhong.zheng
 *
 */

public interface UserService
{
    String userLogin(String userCode, String password) throws ServiceException;

    void logout(String accessToken) throws ServiceException;

    User getUserByAccessToken(String accessToken) throws ServiceException;

    void cleanExpiredUserToken() throws ServiceException;

}
