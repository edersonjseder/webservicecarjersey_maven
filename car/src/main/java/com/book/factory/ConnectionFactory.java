package com.book.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {
	
	private static final String URL = "jdbc:mysql://localhost:3306/book";
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String USUARIO = "book";
	private static final String SENHA = "book123";
	
	public static Connection openConnection() throws SQLException {
		try {
			Class.forName(DRIVER);
			return DriverManager.getConnection(URL, USUARIO, SENHA);
			
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
	}

}
