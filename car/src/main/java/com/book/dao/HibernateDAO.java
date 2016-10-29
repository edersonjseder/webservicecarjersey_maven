package com.book.dao;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Generic Hibernate DAO
 * 
 * @author ederson
 *
 * @param <T>
 */
@Component
@SuppressWarnings("unchecked")
public class HibernateDAO<T> {
	
	protected Class<T> clazz;
	protected Session session;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public HibernateDAO(Class<T> clazz){
		this.clazz = clazz;
	}
	
	public HibernateDAO(){
		
	}
	
	public void delete(T entity){
		getSession().delete(entity);
		
	}
	
	public void update(T entity){
		getSession().update(entity);
		
	}
	
	public void save(T entity){
		getSession().save(entity);
		
	}
	
	public void saveOrUpdate(T entity){
		getSession().saveOrUpdate(entity);
		
	}
	
	public T load(Serializable id){
		return (T) getSession().load(this.clazz, id);
		
	}
	
	public T get(Serializable id){
		return (T) getSession().get(this.clazz, id);
		
	}
	
	protected Query createQuery(String query) {
		return getSession().createQuery(query);
	}
	
	protected Criteria createCriteria() {
		return getSession().createCriteria(this.clazz);
	}
	
	protected Session getSession(){
		
		if(sessionFactory != null){
			session = sessionFactory.getCurrentSession();
		}
		
		if(session == null){
			throw new RuntimeException("Hibernate session is null");
		}
		
		return session;
	}
	
	public SessionFactory getSessionFactory(){
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
