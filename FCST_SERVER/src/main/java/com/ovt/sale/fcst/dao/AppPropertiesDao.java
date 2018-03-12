package com.ovt.sale.fcst.dao;

import java.util.List;
import java.util.Map;

import com.ovt.sale.fcst.dao.vo.AppProperty;


public interface AppPropertiesDao 
{
	public Map<String, AppProperty> getAppPropertiesMap();
	
	public List<AppProperty> getAppPropertiesList();
	
}
