package com.ovt.sale.fcst.dao.vo;

import com.ovt.sale.fcst.common.annotation.Column;

public class Forcast {

	@Column("region")
	private String region;

	@Column("fcst_week")
	private String fcstWeek;

	@Column("customer")
	private String customer;

	@Column("mi")
	private String mi;

	@Column("disti")
	private String disti;

	@Column("reseller")
	private String reseller;

	@Column("part_id")
	private String partId;

	@Column("project")
	private String project;

	@Column("account_manager")
	private String accountManager;

	@Column("market")
	private String market;

	@Column("note")
	private String note;

	@Column("qty")
	private Long qty;

	@Column("asp")
	private Double asp;

	@Column("bucket_date")
	private String bucketDate;

	@Column("confidence_level")
	private String confidenceLevel;

	@Column("revenue")
	private Double revenue;

	@Column("qtr")
	private String qtr;

	public Forcast() {

	}

	public Forcast(String region, String fcstWeek, String customer, String mi, String disti, String reseller,
			String partId, String project, String accountManager, String market, String note) {
		super();
		this.region = region;
		this.fcstWeek = fcstWeek;
		this.customer = customer;
		this.mi = mi;
		this.disti = disti;
		this.reseller = reseller;
		this.partId = partId;
		this.project = project;
		this.accountManager = accountManager;
		this.market = market;
		this.note = note;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getFcstWeek() {
		return fcstWeek;
	}

	public void setFcstWeek(String fcstWeek) {
		this.fcstWeek = fcstWeek;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getMi() {
		return mi;
	}

	public void setMi(String mi) {
		this.mi = mi;
	}

	public String getDisti() {
		return disti;
	}

	public void setDisti(String disti) {
		this.disti = disti;
	}

	public String getReseller() {
		return reseller;
	}

	public void setReseller(String reseller) {
		this.reseller = reseller;
	}

	public String getPartId() {
		return partId;
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getAccountManager() {
		return accountManager;
	}

	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public Double getAsp() {
		return asp;
	}

	public void setAsp(Double asp) {
		this.asp = asp;
	}

	public String getBucketDate() {
		return bucketDate;
	}

	public void setBucketDate(String bucketDate) {
		this.bucketDate = bucketDate;
	}

	public String getConfidenceLevel() {
		return confidenceLevel;
	}

	public void setConfidenceLevel(String confidenceLevel) {
		this.confidenceLevel = confidenceLevel;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public String getQtr() {
		return qtr;
	}

	public void setQtr(String qtr) {
		this.qtr = qtr;
	}

}
