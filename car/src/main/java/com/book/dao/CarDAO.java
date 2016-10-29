package com.book.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.book.model.Car;

@Repository
@SuppressWarnings("unchecked")
public class CarDAO extends HibernateDAO<Car> {

	public CarDAO (){
		super(Car.class);
	}
	
	public void insertOrIpdate(Car car) {
		
		super.saveOrUpdate(car);
		
	}
	
	public boolean delete(int id) {
		
		Car car = get(id);
		delete(car);
		
		return true;
	}
	
	public Car getCarById(Integer id) {

		return super.get(id);

	}
	
	public List<Car> getCarByType(String type) {
		
		Query query = getSession().createQuery("from Car where type = ?");
		query.setString(0, type);
		
		return query.list();
	}
	
	public List<Car> getCarByName(String name) {
		
		Query query = getSession().createQuery("from Car where name = ?");
		query.setString(0, name);
		
		return query.list();
	}
	
	public List<Car> getCarsList() {   
	
		Query query = getSession().createQuery("from Car");
		List<Car> list = query.list();
		
	    return list;
	}


}
