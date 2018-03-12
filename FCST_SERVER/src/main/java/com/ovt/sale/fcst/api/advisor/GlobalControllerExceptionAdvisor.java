/**
 * GlobalExceptionController.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Jun 17, 2015
 */
package com.ovt.sale.fcst.api.advisor;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ovt.sale.fcst.api.response.OvForcastResponse;
import com.ovt.sale.fcst.common.constant.LoggerConstants;
import com.ovt.sale.fcst.common.log.Logger;
import com.ovt.sale.fcst.common.log.LoggerFactory;
import com.ovt.sale.fcst.common.model.JsonDocument;
import com.ovt.sale.fcst.service.exception.ServiceErrorCode;
import com.ovt.sale.fcst.service.exception.ServiceException;

/**
 * GlobalExceptionController
 * 
 * @Author hyson
 * @Version 1.0
 * @See
 * @Since [OVT OVALARM]/[API] 1.0
 */
@ControllerAdvice
public class GlobalControllerExceptionAdvisor
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(LoggerConstants.SYSTEM_LOGGER);

    @ExceptionHandler
    @ResponseBody
    public JsonDocument handleServiceException(ServiceException serviceException)
    {
        LOGGER.error("Controller catches service exception!", serviceException);
        return new OvForcastResponse(serviceException.getErrorCode());
    }

    @ExceptionHandler
    @ResponseBody
    public JsonDocument handleRuntimeException(RuntimeException runtimeException)
    {
        LOGGER.error("Controller catches runtime exception!", runtimeException);
        return new OvForcastResponse(ServiceErrorCode.SYSTEM_UNEXPECTED);
    }
}
