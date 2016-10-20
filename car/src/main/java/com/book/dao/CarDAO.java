package com.book.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.book.factory.ConnectionFactory;
import com.book.model.Car;
import com.book.utils.SqlQueries;

public class CarDAO {

	private Connection connection;
	
	public CarDAO (){
		try {
			this.connection = ConnectionFactory.openConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Integer insert(Car car) throws SQLException{
		PreparedStatement stmt;
		String sql = SqlQueries.INSERT_QUERY;
		String key[] = {"id"};
		Integer idGenerated = 0;
		
		try {
			
			stmt = connection.prepareStatement(sql, key);
			
			stmt.setString(1, car.getName());
			stmt.setString(2, car.getDescription());
			stmt.setString(3, car.getUrlPhoto());
			stmt.setString(4, car.getUrlVideo());
			stmt.setString(5, car.getLatitud());
			stmt.setString(6, car.getLongitud());
			stmt.setString(7, car.getType());
			
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			
			while(rs.next()){
				idGenerated = rs.getInt(1);
			}
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
			
		} finally {
			if(connection != null){
				connection.close();
			}
		}
		
		return idGenerated;
	}

	public boolean update(Car car) throws SQLException{
		PreparedStatement stmt;
		try {
			
			stmt = connection.prepareStatement(SqlQueries.UPDATE_QUERY + car.getId());
	
			stmt.setString(1, car.getName());
			stmt.setString(2, car.getDescription());
			stmt.setString(3, car.getUrlPhoto());
			stmt.setString(4, car.getUrlVideo());
			stmt.setString(5, car.getLatitud());
			stmt.setString(6, car.getLongitud());
			stmt.setString(7, car.getType());
			
			stmt.execute();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
			
		} finally {
			if(connection != null){
				connection.close();
			}
		}
		
		return true;
	}
	
	public void delete(int id) throws SQLException{
		PreparedStatement stmt;
		
		try {
			
			stmt = connection.prepareStatement(SqlQueries.DELETE_QUERY);
			stmt.setInt(1, id);
			stmt.execute();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			if(connection != null){
				connection.close();
			}
		}
		
	}
	
	public Car getCarById(Integer id) throws SQLException{
		PreparedStatement stmt;
		Car car = null;
		
		try{
			stmt = connection.prepareStatement(SqlQueries.LIST_QUERY_BY_ID + id);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				car = new Car();
				
				car.setId(rs.getInt("id"));
				car.setName(rs.getString("name"));
				car.setDescription(rs.getString("description"));
				car.setUrlPhoto(rs.getString("url_photo"));
				car.setUrlVideo(rs.getString("url_video"));
				car.setLatitud(rs.getString("latitud"));
				car.setLongitud(rs.getString("longitud"));
				car.setType(rs.getString("type"));
			}
			
			rs.close();
			stmt.close();
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
			
		} finally {
			if(connection != null){
				connection.close();
			}
		}
		
		return car;
	}
	
	public List<Car> getCarByType(String type) throws SQLException{
		PreparedStatement stmt;
		List<Car> list = null;
		
		try{
			stmt = connection.prepareStatement(SqlQueries.LIST_QUERY_BY_TYPE + type + "'");
			ResultSet rs = stmt.executeQuery();
			
			list = new ArrayList<Car>();

			while(rs.next()){
				Car car = new Car();
				
				car.setId(rs.getInt("id"));
				car.setName(rs.getString("name"));
				car.setDescription(rs.getString("description"));
				car.setUrlPhoto(rs.getString("url_photo"));
				car.setUrlVideo(rs.getString("url_video"));
				car.setLatitud(rs.getString("latitud"));
				car.setLongitud(rs.getString("longitud"));
				car.setType(rs.getString("type"));
				
				list.add(car);
			}
			
			rs.close();
			stmt.close();
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
			
		} finally {
			if(connection != null){
				connection.close();
			}
		}
		
		return list;
	}
	
	public List<Car> getCarsList() throws SQLException{   
		PreparedStatement stmt;
		List<Car> list = null;
		
		try {
			stmt = connection.prepareStatement(SqlQueries.LIST_QUERY);
			ResultSet rs = stmt.executeQuery();
			
			list = new ArrayList<Car>();
			
			while(rs.next()){
				Car car = new Car();
				
				car.setId(rs.getInt("id"));
				car.setName(rs.getString("name"));
				car.setDescription(rs.getString("description"));
				car.setUrlPhoto(rs.getString("url_photo"));
				car.setUrlVideo(rs.getString("url_video"));
				car.setLatitud(rs.getString("latitud"));
				car.setLongitud(rs.getString("longitud"));
				car.setType(rs.getString("type"));
				
				list.add(car);
			}
			
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			if(connection != null){
				connection.close();
			}
		}
		
	    return list;
	}


}
