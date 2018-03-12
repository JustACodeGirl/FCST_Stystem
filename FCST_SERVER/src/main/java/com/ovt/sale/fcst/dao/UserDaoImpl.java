/**
 * UserDaoImpl.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年6月30日
 */
package com.ovt.sale.fcst.dao;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.sale.fcst.common.utils.DaoRowMapper;
import com.ovt.sale.fcst.dao.vo.User;


/**
 * UserDaoImpl
 * 
 * @Author hyson
 * @Version 1.0
 * @See
 * @Since [OVT OVALARM]/[DAO] 1.0
 */

@Repository
public class UserDaoImpl implements UserDao
{
    @Autowired
    private DaoHelper daoHelper;

    
    private static final String SQL_GET_USER_BY_CODE = 
            " select id,user_code,user_name,password,phone from t_user" +
            " where is_delete=0 and user_code=? LIMIT 1";
    
    private static final String SQL_GET_USER_BY_ID = 
            " select id,user_code,user_name,password,phone from t_user" +
            " where is_delete=0 and id=? LIMIT 1";
    
    public User getUserByCode(String userCode)
    {
        String errMsg = MessageFormat.format("Failed query user by code {0}!",
                userCode);
        User user = daoHelper.queryForObject(SQL_GET_USER_BY_CODE, new DaoRowMapper<User>(User.class),
                errMsg, userCode);

        return user;
    }

    public User getUserById(long userId)
    {
        String errMsg = MessageFormat.format("Failed query user by id {0}!",
                userId);
        User user = daoHelper.queryForObject(SQL_GET_USER_BY_ID, new DaoRowMapper<User>(User.class),
                errMsg, userId);

        return user;
    }
    
}