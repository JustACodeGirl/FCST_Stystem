package com.ovt.sale.fcst.dao.vo;

import java.sql.Timestamp;

import com.ovt.sale.fcst.common.annotation.Column;

public class AppProperty
{
    @Column("id")
    protected long id;
    
    @Column("prop_name")
    private String propName;

    @Column("prop_value")
    private String propValue;

    @Column("desc")
    private String desc;
    
    @Column("update_time")
    private Timestamp updateTime;
    
	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
}
