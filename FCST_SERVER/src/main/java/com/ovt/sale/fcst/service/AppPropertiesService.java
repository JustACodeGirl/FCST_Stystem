package com.ovt.sale.fcst.service;

import java.util.Map;

import com.ovt.sale.fcst.dao.vo.AppProperty;

public interface AppPropertiesService
{
    public Map<String, AppProperty> getAppPropertiesMap();

    public int getCookieAccessTokenAge();

    public String getFileExcelExportPath();

}
