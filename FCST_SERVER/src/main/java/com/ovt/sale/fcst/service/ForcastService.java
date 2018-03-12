package com.ovt.sale.fcst.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.ovt.sale.fcst.common.model.PageInfo;
import com.ovt.sale.fcst.common.model.PageResult;
import com.ovt.sale.fcst.dao.vo.CheckList;
import com.ovt.sale.fcst.dao.vo.Forcast;
import com.ovt.sale.fcst.exception.UploadFileException;

public interface ForcastService {

	/**
	 * 
	 * @param file
	 * @return
	 * @throws UploadFileException
	 * @throws IOException
	 */
	public void uploadFcstXls(MultipartFile file) throws UploadFileException,
			IOException;

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
	public Map<String, List<Map<String, Object>>> statsByDate(String fcstWeek,
			String[] bucketDates);

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
	 * @param fcst_week
	 * @return
	 */
	public List<CheckList> getBucketDate(String fcst_week);

	/**
	 * 
	 * @param fcstWeek
	 * @return
	 */
	public List<String> getQtr(String fcstWeek);

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
	 * @param bucketDates
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getBuckectDateByPart(
			String confidenceLevel, String fcstWeek, String accountManager,
			String[] bucketDates);

	/**
	 * 
	 * @param bucketDates
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getBucketDateByCustomer(
			String region, String confidenceLevel, String fcstWeek,
			String accountManager, String[] bucketDates);

	public void dataExport(HttpServletResponse response, String region,
			String customer, String partId, String accountManager, String market);

}
