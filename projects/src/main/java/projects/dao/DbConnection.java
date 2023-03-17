package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects.exception.DbException;

public class DbConnection 
{
	public static String HOST     = "localhost";
	public static String PASSWORD = "projects";
	public static String USER     = "projects";
	public static String SCHEMA   = "projects";
	public static int    PORT     = 3306;
	
	public static Connection getConnection()
	{
		String url = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s&useSSL=false", HOST, PORT, SCHEMA, USER, PASSWORD);
		//System.out.println(url);
		try 
		{
			Connection connection = DriverManager.getConnection(url);
			System.out.println("connected");
			return connection;
		} 
		catch (SQLException e)
		{				
			throw new DbException(e);
		}		
	}
}