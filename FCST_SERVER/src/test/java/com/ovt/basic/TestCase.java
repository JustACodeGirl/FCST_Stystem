/**
 * TestCase.java
 * 
 * Copyright@2017 OVT Inc. All rights reserved. 
 * 
 * 2017年5月11日
 */
package com.ovt.basic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.client.RestTemplate;

import com.ovt.sale.fcst.common.exception.OVTException;
import com.ovt.sale.fcst.common.model.JsonDocument;


/**
 * TestCase
 * 
 * @Author hyson.yu
 * @Version 1.0
 * @See 
 * @Since [OVT OVALARM]/[API] 1.0
 */
public class TestCase
{

    private static RestTemplate restTemplate = new RestTemplate();
    //public static String BASE_URL = "http://localhost:18080/SaleForcast/api";
    public static String BASE_URL = "http://116.211.106.187:8383/fcst/api";
    
    /**
     * @param args
     */
    public static void main(String[] args)  throws OVTException, IOException
    {
        userLogin();
        //userLogout();
        //userInfo();

    }
    
    private static void userLogin()
    {
        String url = BASE_URL + "/users/login?userCode=admin&password=admin";
        JsonDocument response = restTemplate.postForObject(url, null, JsonDocument.class);
        System.out.println(response.getServiceCode());
        System.out.println(response.getStateCode());
        System.out.println(response.getData());
    }
    
    private static void userLogout()
    {
        String url = BASE_URL + "/users/logout?access_token=53f3c0e23e44b65858c7dd34535c6fd0";
        JsonDocument response = restTemplate.postForObject(url, null, JsonDocument.class);
        
        System.out.println(response.getServiceCode());
        System.out.println(response.getStateCode());
        System.out.println(response.getData());
    }
    
    
    private static void userInfo()
    {
        String url = BASE_URL + "/users/info?access_token=53f3c0e23e44b65858c7dd34535c6fd0";
                
        JsonDocument response = restTemplate.getForObject(url, JsonDocument.class);
        System.out.println(response.getServiceCode());
        System.out.println(response.getStateCode());
        System.out.println(response.getData());
    }



}
