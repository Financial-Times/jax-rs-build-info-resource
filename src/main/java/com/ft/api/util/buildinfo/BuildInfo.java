package com.ft.api.util.buildinfo;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class BuildInfo {

  @Getter private final Map<String, String> buildInfo;

  public BuildInfo(Properties properties) {
    this.buildInfo = toMap(properties);
  }

  private Map<String, String> toMap(Properties properties) {
    Map<String, String> map = new TreeMap<>();
    for (String key : properties.stringPropertyNames()) {
      map.put(key, properties.getProperty(key));
    }
    return map;
  }

  public String getProperty(String propertyName) {
    return buildInfo.get(propertyName);
  }
}
