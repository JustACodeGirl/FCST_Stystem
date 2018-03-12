package com.ovt.sale.fcst.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ovt.sale.fcst.common.model.PageInfo;
import com.ovt.sale.fcst.common.model.PageResult;
import com.ovt.sale.fcst.common.utils.DaoRowMapper;
import com.ovt.sale.fcst.common.utils.StringUtils;
import com.ovt.sale.fcst.dao.vo.Forcast;

@Repository
public class ForcastDaoImpl implements ForcastDao {
	@Autowired
	DaoHelper daoHelper;

	private static final String INSERT_FCST = "INSERT INTO t_fcst(region,fcst_week,customer,mi,disti,reseller,part_id,project,account_manager,market,"
			+ "note,qty,asp,bucket_date,confidence_level,revenue,qtr) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String GET_FCST_WEEK = "SELECT DISTINCT fcst_week FROM t_fcst WHERE is_delete = 0";

	private static final String GET_MANAGER_LIST = "select distinct account_manager from t_fcst WHERE is_delete = 0";

	private static final String GET_CUSTOMER_LIST = "select distinct customer from t_fcst WHERE is_delete = 0";

	private static final String GET_BUCKET_DATE = "SELECT DISTINCT bucket_date FROM t_fcst WHERE fcst_week = ? and is_delete = 0 order by bucket_date";

	private static String SUM_QTY_CONST = "sum(case bucket_date when '$' then qty else 0 end) '$'";

	private static String SUM_QTY_REVENUE_CONST = "cast(CONCAT(sum(case bucket_date when '$' then qty else 0 end),',',sum(case bucket_date when '$' then revenue else 0 end)) as CHAR) '$'";

	private static String SUM_REVENUE_CONST = "(select confidence_level,region,sum(revenue) $1 "
			+ "from t_fcst where bucket_date in ($2) group by confidence_level, region) $3,";

