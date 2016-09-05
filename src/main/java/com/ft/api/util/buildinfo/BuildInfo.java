package com.ft.api.util.buildinfo;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public class BuildInfo {
	
	private final Map<String, String> buildInfo;

	public BuildInfo(Properties properties) {
		this.buildInfo = toMap(properties);
	}
	
	public Map<String, String> getBuildInfo() {
		return buildInfo;
	}

	private Map<String, String> toMap(Properties properties) {
		Map<String, String> map = new TreeMap<String, String>();
		for (String key : properties.stringPropertyNames()) {
			map.put(key, properties.getProperty(key));
		}
		return map;
	}
	
	public String getProperty(String propertyName) {
		return buildInfo.get(propertyName);
	}
	
}
