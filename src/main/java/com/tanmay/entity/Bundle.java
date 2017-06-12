package com.tanmay.entity;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author : tanmay
 * @created : 12-Jun-2017
 */
@XmlRootElement
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