	@Override
	public void uploadFcstInfo(List<Forcast> forcastList) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (Forcast forcast : forcastList) {
			Object[] arg = new Object[17];
			arg[0] = forcast.getRegion();
			arg[1] = forcast.getFcstWeek();
			arg[2] = forcast.getCustomer();
			arg[3] = forcast.getMi();
			arg[4] = forcast.getDisti();
			arg[5] = forcast.getReseller();
			arg[6] = forcast.getPartId();
			arg[7] = forcast.getProject();
			arg[8] = forcast.getAccountManager();
			arg[9] = forcast.getMarket();
			arg[10] = forcast.getNote();
			arg[11] = forcast.getQty();
			arg[12] = forcast.getAsp();
			arg[13] = forcast.getBucketDate();
			arg[14] = forcast.getConfidenceLevel();
			arg[15] = forcast.getRevenue();
			arg[16] = forcast.getQtr();
			batchArgs.add(arg);
		}
		daoHelper.batchUpdate(INSERT_FCST, "error", batchArgs);
	}

	@Override
	public PageResult<Forcast> getFcstInfo(PageInfo pageInfo, String region,
			String customer, String fcstWeek, String accountManager,
			String market) {
		String sql = "SELECT * FROM t_fcst WHERE is_delete = 0";
		sql += StringUtils.isNotBlank(region) ? (" AND region = '" + region + "'")
				: "";
		sql += StringUtils.isNotBlank(customer) ? (" AND customer = '" + customer)
				+ "'"
				: "";
		sql += StringUtils.isNotBlank(fcstWeek) ? (" AND fcst_week = '" + fcstWeek)
				+ "'"
				: "";
		sql += StringUtils.isNotBlank(accountManager) ? (" AND account_manager = '" + accountManager)
				+ "'"
				: "";
		sql += StringUtils.isNotBlank(market) ? (" AND market = '" + market)
				+ "'" : "";
		PageResult<Forcast> fcstPageList = daoHelper.queryForPageList(pageInfo,
				sql, new DaoRowMapper<Forcast>(Forcast.class), "error");
		return fcstPageList;
	}

	@Override
	public List<Map<String, Object>> statsByDate(String fcstWeek,
			Map<String, List<String>> qtrMap) {
		String sql = "SELECT t0.confidence_level,t0.region,";
		String[] keySets = qtrMap.keySet().toArray(new String[] {});
		String whereSql = "", sqlRevenue = "", revenueField = "";
		for (int i = 0; i < keySets.length; i++) {
			String bucketDates = "";
			for (String bucketDate : qtrMap.get(keySets[i])) {
				bucketDates += ("'" + bucketDate + "'" + ",");
			}
			bucketDates = bucketDates.substring(0, bucketDates.length() - 1);
			revenueField += ("t" + i + "." + keySets[i] + ",");
			sqlRevenue += SUM_REVENUE_CONST.replace("$1", keySets[i])
					.replace("$2", bucketDates).replace("$3", "t" + i);
			whereSql += "t0.confidence_level = " + "t" + i + "."
					+ "confidence_level and " + "t0.region = " + "t" + i + "."
					+ "region and ";
		}
		sql = sql + revenueField.substring(0, revenueField.length() - 1)
				+ " from " + sqlRevenue.substring(0, sqlRevenue.length() - 1)
				+ " where " + whereSql.substring(0, whereSql.length() - 5);
		List<Map<String, Object>> fcstList = daoHelper.queryForList(sql,
				"error");
		return fcstList;
	}

	@Override
	public List<Forcast> statsByParts(String confidenceLevel, String fcstWeek,
			String accountManager) {
		String sql = "SELECT * FROM t_fcst WHERE 1 = 1";
		sql += StringUtils.isNotBlank(confidenceLevel) ? (" AND confidence_level = '"
				+ confidenceLevel + "'")
				: "";
		sql += StringUtils.isNotBlank(fcstWeek) ? (" AND fcst_week = '"
				+ fcstWeek + "'") : "";
		sql += StringUtils.isNotBlank(accountManager) ? (" AND account_manager = '"
				+ accountManager + "'")
				: "";
		sql += " order by part_id ,region";
		List<Forcast> fcstList = daoHelper.queryForList(sql,
				new DaoRowMapper<Forcast>(Forcast.class), "error");
		return fcstList;
	}

	@Override
	public List<Forcast> statsByCustom(String region, String confidenceLevel,
			String fcstWeek, String accountManager) {
		String sql = "SELECT * FROM t_fcst WHERE 1 = 1";
		sql += StringUtils.isNotBlank(region) ? (" AND region = '" + region + "'")
				: "";
		sql += StringUtils.isNotBlank(confidenceLevel) ? (" AND confidence_level = '"
				+ confidenceLevel + "'")
				: "";
		sql += StringUtils.isNotBlank(fcstWeek) ? (" AND fcst_week = '"
				+ fcstWeek + "'") : "";
		sql += StringUtils.isNotBlank(accountManager) ? (" AND account_manager = '"
				+ accountManager + "'")
				: "";
		sql += " order by part_id ,customer, mi";
		List<Forcast> fcstList = daoHelper.queryForList(sql,
				new DaoRowMapper<Forcast>(Forcast.class), "error");
		return fcstList;
	}

	@Override
	public List<String> getFcstWeek() {
		List<String> fcstWeeks = daoHelper.queryForList(GET_FCST_WEEK,
				String.class, "error");
		return fcstWeeks;
	}

	@Override
	public List<String> getAccountManager() {
		List<String> accountManagerList = daoHelper.queryForList(
				GET_MANAGER_LIST, String.class, "error");
		return accountManagerList;
	}

	@Override
	public List<String> getCustomer() {
		List<String> customerList = daoHelper.queryForList(GET_CUSTOMER_LIST,
				String.class, "error");
		return customerList;
	}

	@Override
	public List<String> getBucketDate(String fcstWeek) {
		List<String> bucketList = daoHelper.queryForList(GET_BUCKET_DATE,
				String.class, "error", fcstWeek);
		return bucketList;
	}

	@Override
	public List<Map<String, Object>> getBuckectDateByPart(
			String confidenceLevel, String fcstWeek, String accountManager,
			String[] bucketDates) {
		String sql = "select part_id,region,";
		for (String bucketDate : bucketDates) {
			sql += (SUM_QTY_CONST.replace("$", bucketDate) + ",");
		}
		String sqlCondition = "";
		sqlCondition += StringUtils.isNotBlank(confidenceLevel) ? (" AND confidence_level = '"
				+ confidenceLevel + "'")
				: "";
		sqlCondition += StringUtils.isNotBlank(fcstWeek) ? (" AND fcst_week = '" + fcstWeek)
				+ "'"
				: "";
		sqlCondition += StringUtils.isNotBlank(accountManager) ? (" AND account_manager = '" + accountManager)
				+ "'"
				: "";
		sql = sql.substring(0, sql.length() - 1) + " from t_fcst where 1=1 "
				+ sqlCondition + " group by part_id, region";
		List<Map<String, Object>> data = daoHelper.queryForList(sql, "error");
		return data;
	}

	@Override
	public List<Map<String, Object>> getBucketDateByCustomer(String region,
			String confidenceLevel, String fcstWeek, String accountManager,
			String[] bucketDates) {
		String sql = "select customer,part_id,";
		for (String bucketDate : bucketDates) {
			sql += (SUM_QTY_REVENUE_CONST.replace("$", bucketDate) + ",");
		}
		String sqlCondition = "";
		sqlCondition += StringUtils.isNotBlank(region) ? (" AND region = '"
				+ region + "'") : "";
		sqlCondition += StringUtils.isNotBlank(confidenceLevel) ? (" AND confidence_level = '" + confidenceLevel)
				+ "'"
				: "";
		sqlCondition += StringUtils.isNotBlank(fcstWeek) ? (" AND fcst_week = '" + fcstWeek)
				+ "'"
				: "";
		sqlCondition += StringUtils.isNotBlank(accountManager) ? (" AND account_manager = '" + accountManager)
				+ "'"
				: "";
		sql = sql.substring(0, sql.length() - 1)
				+ " from t_fcst where customer is not null " + sqlCondition
				+ " group by customer,part_id";
		List<Map<String, Object>> data = daoHelper.queryForList(sql, "error");
		return data;
	}

	@Override
	public List<Forcast> getFcstList(String region,
			String customer, String fcstWeek, String accountManager, String market) {
		String sql = "SELECT * FROM t_fcst WHERE is_delete = 0";
		sql += StringUtils.isNotBlank(fcstWeek) ? (" AND fcst_week = '"
				+ fcstWeek + "'") : "";
		sql += StringUtils.isNotBlank(region) ? (" AND region = '" + region + "'")
				: "";
		sql += StringUtils.isNotBlank(customer) ? (" AND customer = '" + customer)
				+ "'"
				: "";
		sql += StringUtils.isNotBlank(accountManager) ? (" AND account_manager = '" + accountManager)
				+ "'"
				: "";
		sql += StringUtils.isNotBlank(market) ? (" AND market = '" + market)
				+ "'" : "";
		List<Forcast> fcstList = daoHelper.queryForList(sql,
				new DaoRowMapper<Forcast>(Forcast.class), "error");
		return fcstList;
	}
}
