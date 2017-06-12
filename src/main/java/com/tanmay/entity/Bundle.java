package com.tanmay.entity;

import java.util.Map;

/**
 * @author : tanmay
 * @created : 12-Jun-2017
 */
public class Bundle {
	private String id;
	private Map<String, String> filter;

	public Map<String, String> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, String> filter) {
		this.filter = filter;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
