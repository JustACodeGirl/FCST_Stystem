package com.ovt.sale.fcst.dao;

/**
 * AbstractDao.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * May 5, 2015
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.ovt.sale.fcst.common.model.PageInfo;
import com.ovt.sale.fcst.common.model.PageResult;
import com.ovt.sale.fcst.common.utils.CollectionUtils;
import com.ovt.sale.fcst.common.utils.StringUtils;
import com.ovt.sale.fcst.dao.accessor.DataAccessorManager;
import com.ovt.sale.fcst.dao.exception.DBException;


/**
 * AbstractDao
 * 
 * @Author zhi.liu
 * @Version 2.0
 * @See
 * @Since [OVT OV3D]/[DAO] 1.0
 */
@Component
public class DaoHelper
{

    public static final String DB_ERROR_CODE_SAVE = "SaveFailed";

    public static final String DB_ERROR_CODE_QUERY = "QueryFailed";

    public static final String DB_ERROR_CODE_UPDATE = "UpdateFailed";
    
    private static final long DEFAULT_KEY = 1;

    public static final String DB_ERROR_CODE_INVALID_INDEX = "InvalidIndex";

    private static final String COUNT_SQL = "SELECT COUNT(1) ";

    private static final String COUNT_PATTERN = "SELECT COUNT(DISTINCT {0}) ";

    private static final String LIMIT_PATTERN_STRING = " LIMIT {0}, {1}";
    
    @Autowired
    private DataAccessorManager dataAccessorManager;

    public long save(final String sql,
            String errMsg, boolean returnKey, final Object... args)
    {
        long key = 0;
        if (returnKey)
        {
            key = saveWithKeyReturn(sql, errMsg, args);
        }
        else
        {
            key = save(sql, errMsg, args);
        }

        return key;
    }

    private long save(String sql , String errMsg,Object... args)
    {
        try
        {
            dataAccessorManager.getJdbcTemplate().update(sql, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_SAVE, errMsg, e);
        }

        return DEFAULT_KEY;
    }

