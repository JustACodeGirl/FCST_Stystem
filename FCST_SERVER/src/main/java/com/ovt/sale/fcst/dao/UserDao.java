/**
 * UserDao.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年6月30日
 */
package com.ovt.sale.fcst.dao;


import com.ovt.sale.fcst.dao.vo.User;


/**
 * UserDao
 * 
 * @Author hyson
 * @Version 1.0
 * @See 
 * @Since [OVT OVALARM]/[DAO] 1.0
 */
public interface UserDao
{
    User getUserByCode(String userCode);
    
    User getUserById(long userId);
    
    
    
}
