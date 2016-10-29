package com.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.book.dao.CarDAO;
import com.book.model.Car;


@Component
public class CarService {
	
	@Autowired
	private CarDAO dao;
	
	public List<Car> getCars() {
		
		List<Car> carsList = dao.getCarsList();
		
		return carsList;
	}
	
	public Car getCarById(Integer id) {
		return dao.getCarById(id);
		
	}
	
	public List<Car> getCarByType(String type) {
		return dao.getCarByType(type);
	}
	
	public List<Car> getCarByName(String name) {
		return dao.getCarByName(name);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public boolean delete(Integer id) {
		return dao.delete(id);
	}	
	
	@Transactional(rollbackFor=Exception.class)
	public boolean saveOrUpdate(Car car) {
		
		dao.saveOrUpdate(car);
		
		return true;
	}

}
