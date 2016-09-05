package com.ft.api.util.buildinfo;

import java.io.IOException;

public class PropertiesFileLoaderException extends RuntimeException {

	private static final long serialVersionUID = 2209955697623045918L;

	private static final String DEFAULT_MESSAGE = "Unable to load resource from classpath:%s.";

	private String propertiesFilePath;

	public PropertiesFileLoaderException(String propertiesFilePath) {
		super(buildDefaultMessage(propertiesFilePath));
		this.propertiesFilePath = propertiesFilePath;
	}

	public PropertiesFileLoaderException(String propertiesFilePath, IOException cause) {
		super(buildDefaultMessage(propertiesFilePath), cause);
		this.propertiesFilePath = propertiesFilePath;
	}

	public String getPropertiesFilePath() {
		return propertiesFilePath;
	}

	private static String buildDefaultMessage(String propertiesFilePath) {
		return String.format(DEFAULT_MESSAGE, propertiesFilePath);
	}

}
