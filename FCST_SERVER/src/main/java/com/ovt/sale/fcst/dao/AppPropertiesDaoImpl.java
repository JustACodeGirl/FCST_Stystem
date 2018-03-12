package com.ovt.sale.fcst.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.sale.fcst.common.utils.CollectionUtils;
import com.ovt.sale.fcst.common.utils.DaoRowMapper;
import com.ovt.sale.fcst.dao.vo.AppProperty;


@Repository
public class AppPropertiesDaoImpl implements AppPropertiesDao {

	@Autowired
	private DaoHelper daoHelper;
	
	private static final String SQL_GET_APP_PROPERTIES = "SELECT id, prop_name, prop_value, `desc`, "
            + " update_time FROM app_properties";
	
	
	@Override
	public Map<String, AppProperty> getAppPropertiesMap() 
	{
		Map<String, AppProperty> appPropertiesMap = new HashMap<String, AppProperty>();

        List<AppProperty> appProperties = this.getAppPropertiesList();
        if (CollectionUtils.isNotEmpty(appProperties))
        {
            for (AppProperty appProperty : appProperties)
            {
                appPropertiesMap.put(appProperty.getPropName(), appProperty);
            }
        }

        return appPropertiesMap;
	}

	@Override
	public List<AppProperty> getAppPropertiesList() 
	{

	    List<AppProperty> appProperties = daoHelper.queryForList(SQL_GET_APP_PROPERTIES,
                new DaoRowMapper<AppProperty>(AppProperty.class), "error");
	    
        return appProperties;
	}



}
