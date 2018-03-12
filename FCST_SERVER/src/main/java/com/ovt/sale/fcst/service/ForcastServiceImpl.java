package com.ovt.sale.fcst.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTItems;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotField;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPivotFields;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STAxis;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ovt.sale.fcst.common.model.PageInfo;
import com.ovt.sale.fcst.common.model.PageResult;
import com.ovt.sale.fcst.common.utils.DateTimeUtils;
import com.ovt.sale.fcst.common.utils.StringUtils;
import com.ovt.sale.fcst.dao.ForcastDao;
import com.ovt.sale.fcst.dao.vo.CheckList;
import com.ovt.sale.fcst.dao.vo.Forcast;
import com.ovt.sale.fcst.exception.FileTypeException;
import com.ovt.sale.fcst.exception.GettingTypeException;
import com.ovt.sale.fcst.exception.UploadFileException;

@Service
public class ForcastServiceImpl implements ForcastService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private AppPropertiesService appProperties;
	
	@Autowired
	private ForcastDao forcastDao;

	/**
	 * 
	 */
	public void uploadFcstXls(MultipartFile file) throws UploadFileException,
			IOException {
		if (!validateType(file)) {
		}
		List<Forcast> fcstList = new ArrayList<Forcast>();
		Workbook book = null;
		try {
			book = new XSSFWorkbook(file.getInputStream());
		} catch (Exception e) {
			book = new HSSFWorkbook(file.getInputStream());
		}
		Sheet sheet = book.getSheetAt(0);
		for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row.getLastCellNum() < 12) {
				// 少于12抛错
			}

			int number = (row.getLastCellNum() - 11) / 4;
			String confidenceLevel = "";
			String bucketDate = "";
			String qtr = "";
			for (int j = 0; j < number; j++) {

				if (null != row.getCell(11 + 4 * j + 3))
					row.getCell(11 + 4 * j + 3).setCellType(
							Cell.CELL_TYPE_NUMERIC);
				double asp = (null == row.getCell(11 + 4 * j + 3)) ? 0
						: (double) row.getCell(11 + 4 * j + 3)
								.getNumericCellValue();
				String region = (null == row.getCell(0)) ? null : row
						.getCell(0).getStringCellValue();
				String fcstWeek = (null == row.getCell(1)) ? null
						: DateTimeUtils.formatSqlDate(row.getCell(1)
								.getDateCellValue());
				String customer = (null == row.getCell(2)) ? null : row
						.getCell(2).getStringCellValue();
				String mi = (null == row.getCell(3)) ? null : row
						.getCell(3).getStringCellValue();
				String disti = (null == row.getCell(4)) ? null : row
						.getCell(4).getStringCellValue();
				String reseller = (null == row.getCell(5)) ? null : row
						.getCell(5).getStringCellValue();
				String partId = (null == row.getCell(6)) ? null : row
						.getCell(6).getStringCellValue();
				if (null != row.getCell(7))
					row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
				String project = (null == row.getCell(7)) ? null : row
						.getCell(7).getStringCellValue();
				String accountManager = (null == row.getCell(8)) ? null
						: row.getCell(8).getStringCellValue();
				String market = (null == row.getCell(9)) ? null : row
						.getCell(9).getStringCellValue();
				String note = (null == row.getCell(10)) ? null : row
						.getCell(10).getStringCellValue();
			
				if (null != row.getCell(11 + 4 * j + 0)) {
					row.getCell(11 + 4 * j + 0).setCellType(
							Cell.CELL_TYPE_NUMERIC);
					long qty = (long) row.getCell(11 + 4 * j + 0)
							.getNumericCellValue();
					if(qty > 0)
					{
						confidenceLevel = "BASE";
						bucketDate = analyzeDate(sheet.getRow(0)
								.getCell(11 + 4 * j + 0).getStringCellValue())[0];
						qtr = analyzeDate(sheet.getRow(0).getCell(11 + 4 * j + 0)
								.getStringCellValue())[1];
						Forcast forcast = new Forcast(region, fcstWeek, customer, mi,
								disti, reseller, partId, project, accountManager,
								market, note);
						forcast.setQty(qty);
						forcast.setAsp(asp);
						forcast.setBucketDate(bucketDate);
						forcast.setConfidenceLevel(confidenceLevel);
						forcast.setRevenue(qty * asp);
						forcast.setQtr(qtr);
						fcstList.add(forcast);
					}
				}
				if (null != row.getCell(11 + 4 * j + 1)) {
					row.getCell(11 + 4 * j + 1).setCellType(
							Cell.CELL_TYPE_NUMERIC);
					long qty = (long) row.getCell(11 + 4 * j + 1)
							.getNumericCellValue();
					if(qty > 0)
					{
						confidenceLevel = "UPSIDE";
						bucketDate = analyzeDate(sheet.getRow(0)
								.getCell(11 + 4 * j + 0).getStringCellValue())[0];
						qtr = analyzeDate(sheet.getRow(0).getCell(11 + 4 * j + 0)
								.getStringCellValue())[1];
						Forcast forcast = new Forcast(region, fcstWeek, customer, mi,
								disti, reseller, partId, project, accountManager,
								market, note);
						forcast.setQty(qty);
						forcast.setAsp(asp);
						forcast.setBucketDate(bucketDate);
						forcast.setConfidenceLevel(confidenceLevel);
						forcast.setRevenue(qty * asp);
						forcast.setQtr(qtr);
						fcstList.add(forcast);
					}
				}
				if (null != row.getCell(11 + 4 * j + 2)) {
					row.getCell(11 + 4 * j + 2).setCellType(
							Cell.CELL_TYPE_NUMERIC);
					long qty = (long) row.getCell(11 + 4 * j + 2)
							.getNumericCellValue();
					if(qty > 0)
					{
						confidenceLevel = "LOW";
						bucketDate = analyzeDate(sheet.getRow(0)
								.getCell(11 + 4 * j + 0).getStringCellValue())[0];
						qtr = analyzeDate(sheet.getRow(0).getCell(11 + 4 * j + 0)
								.getStringCellValue())[1];
						Forcast forcast = new Forcast(region, fcstWeek, customer, mi,
								disti, reseller, partId, project, accountManager,
								market, note);
						forcast.setQty(qty);
						forcast.setAsp(asp);
						forcast.setBucketDate(bucketDate);
						forcast.setConfidenceLevel(confidenceLevel);
						forcast.setRevenue(qty * asp);
						forcast.setQtr(qtr);
						fcstList.add(forcast);
					}
				}

			}
		}
		if (!fcstList.isEmpty()) {
			forcastDao.uploadFcstInfo(fcstList);
		}
	}

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
			String market) {
		return forcastDao.getFcstInfo(pageInfo, region, customer, fcstWeek,
				accountManager, market);
	}

	/**
	 * 
	 * @param fcstWeek
	 * @param bucketDate
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> statsByDate(String fcstWeek,
			String[] bucketDates) {
		Map<String, List<String>> qtrMap = BucketDateToQtr(bucketDates);
		List<Map<String, Object>> dataList = forcastDao.statsByDate(fcstWeek,
				qtrMap);
		Map<String, List<Map<String, Object>>> revenueList = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> data : dataList) {
			if (revenueList.containsKey(data.get("confidence_level"))) {
				String key = data.get("confidence_level").toString();
				data.remove("confidence_level");
				revenueList.get(key).add(data);
			} else {
				List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
				String key = data.get("confidence_level").toString();
				data.remove("confidence_level");
				row.add(data);
				revenueList.put(key, row);
			}
		}
		return revenueList;
	}

	/**
	 * 
	 * @param confidenceLevel
	 * @param fcstWeek
	 * @param accountManager
	 * @return
	 */
	public List<Forcast> statsByParts(String confidenceLevel, String fcstWeek,
			String accountManager) {
		return forcastDao.statsByParts(confidenceLevel, fcstWeek,
				accountManager);
	}

	/**
	 * 
	 * @param region
	 * @param confidenceLevel
	 * @param fcstWeek
	 * @param accountManager
	 * @return
	 */
	public List<Forcast> statsByCustom(String region, String confidenceLevel,
			String fcstWeek, String accountManager) {
		return forcastDao.statsByCustom(region, confidenceLevel, fcstWeek,
				accountManager);
	}

	/**
	 * 
	 */
	public List<CheckList> getBucketDate(String fcst_week) {
		List<String> bucketDateList = forcastDao.getBucketDate(fcst_week);
		List<CheckList> checkList = new ArrayList<CheckList>();
		Map<String, List<CheckList>> checkMap = new LinkedHashMap<String, List<CheckList>>();
		for (String bucketDate : bucketDateList) {
			if (StringUtils.isNotBlank(bucketDate)) {
				String[] fcstWeekArray = bucketDate.split("-");
				if (fcstWeekArray.length > 1) {
					int year = Integer.valueOf(fcstWeekArray[0]);
					int month = Integer.valueOf(fcstWeekArray[1]);
					if (month < 4) {
						if (checkMap.containsKey(year + "Q1")) {
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							checkMap.get(year + "Q1").add(check);
						} else {
							List<CheckList> list = new ArrayList<CheckList>();
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							list.add(check);
							checkMap.put(year + "Q1", list);
						}
					} else if (month < 7) {
						if (checkMap.containsKey(year + "Q2")) {
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							checkMap.get(year + "Q2").add(check);
						} else {
							List<CheckList> list = new ArrayList<CheckList>();
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							list.add(check);
							checkMap.put(year + "Q2", list);
						}
					} else if (month < 10) {
						if (checkMap.containsKey(year + "Q3")) {
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							checkMap.get(year + "Q3").add(check);
						} else {
							List<CheckList> list = new ArrayList<CheckList>();
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							list.add(check);
							checkMap.put(year + "Q3", list);
						}
					} else {
						if (checkMap.containsKey(year + "Q4")) {
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							checkMap.get(year + "Q4").add(check);
						} else {
							List<CheckList> list = new ArrayList<CheckList>();
							CheckList check = new CheckList();
							check.setLabel(bucketDate);
							check.setKey(bucketDate);
							check.setValue(bucketDate);
							list.add(check);
							checkMap.put(year + "Q4", list);
						}
					}
				}
			}
		}
		for (String key : checkMap.keySet()) {
			CheckList check = new CheckList();
			check.setLabel(key);
			check.setKey(key);
			check.setValue(key);
			check.setChildren(checkMap.get(key));
			checkList.add(check);
		}
		return checkList;
	}

	public List<String> getQtr(String fcstWeek) {
		List<String> qtrList = new ArrayList<String>();
		List<CheckList> bucketList = getBucketDate(fcstWeek);
		for (CheckList bucket : bucketList) {
			qtrList.add(bucket.getLabel());
		}
		return qtrList;
	}

	/**
	 * 
	 * @param fcst_week
	 * @return
	 */
	public List<String> getFcstWeek() {
		List<String> fcstWeekList = forcastDao.getFcstWeek();
		return fcstWeekList;
	}

	/**
	 * 
	 */
	public List<String> getAccountManager() {
		List<String> managerList = forcastDao.getAccountManager();
		return managerList;
	}

	public List<String> getCustomer() {
		List<String> customerList = forcastDao.getCustomer();
		return customerList;
	}

	/**
	 * 
	 */
	public Map<String, List<Map<String, Object>>> getBuckectDateByPart(
			String confidenceLevel, String fcstWeek, String accountManager,
			String[] bucketDates) {
		List<Map<String, Object>> dataList = forcastDao.getBuckectDateByPart(
				confidenceLevel, fcstWeek, accountManager, bucketDates);
		Map<String, List<Map<String, Object>>> partsList = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> data : dataList) {
			if (partsList.containsKey(data.get("part_id"))) {
				String key = data.get("part_id").toString();
				data.remove("part_id");
				partsList.get(key).add(data);
			} else {
				List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
				String key = data.get("part_id").toString();
				data.remove("part_id");
				row.add(data);
				partsList.put(key, row);
			}
		}
		return partsList;
	}

	public Map<String, List<Map<String, Object>>> getBucketDateByCustomer(
			String region, String confidenceLevel, String fcstWeek,
			String accountManager, String[] bucketDates) {
		List<Map<String, Object>> dataList = forcastDao
				.getBucketDateByCustomer(region, confidenceLevel, fcstWeek,
						accountManager, bucketDates);
		Map<String, List<Map<String, Object>>> partsList = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> data : dataList) {
			if (partsList.containsKey(data.get("customer"))) {
				String key = data.get("customer").toString();
				data.remove("customer");
				partsList.get(key).add(data);
			} else {
				List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
				String key = data.get("customer").toString();
				data.remove("customer");
				row.add(data);
				partsList.put(key, row);
			}
		}
		return partsList;

	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	private boolean validateType(MultipartFile file) {
		String fileType;
		try {
			String fileName = file.getOriginalFilename();
			fileType = fileName.substring(fileName.lastIndexOf('.'),
					fileName.lastIndexOf('s') + 1);
			if (fileType.isEmpty() || !fileType.toLowerCase().equals(".xls")) {
				throw new FileTypeException(
						"the file introduced is not .xls file.");
			}
		} catch (GettingTypeException e) {
			logger.error("error occurs while getting file type.");
			return false;
		} catch (FileTypeException e) {
			logger.error("the file introduced is not .xls file.");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private String[] analyzeDate(String date) {
		String[] dateArray = date.split("-");
		String year = dateArray[1].split(" ")[0];
		String qtr = "";
		String month = "";
		switch (dateArray[0]) {
		case "Jan":
			month = "01";
			qtr = "CY" + year + "Q1";
			break;
		case "Feb":
			month = "02";
			qtr = "CY" + year + "Q1";
			break;
		case "Mar":
			month = "03";
			qtr = "CY" + year + "Q1";
			break;
		case "Apr":
			month = "04";
			qtr = "CY" + year + "Q2";
			break;
		case "May":
			month = "05";
			qtr = "CY" + year + "Q2";
			break;
		case "Jun":
			month = "06";
			qtr = "CY" + year + "Q2";
			break;
		case "Jul":
			month = "07";
			qtr = "CY" + year + "Q3";
			break;
		case "Aug":
			month = "08";
			qtr = "CY" + year + "Q3";
			break;
		case "Sep":
			month = "09";
			qtr = "CY" + year + "Q3";
			break;
		case "Oct":
			month = "10";
			qtr = "CY" + year + "Q4";
			break;
		case "Nov":
			month = "11";
			qtr = "CY" + year + "Q4";
			break;
		case "Dec":
			month = "12";
			qtr = "CY" + year + "Q4";
			break;
		default:
			month = "01";
			qtr = "CY" + year + "Q1";
			break;
		}
		return new String[] { "20" + year + "-" + month + "-01", qtr };
	}

	private Map<String, List<String>> BucketDateToQtr(String bucketDates[]) {
		Map<String, List<String>> qtrMap = new LinkedHashMap<String, List<String>>();
		for (String bucketDate : bucketDates) {
			char[] charArray = bucketDate.toCharArray();
			String year = "" + charArray[0] + charArray[1] + charArray[2]
					+ charArray[3];
			int month = Integer.valueOf(("" + charArray[5] + charArray[6]));
			if (month < 4) {
				String qtr = "CY" + year + "Q1";
				if (qtrMap.containsKey(qtr)) {
					qtrMap.get(qtr).add(bucketDate);
				} else {
					List<String> buckets = new ArrayList<String>();
					buckets.add(bucketDate);
					qtrMap.put(qtr, buckets);
				}
			} else if (month < 7) {
				String qtr = "CY" + year + "Q2";
				if (qtrMap.containsKey(qtr)) {
					qtrMap.get(qtr).add(bucketDate);
				} else {
					List<String> buckets = new ArrayList<String>();
					buckets.add(bucketDate);
					qtrMap.put(qtr, buckets);
				}
			} else if (month < 10) {
				String qtr = "CY" + year + "Q3";
				if (qtrMap.containsKey(qtr)) {
					qtrMap.get(qtr).add(bucketDate);
				} else {
					List<String> buckets = new ArrayList<String>();
					buckets.add(bucketDate);
					qtrMap.put(qtr, buckets);
				}
			} else {
				String qtr = "CY" + year + "Q4";
				if (qtrMap.containsKey(qtr)) {
					qtrMap.get(qtr).add(bucketDate);
				} else {
					List<String> buckets = new ArrayList<String>();
					buckets.add(bucketDate);
					qtrMap.put(qtr, buckets);
				}
			}
		}
		return qtrMap;
	}
	
	public void dataExport(HttpServletResponse response,String region,
            String customer, String fcstWeek, String accountManager, String market){
	    long startTime = System.currentTimeMillis();
	    String exportPath = appProperties.getFileExcelExportPath();
	    List<Forcast> fcstList = forcastDao.getFcstList(region, customer, fcstWeek,
                accountManager, market); 
	    
	    
        String fileName = "FCST_Analysis_Review_" + fcstWeek + ".xlsx";
        String filePath = exportPath + File.separator + fileName;
        try {
            // 工作区
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFCell cell = null;
            
            XSSFFont headFont = workbook.createFont();  
            headFont.setCharSet(XSSFFont.DEFAULT_CHARSET);
            headFont.setFontHeight(9);
            headFont.setFontName("Arial");
            headFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//加粗 
            XSSFCellStyle  headStyle = workbook.createCellStyle();
            headStyle.setFont(headFont);
            
            XSSFFont bodyFont = workbook.createFont();
            bodyFont.setCharSet(XSSFFont.DEFAULT_CHARSET);
            bodyFont.setFontHeight(10);
            bodyFont.setFontName("Arial");
            XSSFCellStyle  bodyStyle = workbook.createCellStyle();
            bodyStyle.setFont(bodyFont);
            
            XSSFSheet sheet = workbook.createSheet("Data");
            sheet.setDefaultRowHeightInPoints((short) 12.45);
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1,3000 );
            sheet.setColumnWidth(2, 2000);
            sheet.setColumnWidth(3, 2000);
            sheet.setColumnWidth(4, 2000);
            sheet.setColumnWidth(5, 2000);
            sheet.setColumnWidth(6, 6000);
            sheet.setColumnWidth(7, 3000);
            sheet.setColumnWidth(8, 5000);
            sheet.setColumnWidth(9, 2000);
            sheet.setColumnWidth(10, 2000 );
            sheet.setColumnWidth(11, 4000);
            sheet.setColumnWidth(12, 2000);
            sheet.setColumnWidth(13, 3000);
            sheet.setColumnWidth(14, 2000);
            sheet.setColumnWidth(15, 3000);
            sheet.setColumnWidth(16, 2000);
            
            
            String[] headers = {"Region","Forecast Week","Customer","MI/CM","Disti","Reseller","PARTID","Project","Account Manager","Market","Note",
                    "Qty","ASP","Bucket Date","Confidence Level","Revenue","OVT QBR"};
            
            XSSFRow headerRow = sheet.createRow(0);//第一行为表头
            for(int i = 0;i<headers.length;i++){
                cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headStyle);
            }
            
            int n = 1;
            for (Forcast obj : fcstList) {
                XSSFRow row = sheet.createRow(n);
                cell = row.createCell(0);
                cell.setCellValue(obj.getRegion()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(1);
                cell.setCellValue(obj.getFcstWeek()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(2);
                cell.setCellValue(obj.getCustomer()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(3);
                cell.setCellValue(obj.getMi()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(4);
                cell.setCellValue(obj.getDisti()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(5);
                cell.setCellValue(obj.getReseller()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(6);
                cell.setCellValue(obj.getPartId()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(7);
                cell.setCellValue(obj.getProject()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(8);
                cell.setCellValue(obj.getAccountManager()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(9);
                cell.setCellValue(obj.getMarket()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(10);
                cell.setCellValue(obj.getNote()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(11);
                cell.setCellValue(obj.getQty()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(12);
                cell.setCellValue(obj.getAsp()); 
                cell.setCellStyle(bodyStyle); 
                
                cell = row.createCell(13);
                cell.setCellValue(obj.getBucketDate()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(14);
                cell.setCellValue(obj.getConfidenceLevel()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(15);
                cell.setCellValue(obj.getRevenue()); 
                cell.setCellStyle(bodyStyle);
                
                cell = row.createCell(16);
                cell.setCellValue(obj.getQtr()); 
                cell.setCellStyle(bodyStyle);
                n++;
            }
            createQuarterlyOverviewSheet(workbook);
            createByPartsSheet(workbook);
            createByCustomerSheet(workbook);
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        download(filePath, response);
        long endTime = System.currentTimeMillis();
        System.out.println("------export ["+fileName+"] cost:"+(endTime - startTime)/1000+" senonds ------");
	}
	
	
	private void createQuarterlyOverviewSheet(XSSFWorkbook workbook){
	    
	    XSSFSheet sourceSheet = workbook.getSheet("Data");
	    int pRow = sourceSheet.getLastRowNum();//要透视数据的结束行（从0开始）
        int pCol = 16;//要透视数据的结束列（从0开始）
        
        //area of pivot data
        CellReference start=new CellReference(0,0);
        CellReference end=new CellReference(pRow,pCol);
        
        AreaReference source=new AreaReference(start,end);
        CellReference position=new CellReference(0,0);    
        
        XSSFSheet quarterlySheet = workbook.createSheet("Quarterly Overview");
        XSSFPivotTable quarterlyPivotTable = quarterlySheet.createPivotTable(source,position,sourceSheet);
        //行区域
        quarterlyPivotTable.addRowLabel(14);//Confidence Level
        quarterlyPivotTable.addRowLabel(0);//Region
        
        //列区域
        addColLabel(quarterlyPivotTable, 16);//OVT QBR
        addColLabel(quarterlyPivotTable, 13);//Bucket Date
        
        //数据区域
        quarterlyPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 15,"Sum of Revenue");//Revenue 
	}
	
	private void createByPartsSheet(XSSFWorkbook workbook){
        
        XSSFSheet sourceSheet = workbook.getSheet("Data");
        int pRow = sourceSheet.getLastRowNum();//要透视数据的结束行（从0开始）
        int pCol = 16;//要透视数据的结束列（从0开始）
        
        //area of pivot data
        CellReference start=new CellReference(0,0);
        CellReference end=new CellReference(pRow,pCol);
        
        AreaReference source=new AreaReference(start,end);
        CellReference position=new CellReference(0,0);    
        
        XSSFSheet quarterlySheet = workbook.createSheet("By parts");
        XSSFPivotTable quarterlyPivotTable = quarterlySheet.createPivotTable(source,position,sourceSheet);
        //行区域
        quarterlyPivotTable.addRowLabel(6);//PARTID
        quarterlyPivotTable.addRowLabel(0);//Region
        quarterlyPivotTable.addRowLabel(2);//Customer
        quarterlyPivotTable.addRowLabel(3);//MI/CM
        
        //列区域
        addColLabel(quarterlyPivotTable, 13);//Bucket Date
        
        //数据区域
        quarterlyPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 11,"Sum of Qty");//Qty 
    }
	
	private void createByCustomerSheet(XSSFWorkbook workbook){
        
        XSSFSheet sourceSheet = workbook.getSheet("Data");
        int pRow = sourceSheet.getLastRowNum();//要透视数据的结束行（从0开始）
        int pCol = 16;//要透视数据的结束列（从0开始）
        
        //area of pivot data
        CellReference start=new CellReference(0,0);
        CellReference end=new CellReference(pRow,pCol);
        
        AreaReference source=new AreaReference(start,end);
        CellReference position=new CellReference(0,0);    
        
        XSSFSheet quarterlySheet = workbook.createSheet("By customer");
        XSSFPivotTable quarterlyPivotTable = quarterlySheet.createPivotTable(source,position,sourceSheet);
        //行区域
        quarterlyPivotTable.addRowLabel(6);//PARTID
        quarterlyPivotTable.addRowLabel(2);//Customer
        quarterlyPivotTable.addRowLabel(3);//MI/CM
        
        //列区域
        addColLabel(quarterlyPivotTable, 13);//Bucket Date
        
        //数据区域
        quarterlyPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 11,"Sum of Qty");//Qty 
        quarterlyPivotTable.addColumnLabel(DataConsolidateFunction.SUM, 15,"Sum of Revenue");//Revenue 
    }

	
	private void addColLabel(XSSFPivotTable pivotTable, int columnIndex) {
	        AreaReference pivotArea = new AreaReference(pivotTable.getPivotCacheDefinition().getCTPivotCacheDefinition()
	                .getCacheSource().getWorksheetSource().getRef());
	        int lastRowIndex = pivotArea.getLastCell().getRow() - pivotArea.getFirstCell().getRow();
	        int lastColIndex = pivotArea.getLastCell().getCol() - pivotArea.getFirstCell().getCol();

	        if (columnIndex > lastColIndex) {
	            throw new IndexOutOfBoundsException();
	        }
	        CTPivotFields pivotFields = pivotTable.getCTPivotTableDefinition().getPivotFields();

	        CTPivotField pivotField = CTPivotField.Factory.newInstance();
	        CTItems items = pivotField.addNewItems();

	        pivotField.setAxis(STAxis.AXIS_COL);
	        pivotField.setShowAll(false);
	        for (int i = 0; i <= lastRowIndex; i++) {
	            items.addNewItem().setT(STItemType.DEFAULT);
	        }
	        items.setCount(items.sizeOfItemArray());
	        pivotFields.setPivotFieldArray(columnIndex, pivotField);

	        CTColFields rowFields;
	        if (pivotTable.getCTPivotTableDefinition().getColFields() != null) {
	            rowFields = pivotTable.getCTPivotTableDefinition().getColFields();
	        } else {
	            rowFields = pivotTable.getCTPivotTableDefinition().addNewColFields();
	        }

	        rowFields.addNewField().setX(columnIndex);
	        rowFields.setCount(rowFields.sizeOfFieldArray());
	    }
	 
	
	private void download(String path, HttpServletResponse response) {
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(
                    response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=gb2312");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
}
