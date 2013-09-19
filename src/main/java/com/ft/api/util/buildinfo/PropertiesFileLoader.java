package com.ft.api.util.buildinfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileLoader {
	
	/**
	 * Loads a property file from the classpath
	 * @param propertiesFile The path of the properties resource e.g. /example.properties
	 * @return A Properties instance or null if not found on the classpath
	 */
	public static Properties load(String propertiesFile) {
		try {
			InputStream in = PropertiesFileLoader.class.getResourceAsStream(propertiesFile);
	    	if (in == null) {
	    		return null;
	    	}
    		Properties prop = new Properties();
    		prop.load(in);
    		in.close();
    		return prop;
		} catch (IOException ex) {
			return null;
		}
	}
	
}
