package com.book.rest;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.book.dao.CarDAO;
import com.book.model.Car;
import com.book.model.ResponseJSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Path("/cars")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class CarsServletResource {

	@GET
	public List<Car> get(){
		List<Car> carsList = null;
		
		try {
			
			CarDAO carDAO = new CarDAO();
			carsList = carDAO.getCarsList();
			
		} catch (SQLException e) {
			e.getMessage();
		}
		
	    return carsList;
	}
	
	@GET
	@Path("{id}")
	public Car get(@PathParam("id") Integer id){
		Car car = null;
		
		try {
			
			CarDAO carDAO = new CarDAO();
			car = carDAO.getCarById(id);
			
		} catch (SQLException e) {
			e.getMessage();
		}
		
	    return car;
	}
	
	@GET
	@Path("/type/{type}")
	public List<Car> getByType(@PathParam("type") String type){
		List<Car> carsList = null;
		
		try {
			
			CarDAO carDAO = new CarDAO();
			carsList = carDAO.getCarByType(type);
			
		} catch (SQLException e) {
			e.getMessage();
		}
		
	    return carsList;
	}
	
	@DELETE
	@Path("{id}")
	public Response delete(@PathParam("id") Integer id){
		String stringDeletedCarJson = "";
		CarDAO carDAO = new CarDAO();
		ResponseJSON response;
		
		try {
			
			if(id != null){
				carDAO.delete(id);
				response = ResponseJSON.ok("Car removed successfully");
				
				stringDeletedCarJson = showResponseJSONFormatted(response);
				
			} else {
				response = ResponseJSON.error("Invalid URL");
				
				stringDeletedCarJson = showResponseJSONFormatted(response);
				
			}
			
		} catch (SQLException e) {
			e.getMessage();
		}
		
	    return Response.status(200).entity(stringDeletedCarJson).build();
	}
	
	@POST
	public Response post(Car car){
		String stringCarJson = "";
		CarDAO carDAO = new CarDAO();
		ResponseJSON response;
		
		try {
			
			carDAO.insert(car);
			response = ResponseJSON.ok("Car added successfully");
			stringCarJson = showResponseJSONFormatted(response);
			
		} catch (SQLException e) {

			response = ResponseJSON.error(e.getMessage());
			
			stringCarJson = showResponseJSONFormatted(response);
		}
		
	    return Response.status(200).entity(stringCarJson).build();
	}

	@PUT
	public Response put(Car car){
		String stringCarJson = "";
		CarDAO carDAO = new CarDAO();
		ResponseJSON response;
		
		try {
			
			carDAO.update(car);
			response = ResponseJSON.ok("Car Updated successfully");
			stringCarJson = showResponseJSONFormatted(response);
			
		} catch (SQLException e) {

			response = ResponseJSON.error(e.getMessage());
			
			stringCarJson = showResponseJSONFormatted(response);
		}
		
	    return Response.status(200).entity(stringCarJson).build();
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
