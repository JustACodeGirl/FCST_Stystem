/**
 * UsersController.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年6月30日
 */
package com.ovt.sale.fcst.api.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovt.sale.fcst.api.response.OvForcastResponse;
import com.ovt.sale.fcst.common.model.JsonDocument;
import com.ovt.sale.fcst.common.utils.CookieUtil;
import com.ovt.sale.fcst.common.utils.HttpUtils;
import com.ovt.sale.fcst.dao.vo.User;
import com.ovt.sale.fcst.service.AppPropertiesService;
import com.ovt.sale.fcst.service.UserService;
import com.ovt.sale.fcst.service.context.SessionContext;
import com.ovt.sale.fcst.service.exception.ServiceException;

/**
 * UsersController provides restful APIs of user
 * 
 * @Author hyson
 * @Version 1.0
 * @See
 * @Since [OVT OVALARM]/[API] 1.0
 */

@Controller
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private AppPropertiesService appProperties;

    private static final JsonDocument SUCCESS = OvForcastResponse.SUCCESS;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ResponseBody
    public JsonDocument login(HttpServletResponse response,
            @RequestParam String userCode, @RequestParam String password)
            throws ServiceException
    {
        String accessToken = userService.userLogin(userCode, password);
        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN,
                accessToken, appProperties.getCookieAccessTokenAge());
        User currentUser = SessionContext.getCurrentUser();
        currentUser.setPassword("");
        return new OvForcastResponse(currentUser);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    @ResponseBody
    public JsonDocument logout(HttpServletRequest request,
            HttpServletResponse response) throws ServiceException
    {
        String accessToken = HttpUtils.getParamValue(request,
                CookieUtil.KEY_ACCESS_TOKEN);
        userService.logout(accessToken);

        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN,
                accessToken, 0);

        return SUCCESS;
    }

    
    @RequestMapping(method = RequestMethod.GET, value = "/info")
    @ResponseBody
    public JsonDocument getCurrentUser() throws ServiceException
    {
        User currentUser = SessionContext.getCurrentUser();
        currentUser.setPassword("");
        return new OvForcastResponse(currentUser);
    }
    


}
