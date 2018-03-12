/**
 * UserServiceImpl.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package com.ovt.sale.fcst.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.sale.fcst.common.utils.DateTimeUtils;
import com.ovt.sale.fcst.common.utils.EncryptionUtils;
import com.ovt.sale.fcst.common.utils.StringUtils;
import com.ovt.sale.fcst.dao.UserDao;
import com.ovt.sale.fcst.dao.UserTokenDao;
import com.ovt.sale.fcst.dao.vo.User;
import com.ovt.sale.fcst.dao.vo.UserToken;
import com.ovt.sale.fcst.service.context.SessionContext;
import com.ovt.sale.fcst.service.exception.ServiceErrorCode;
import com.ovt.sale.fcst.service.exception.ServiceException;

/**
 * UserServiceImpl
 * 
 * @Author hyson
 * @Version 1.0
 * @See
 * @Since [OVT OVALARM]/[API] 1.0
 */
@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserTokenDao userTokenDao;

    @Autowired
    private AppPropertiesService appProperties;

    public String userLogin(String userCode, String password)
            throws ServiceException
    {
        User user = null;

        user = userDao.getUserByCode(userCode);
        if (user == null)
        {
            throw new ServiceException(ServiceErrorCode.USER_NOT_EXIST,
                    "User is not exist");
        }
        if (!StringUtils.equals(EncryptionUtils.encrypt(password),
                user.getPassword()))
        {
            throw new ServiceException(
                    ServiceErrorCode.WRONG_PASSWORD_USERCODE,
                    "User code or password is wrong");
        }
        SessionContext.setCurrentUser(user);

        String accessToken = generateUserToken(user.getId());

        return accessToken;
    }

    public void logout(String accessToken) throws ServiceException
    {
        if (!StringUtils.isBlank(accessToken))
        {
            userTokenDao.delete(accessToken);
        }
    }

    public User getUserByAccessToken(String accessToken)
            throws ServiceException
    {
        if (StringUtils.isBlank(accessToken))
        {
            return null;
        }
        long userId = userTokenDao.getUserByToken(accessToken);
        if (userId == 0)
        {
            throw new ServiceException(ServiceErrorCode.INVALID_ACCESS_TOKEN,
                    "Invalid access token!!");
        }
        User user = userDao.getUserById(userId);
        return user;
    }

    private String generateUserToken(long userId)
    {
        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setToken(EncryptionUtils.generateUUID());
        userToken.setExpireTime(new Timestamp(DateTimeUtils.addSeconds(
                new java.util.Date(), appProperties.getCookieAccessTokenAge())
                .getTime()));

        userTokenDao.save(userToken);
        return userToken.getToken();
    }

    public void cleanExpiredUserToken() throws ServiceException
    {
        userTokenDao.deleteExpiredUserToken();
    }

}
