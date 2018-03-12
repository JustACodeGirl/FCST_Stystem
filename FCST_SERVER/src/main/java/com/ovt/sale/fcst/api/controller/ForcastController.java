package com.ovt.sale.fcst.api.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ovt.sale.fcst.api.response.OvForcastResponse;
import com.ovt.sale.fcst.common.model.JsonDocument;
import com.ovt.sale.fcst.common.model.PageInfo;
import com.ovt.sale.fcst.common.model.PageResult;
import com.ovt.sale.fcst.common.utils.HttpUtils;
import com.ovt.sale.fcst.dao.vo.CheckList;
import com.ovt.sale.fcst.dao.vo.Forcast;
import com.ovt.sale.fcst.exception.UploadFileException;
import com.ovt.sale.fcst.service.ForcastService;

/**
 * 
 * @author lyman.meng
 *
 */
@Controller
@EnableWebMvc
public class ForcastController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ForcastService fcstService;

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument upload(@RequestParam("xlsfile") MultipartFile file) {
		try {
			fcstService.uploadFcstXls(file);
		} catch (UploadFileException e) {
			logger.error("error occurs while getting file type.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("error occurs while transfering file");
			e.printStackTrace();
		}
		return new OvForcastResponse();
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument getFcstInfo(HttpServletRequest request,@RequestParam String fcstWeek, 
			@RequestParam(required = false, defaultValue = "") String region, @RequestParam(required = false, defaultValue = "") String customer,
			@RequestParam(required = false, defaultValue = "") String accountManager,
			@RequestParam(required = false, defaultValue = "") String market) {
		PageInfo pageInfo = HttpUtils.getPageInfo(request);
		PageResult<Forcast> fcstList = fcstService.getFcstInfo(pageInfo,
				region, customer, fcstWeek, accountManager, market);
		return new OvForcastResponse(fcstList);
	}

	@RequestMapping(value = "/statsByQuarter", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument statsByDate(@RequestParam String fcstWeek,
			@RequestParam(required = false) String[] bucketDates) {
		Map<String, List<Map<String, Object>>> fcstList = fcstService
				.statsByDate(fcstWeek, bucketDates);
		return new OvForcastResponse(fcstList);
	}

	@RequestMapping(value = "/statsByPart", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument statsByParts(@RequestParam String confidenceLevel,
			@RequestParam String fcstWeek, @RequestParam String accountManager,
			@RequestParam String[] bucketDates) {
		Map<String, List<Map<String, Object>>> fcstList = fcstService
				.getBuckectDateByPart(confidenceLevel, fcstWeek,
						accountManager, bucketDates);
		return new OvForcastResponse(fcstList);
	}

	@RequestMapping(value = "/statsByCustomer", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument statsByCustom(@RequestParam String region,
			@RequestParam String confidenceLevel,
			@RequestParam String fcstWeek, @RequestParam String accountManager,
			@RequestParam String[] bucketDates) {
		Map<String, List<Map<String, Object>>> fcstList = fcstService
				.getBucketDateByCustomer(region, confidenceLevel, fcstWeek,
						accountManager, bucketDates);
		return new OvForcastResponse(fcstList);
	}

	@RequestMapping(value = "/getFcstWeek", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument getFcstWeek() {
		List<String> checkList = fcstService.getFcstWeek();
		return new OvForcastResponse(checkList);
	}

	@RequestMapping(value = "/getQtr", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument getQtr(String fcstWeek) {
		List<String> qtrList = fcstService.getQtr(fcstWeek);
		return new OvForcastResponse(qtrList);
	}

	@RequestMapping(value = "/getAccountManager", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument getAccountManager() {
		List<String> managerList = fcstService.getAccountManager();
		return new OvForcastResponse(managerList);
	}

	@RequestMapping(value = "/getCustomer", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument getCustomer() {
		List<String> managerList = fcstService.getCustomer();
		return new OvForcastResponse(managerList);
	}

	@RequestMapping(value = "/getBucketDate", method = RequestMethod.POST)
	@ResponseBody
	public JsonDocument getBucketDate(@RequestParam String fcstWeek) {
		List<CheckList> fcstList = fcstService.getBucketDate(fcstWeek);
		return new OvForcastResponse(fcstList);
	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void dataExport(HttpServletResponse response,
			@RequestParam String region, @RequestParam String customer,
			@RequestParam String fcstWeek, @RequestParam String accountManager,
			@RequestParam String market) {
		fcstService.dataExport(response, region, customer, fcstWeek,
				accountManager, market);
	}

}
