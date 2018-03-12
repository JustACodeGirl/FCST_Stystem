package com.ovt.sale.fcst.dao;

import java.util.List;
import java.util.Map;

import com.ovt.sale.fcst.common.model.PageInfo;
import com.ovt.sale.fcst.common.model.PageResult;
import com.ovt.sale.fcst.dao.vo.Forcast;

/**
 * 
 * @author lyman.meng
 *
 */
public interface ForcastDao {

	/**
	 * 
	 * @param userList
	 * @return
	 */
	public void uploadFcstInfo(List<Forcast> userList);

	/**
	 * 
	 * @param pageInfo
	 * @param region
	 * @param fcstWeek
	 * @param customer
	 * @param partId
	 * @param project
	 * @param accountManager
	 * @param market
	 * @return
	 */
	public PageResult<Forcast> getFcstInfo(PageInfo pageInfo, String region,
			String customer, String fcstWeek, String accountManager,
			String market);

	/**
	 * 
	 * @param fcstWeek
	 * @param bucketDate
	 * @return
	 */
	public List<Map<String, Object>> statsByDate(String fcstWeek,
			Map<String, List<String>> qtrMap);

	/**
	 * 
	 * @param confidenceLevel
	 * @param fcstWeek
	 * @param accountManager
	 * @return
	 */
	public List<Forcast> statsByParts(String confidenceLevel, String fcstWeek,
			String accountManager);

	/**
	 * 
	 * @param region
	 * @param confidenceLevel
	 * @param fcstWeek
	 * @param accountManager
	 * @return
	 */
	public List<Forcast> statsByCustom(String region, String confidenceLevel,
			String fcstWeek, String accountManager);

	/**
	 * 
	 * @return
	 */
	public List<String> getFcstWeek();

	/**
	 * 
	 * @return
	 */
	public List<String> getAccountManager();

	/**
	 * 
	 * @return
	 */
	public List<String> getCustomer();

	/**
	 * 
	 * @param bucketDate
	 * @return
	 */
	public List<String> getBucketDate(String fcst_week);

	/**
	 * 
	 * @param bucketDates
	 * @return
	 */
	public List<Map<String, Object>> getBuckectDateByPart(
			String confidenceLevel, String fcstWeek, String accountManager,
			String[] bucketDates);

	/**
	 * 
	 * @param bucketDates
	 * @return
	 */
	public List<Map<String, Object>> getBucketDateByCustomer(String region,
			String confidenceLevel, String fcstWeek, String accountManager,
			String[] bucketDates);

	public List<Forcast> getFcstList(String region,
			String customer, String fcstWeek, String accountManager, String market);

}
