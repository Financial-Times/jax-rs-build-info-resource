package com.ft.api.util.buildinfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileLoader {
	
	/**
	 * Loads a property file from the classpath
	 * @param propertiesFilePath The path of the properties resource e.g. /example.properties
	 * @return A Properties instance if found on the classpath
	 * @throws PropertiesFileLoaderException Thrown when there is a problem loading the properties file.
	 */
	public static Properties load(String propertiesFilePath) throws PropertiesFileLoaderException {
		InputStream in = PropertiesFileLoader.class.getResourceAsStream(propertiesFilePath);
    	if (in == null) {
    		throw new PropertiesFileLoaderException(propertiesFilePath);
    	}

		try {
			return loadProperties(in);
		} catch (IOException ex) {
    		throw new PropertiesFileLoaderException(propertiesFilePath, ex);
		}
	}

	private static Properties loadProperties(InputStream in) throws IOException {
		try {
			Properties prop = new Properties();
			prop.load(in);
			return prop;
		} finally {
			in.close();
		}
	}
	
}
