package com.ft.api.util.buildinfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/build-info/version")
@Produces(MediaType.TEXT_PLAIN)
public class VersionResource {

	private static final String DEFAULT_BUILD_INFO_PROPERTIES = "/build-info.properties";

	private final String propertiesFileName;
	
	public VersionResource() {
		propertiesFileName = DEFAULT_BUILD_INFO_PROPERTIES;
	}
	
	public VersionResource(String propertiesFileName) {
		this.propertiesFileName = propertiesFileName;
	}

	@GET
	public String getVersion() {

		try {
			Properties buildInfoProperties = PropertiesFileLoader.load(propertiesFileName);
			return buildInfoProperties.getProperty("artifact.version");
		} catch(PropertiesFileLoaderException ex) {
			Response error = Response.serverError()
									 .entity(ex.getMessage())
									 .type(MediaType.TEXT_PLAIN)
									 .build();
			
			throw new WebApplicationException(error);
		}
	}

}
