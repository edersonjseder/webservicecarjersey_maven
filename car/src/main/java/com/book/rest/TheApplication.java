package com.book.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Application;

public class TheApplication extends Application {
	
	/**
	 * This method is used to add features to Jersey and make the configuration
	 * to allow Jersey returns JSON data.
	 * 
	 */
//	@Override
//	public Set<Object> getSingletons() {
//		
//		Set<Object> singletons = new HashSet<Object>();
//		
//		// Jettison to generate JSON
//		singletons.add(new JettisonFeature());
//		
//		return singletons;
//	}
	
	/**
	 * This method configures the 'jersey.config.server.provider.packages' property
	 * that indicates to Jersey which packages are the web service classes and scan
	 * the annotations (@Path, @GET, @POST...) on them.
	 * 
	 */
	@Override
	public Map<String, Object> getProperties() {
		
		Map<String, Object> properties = new HashMap<String, Object>();
		
		// Configure the package to scan classes with REST annotations
		properties.put("jersey.config.server.provider.packages", "com.book.rest");
		
		return properties;
	}

}
