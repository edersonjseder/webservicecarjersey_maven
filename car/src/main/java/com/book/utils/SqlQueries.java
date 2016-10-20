package com.book.utils;

public class SqlQueries {
	public static final String INSERT_QUERY = "insert into car (name, description, url_photo, url_video, latitud, longitud, type) values (?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_QUERY = "update car set name=?, description=?, url_photo=?, url_video=?, latitud=?, longitud=?, type=? where id=";
	public static final String DELETE_QUERY = "delete from car where id=?";
	public static final String LIST_QUERY = "select * from car";
	public static final String LIST_QUERY_BY_ID = "select * from car where id = ";
	public static final String LIST_QUERY_BY_TYPE = "select * from car where type = '";
}
