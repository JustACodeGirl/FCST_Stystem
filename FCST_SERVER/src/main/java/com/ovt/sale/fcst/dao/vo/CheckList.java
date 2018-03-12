package com.ovt.sale.fcst.dao.vo;

import java.util.List;

public class CheckList {

	private String label;

	private String value;

	private String key;

	private List<CheckList> children;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<CheckList> getChildren() {
		return children;
	}

	public void setChildren(List<CheckList> children) {
		this.children = children;
	}
}
