/**
 * PlatformErrorCode.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */
package com.ovt.sale.fcst.service.exception;

/**
 * PlatformErrorCode
 * 
 * @Author hyson
 * @Version 1.0
 * @See 
 * @Since [OVT OVALARM]/[API] 1.0
 */
public class ServiceErrorCode
{
    
    //user
    public static final String USER_CODE_OR_NAME_NULL = "UserCodeOrNameIsNull";//用户编码或用户名为空
    public static final String USER_CODE_EXIST = "UserCodeHasExisted";//用户编码已存在
    public static final String OLD_PASSWORD_WRONG = "OldPasswordIsWrong";//原始密码错误
    public static final String PASSWORD_NULL = "PasswordCanNotBeNull";//密码不能为空
    public static final String NOT_LOGIN = "NotLogin";//未登录
    public static final String INVALID_ACCESS_TOKEN = "InvalidAccessToken";//登录无效
    public static final String WRONG_PASSWORD_USERCODE = "UserCodeOrPasswordWrong";//用户编码或密码错误
    public static final String USER_NOT_EXIST = "UserIsNotExist";//用户不存在
    
    //system
    public static final String SYSTEM_UNEXPECTED = "SystemUnexpected";//系统未知错误
    
    //app properties
    public static final String PROPERTY_NAME_EXIST = "PropertyNameExist";
    
    
}
