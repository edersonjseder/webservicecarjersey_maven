package com.book.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.book.model.Car;
import com.book.model.ResponseJSON;
import com.book.service.CarService;
import com.book.utils.UploadService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * REST resource service to get cars information
 * 
 * @author ederson
 *
 */
@Path("/cars")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Component
public class CarsResource {

	
	@Autowired
	private CarService service;
	
	@Autowired
	private UploadService UploadService;
	
	@GET
	public List<Car> get(){
		
		List<Car> carsList = null;
		
		carsList = service.getCars();

		return carsList;
	}
	
	@GET
	@Path("{id}")
	public Car get(@PathParam("id") Integer id){
		Car car = null;
		
		car = service.getCarById(id);
		
	    return car;
	}
	
	@GET
	@Path("/type/{type}")
	public List<Car> getByType(@PathParam("type") String type){
		List<Car> carsList = null;
		
		carsList = service.getCarByType(type);
		
	    return carsList;
	}
	
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") Integer id){
		String stringDeletedCarJson = "";
		ResponseJSON response;
		
		if(id != null){
			service.delete(id);
			response = ResponseJSON.ok("Car removed successfully");
			
			stringDeletedCarJson = showResponseJSONFormatted(response);
			
		} else {
			response = ResponseJSON.error("Invalid URL");
			
			stringDeletedCarJson = showResponseJSONFormatted(response);
			
		}
		
	    return Response.status(200).entity(stringDeletedCarJson).build();
	}
	
	@POST
	public Response post(Car car){
		String stringCarJson = "";
		ResponseJSON response;
		
		service.saveOrUpdate(car);
		response = ResponseJSON.ok("Car added successfully");
		stringCarJson = showResponseJSONFormatted(response);
		
	    return Response.status(200).entity(stringCarJson).build();
	}

	@PUT
	public Response put(Car car){
		String stringCarJson = "";
		ResponseJSON response;
		
		service.saveOrUpdate(car);
		response = ResponseJSON.ok("Car Updated successfully");
		stringCarJson = showResponseJSONFormatted(response);
		
	    return Response.status(200).entity(stringCarJson).build();
	}
	
	/**
	 * This method send an image file to temporary JVM folder and upload
	 * it on the cloud storage
	 * 
	 * @param multiPart
	 * @return
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ResponseJSON postPhoto(final FormDataMultiPart multiPart) {
		
		if(multiPart != null && multiPart.getFields() != null){
			Set<String> keys = multiPart.getFields().keySet();
			
			for (String key : keys) {
				// Get an InputStream to read the file
				FormDataBodyPart field = multiPart.getField(key);
				InputStream inputStream = field.getValueAs(InputStream.class);
				
				try {
					// Save the file
					String fileName = field.getFormDataContentDisposition().getFileName();
					
					String url = UploadService.uploadFile(fileName, inputStream);
					
					System.out.println("File: " + url);
					
					return ResponseJSON.ok("File successfully received", url);
					
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}
			
		}
		
		return ResponseJSON.error("Invalid request");
	}
	
	/**
	 * This method returns a conversion from an image file to a base64 string, it's
	 * easy to work in this image format on mobile application, and have better performance 
	 * 
	 * @param multiPart
	 * @return
	 */
	@POST
	@Path("/toBase64")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response toBase64(final FormDataMultiPart multiPart) {
		
		ResponseJSON response = null;
		
		String base64 = UploadService.passToBase64(multiPart);
		
		response = ResponseJSON.ok(base64);
		
		base64 = showResponseJSONFormatted(response);
			
		return Response.status(200).entity(base64).build();
	}
	
	@POST
	@Path("/postPhotoBase64")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postPhotoBase64(@FormParam("fileName") String fileName, @FormParam("base64") String base64){
		
		ResponseJSON response = null;
		
		try {
			
			String path = UploadService.getPhotoBase64(fileName, base64);
			
			response = ResponseJSON.ok(path);
			
			path = showResponseJSONFormatted(response);
			
			return Response.status(200).entity(path).build();
			
		} catch (Exception e) {
			e.printStackTrace();
			
			response = ResponseJSON.error(e.getMessage());
			String error = showResponseJSONFormatted(response);
			
			return Response.status(200).entity(error).build();
		}
				
	}
	
	/**
	 * This method transform the Object result into JSON String value
	 * 
	 * @param object
	 * @return
	 */
	private String showResponseJSONFormatted(Object object){
		
		// Turn the object into JSON 
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String stringJson = gson.toJson(object);
		
		return stringJson;
	}
	
	

}
