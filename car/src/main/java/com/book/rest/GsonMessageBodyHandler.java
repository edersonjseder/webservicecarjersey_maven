package com.book.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class is a content provider, which is annotated with @Provider above class, and
 * indicates that the return objects will be converted into JSON type and returned via HTTP by
 * CarsServletResource class to the requesting client
 * 
 * @author ederson
 * @author - Original base: http://eclipsesource.com/blogs/2012/11/02/integrating-gson-into-a-jax-rs-based-application/
 */
@Provider
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class GsonMessageBodyHandler implements MessageBodyWriter<Object>,
		MessageBodyReader<Object> {
	
	  private static final String UTF_8 = "UTF-8";
	  
	  private Gson gson;
	 
	  private Gson getGson() {
		  
	    if (gson == null) {
	    
	    	gson = new GsonBuilder().setPrettyPrinting().create();
	      
	    }
	    
	    return gson;
	  }

	@Override
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2,
			MediaType arg3) {
		return true;
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException {
		
		InputStreamReader streamReader = new InputStreamReader(entityStream, UTF_8);
		
		try {
			
			Type jsonType;
			
			if (type.equals(genericType)) {
				jsonType = type;
				
			} else {
				jsonType = genericType;
				
			}
			
			return getGson().fromJson(streamReader, jsonType);
			
		} finally {
			streamReader.close();
		}
	}

	@Override
	public long getSize(Object object, Class<?> type, Type GenericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> object, Type GenericType, Annotation[] annotations,
			MediaType mediaType) {
		return true;
	}

	@Override
	public void writeTo(Object object, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		
		OutputStreamWriter writer = new OutputStreamWriter(entityStream, UTF_8);
		
		try {
			
			Type jsonType;
			
			if (type.equals(genericType)) {
				jsonType = type;
			
			} else {
				jsonType = genericType;
			
			}
			
			getGson().toJson(object, jsonType, writer);
		
		} finally {
			writer.close();
	
		}
	}

}