    private long saveWithKeyReturn(final String sql, 
            String errMsg, final Object... args)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException
            {
                PreparedStatement ps =
                        con.prepareStatement(sql,
                                Statement.RETURN_GENERATED_KEYS);
                for (int i = 1; i <= args.length; i++)
                {
                    ps.setObject(i, args[i - 1]);
                }
                return ps;
            }

        };

        try
        {
            dataAccessorManager.getJdbcTemplate().update(psc, keyHolder);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_SAVE, errMsg, e);
        }

        Number key = keyHolder.getKey();
        long id = key != null ? key.longValue() : -1;

        return id;
    }

    public void update(String sql, String errMsg,
            Object... args)
    {
        try
        {
            dataAccessorManager.getJdbcTemplate().update(sql, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_UPDATE, errMsg, e);
        }

    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper,
            String errMsg, Object... args)
    {
        T result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForObject(sql,
                            rowMapper, args);
        }
        catch (EmptyResultDataAccessException e)
        {
            result = null;
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }

    public Map<String, Object> queryForMap(String sql, String errMsg, Object... args)
    {
        Map<String, Object> result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForMap(sql, args);
        }
        catch (EmptyResultDataAccessException e)
        {
            result = null;
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }
    
    public <T> T queryForObject(String sql, Class<T> elementType,
            String errMsg, Object... args)
    {
        T result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForObject(sql,
                            elementType, args);
        }
        catch (EmptyResultDataAccessException e)
        {
            result = null;
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType,
            String errMsg, Object... args)
    {
        List<T> result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForList(sql,
                            elementType, args);

        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }
    
    public List<Map<String, Object>> queryForList(String sql,
            String errMsg, Object... args)
    {
        List<Map<String, Object>> result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().queryForList(sql,
                            args);
            
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }
        
        return result;
    }

    public <T> List<T> queryForList(String sql, RowMapper<T> rowMapper,
            String errMsg, Object... args)
    {
        List<T> result = null;
        try
        {
            result =
                    dataAccessorManager.getJdbcTemplate().query(sql, rowMapper,
                            args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        return result;
    }
    
    public void batchUpdate(String sql, String errMsg,
            List<Object[]> batchArgs)
    {
        try
        {
            dataAccessorManager.getJdbcTemplate().batchUpdate(sql, batchArgs);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_UPDATE, errMsg, e);
        }
        
    }
    
    public <T> PageResult<T> queryForPageList(PageInfo pageInfo, String sql,
            RowMapper<T> rowMapper, String errMsg, Object... args)
    {
        return queryForPageList(pageInfo, null, sql, rowMapper, errMsg, args);
    }

    public <T> PageResult<T> queryForPageList(PageInfo pageInfo,
            String distinctField, String sql, Class<T> elementType,
            String errMsg, Object... args)
    {
        PageResult<T> pageResult = new PageResult<T>();

        int totalRecodes = getTotalRecodes(sql, distinctField, errMsg, args);

        if ((pageInfo.getPageNo() != 0)
                && (totalRecodes < pageInfo.getPageNo()
                        * pageInfo.getPageSize()))
        {
            throw new DBException(DB_ERROR_CODE_INVALID_INDEX, errMsg);
        }

        List<T> result = null;

        String limitPattenString = MessageFormat.format(LIMIT_PATTERN_STRING,
                pageInfo.getPageNo() * pageInfo.getPageSize(),
                pageInfo.getPageSize());
        sql += limitPattenString;

        try
        {
            result = dataAccessorManager.getJdbcTemplate().queryForList(sql, elementType, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        if (CollectionUtils.isNotEmpty(result))
        {
            pageResult.setPageNo(pageInfo.getPageNo());
            pageResult.setPageSize(pageInfo.getPageSize());
            pageResult.setTotalRecodes(totalRecodes);
            pageResult.setResults(result);
        }

        return pageResult;
    }

    public <T> PageResult<T> queryForPageList(PageInfo pageInfo, String sql,
            Class<T> elementType, String errMsg, Object... args)
    {
        return queryForPageList(pageInfo, null, sql, elementType, errMsg, args);
    }

    public <T> PageResult<T> queryForPageList(PageInfo pageInfo,
            String distinctField, String sql, RowMapper<T> rowMapper,
            String errMsg, Object... args)
    {
        PageResult<T> pageResult = new PageResult<T>();

        int totalRecodes = getTotalRecodes(sql, distinctField, errMsg, args);

        if ((pageInfo.getPageNo() != 0)
                && (totalRecodes <= pageInfo.getPageNo()
                        * pageInfo.getPageSize()))
        {
            throw new DBException(DB_ERROR_CODE_INVALID_INDEX, errMsg);
        }

        List<T> result = null;

        String limitPattenString = MessageFormat.format(LIMIT_PATTERN_STRING,
                new Long(pageInfo.getPageNo() * pageInfo.getPageSize())
                        .toString(), pageInfo.getPageSize());
        sql += limitPattenString;

        try
        {
            result = dataAccessorManager.getJdbcTemplate().query(sql, rowMapper, args);
        }
        catch (DataAccessException e)
        {
            throw new DBException(DB_ERROR_CODE_QUERY, errMsg, e);
        }

        if (CollectionUtils.isNotEmpty(result))
        {
            pageResult.setPageNo(pageInfo.getPageNo());
            pageResult.setPageSize(pageInfo.getPageSize());
            pageResult.setTotalRecodes(totalRecodes);
            pageResult.setResults(result);
        }

        return pageResult;
    }

    /**
     * 获得当前条件下，查询出的总记录数
     * 
     * @param sql
     * @param errMsg
     * @param args
     * @return
     */
    private int getTotalRecodes(String sql, String distinctField,
            String errMsg, Object... args)
    {
        Integer totalRecodes = 1;

        int firstIndex = sql.toUpperCase().indexOf("FROM"); // 找到SQL语句中的第一个FROM的下标
        String countSQL = null;
        if (StringUtils.isBlank(distinctField))
        {
            countSQL = COUNT_SQL + sql.substring(firstIndex, sql.length());
        }
        else
        {
            countSQL = MessageFormat.format(COUNT_PATTERN, distinctField)
                    + sql.substring(firstIndex, sql.length());
        }

        totalRecodes = queryForObject(countSQL, Integer.class, errMsg, args);
        return totalRecodes;
    }
}
