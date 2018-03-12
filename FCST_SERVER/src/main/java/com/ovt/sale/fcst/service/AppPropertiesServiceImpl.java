package com.ovt.sale.fcst.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.sale.fcst.common.exception.OVTRuntimeException;
import com.ovt.sale.fcst.common.utils.CollectionUtils;
import com.ovt.sale.fcst.common.utils.DataConvertUtils;
import com.ovt.sale.fcst.dao.AppPropertiesDao;
import com.ovt.sale.fcst.dao.vo.AppProperty;


@Service
public class AppPropertiesServiceImpl implements AppPropertiesService {

	private Map<String,AppProperty> appPropertiesMap = new HashMap<String,AppProperty>();

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final String COOKIE_ACCESS_AGE_STRING = "cookie.access_token.age";
	
	private final String FILE_EXCEL_EXPORT_PATH = "file.excel.export.path";
	
    
	@Autowired
	private AppPropertiesDao appPropertiesDao;
	
	@PostConstruct
	private void init()
	{
		try {
			appPropertiesMap = appPropertiesDao.getAppPropertiesMap();
		} catch (OVTRuntimeException e) {
			logger.error("Failed to get app properties.", e);
		}
	}
	
	
	@Override
	public int getCookieAccessTokenAge() 
	{
		AppProperty property = getAppPropertiesMap().get(COOKIE_ACCESS_AGE_STRING);
        return property == null ? 0 : DataConvertUtils.toInt(property.getPropValue());
	}

	@Override
	public Map<String, AppProperty> getAppPropertiesMap() 
	{
		if(CollectionUtils.isEmpty(appPropertiesMap))
		{
			init();
		}
		return appPropertiesMap;
	}
	
	
	@Override
	public String getFileExcelExportPath() 
	{
		AppProperty property = getAppPropertiesMap().get(FILE_EXCEL_EXPORT_PATH);
		return property == null ? "" : property.getPropValue();
	}
	

}
