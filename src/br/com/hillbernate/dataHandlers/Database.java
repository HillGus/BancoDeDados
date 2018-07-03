package br.com.hillbernate.dataHandlers;

import java.sql.Connection;

public class Database {

	
	private Connection conexao;
	
	
	public Database(Connection conexao) {
		
		this.conexao = conexao;
	}
	
	
	public <T extends Object> DataTable<T> getTable(String tabela, T obj) {
		
		DataTable<T> dt = new DataTable<T>(tabela, obj);
		dt.setConnection(conexao);
	
		return dt;
	}
}