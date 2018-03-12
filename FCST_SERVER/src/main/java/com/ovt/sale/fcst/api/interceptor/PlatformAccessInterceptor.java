/**
 * PlatformAccessInterceptor.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年6月30日
 */
package com.ovt.sale.fcst.api.interceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ovt.sale.fcst.api.response.OvForcastResponse;
import com.ovt.sale.fcst.common.model.JsonDocument;
import com.ovt.sale.fcst.common.utils.CookieUtil;
import com.ovt.sale.fcst.common.utils.HttpUtils;
import com.ovt.sale.fcst.common.utils.StringUtils;
import com.ovt.sale.fcst.dao.vo.User;
import com.ovt.sale.fcst.service.AppPropertiesService;
import com.ovt.sale.fcst.service.UserService;
import com.ovt.sale.fcst.service.context.SessionContext;
import com.ovt.sale.fcst.service.exception.ServiceErrorCode;
import com.ovt.sale.fcst.service.exception.ServiceException;
import com.ovt.sale.fcst.api.exception.NoAccessException;

/**
 * PlatformAccessInterceptor do the following things: <li>identify user by
 * access token from cookies before call controller</li>
 * 
 * @Author hyson
 * @Version 1.0
 * @See
 * @Since [OVT OVALARM]/[API] 1.0
 */
public class PlatformAccessInterceptor extends HandlerInterceptorAdapter
{
    @Autowired
    private UserService userService;

    @Autowired
    private AppPropertiesService appProperties;
    
    @PostConstruct
    private void init()
    {
       
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception
    {
        super.preHandle(request, response, handler);
        String accessToken = HttpUtils.getParamValue(request, CookieUtil.KEY_ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken))
        {
            writeResponse(response, ServiceErrorCode.NOT_LOGIN);
            return false;
        }

        User user = userService.getUserByAccessToken(accessToken);
        if(user == null)
        {
        	writeResponse(response, ServiceErrorCode.INVALID_ACCESS_TOKEN);
        	return false;
        }
        initSessionContext(user);

        CookieUtil.addCookie(response, CookieUtil.KEY_ACCESS_TOKEN, accessToken,
                appProperties.getCookieAccessTokenAge());
        
        return true;
    }

    /**
     * Initial session context.
     * 
     * @param user
     * @throws ServiceException
     * @throws NoAccessException
     */
    private void initSessionContext(User user)
    {
        SessionContext.setCurrentUser(user);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler, ModelAndView modelAndView) throws Exception
    {
        super.postHandle(request, response, handler, modelAndView);
        SessionContext.destroy();
    }

    private void writeResponse(HttpServletResponse response, String errCode) throws Exception
    {
        JsonDocument respBody = new OvForcastResponse(errCode);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.write(respBody, MediaType.APPLICATION_JSON, new ServletServerHttpResponse(
                response));
    }
    
}
