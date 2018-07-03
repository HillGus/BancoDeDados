package br.com.hillbernate.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import br.com.hillbernate.dataHandlers.Database;

public class ConnectionFactory {

	
	private HashMap<String, Connection> conexoes = new HashMap<String, Connection>();
	
	
	public ConnectionFactory addConnection(String url, String user, String password, String banco) {
		
		try {
			Connection con = DriverManager.getConnection(url, user, password);
			conexoes.put(banco, con);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		return this;
	}
	
	public Connection getConnection(String banco) {
		
		return conexoes.get(banco);
	}
	
	public Database getDatabase(String banco) {
		
		return new Database(getConnection(banco));
	}
	
	
	public ConnectionFactory setConnection(String url, String user, String password) {
		
		return addConnection(url, user, password, "main");
	}
	
	public Connection getConnection() {
		
		return getConnection("main");
	}

	public Database getDatabase() {
		
		return getDatabase("main");
	}
}